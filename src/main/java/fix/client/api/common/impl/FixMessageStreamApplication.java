package fix.client.api.common.impl;

import fix.client.api.sessions.events.FixConnectionStatusUpdateEvent;
import fix.client.api.subscriptions.models.FixSessionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.codec.ServerSentEvent;
import quickfix.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static fix.client.api.common.enums.FixConnectionStatus.*;

@Slf4j
@RequiredArgsConstructor
public class FixMessageStreamApplication implements Application {

    private final String fixSessionID;

    @Getter
    private final ApplicationEventPublisher eventPublisher;

    private final Set<FixSessionSubscriber> subscribers = new HashSet<>();

    private final List<String> lastMessages = new ArrayList<>();

    public void addSubscriber(FixSessionSubscriber subscriber) {
        subscribers.add(subscriber);
        lastMessages.forEach(m -> pushMessage(subscriber, m));
    }

    public void delSubscriber(String subscribeID) {
        subscribers
                .stream()
                .filter(subscriber -> subscriber.getProperties().getSubscriptionID().equals(subscribeID))
                .findFirst()
                .ifPresent((subscribers::remove));
    }

    @Override
    public void onCreate(SessionID sessionID) {
        Session.lookupSession(sessionID).addStateListener(new SessionStateListener() {
            @Override
            public void onConnectException(Exception exception) {
                var message = exception.getMessage();
                lastMessages.add(message);
                pushMessage(message);
                pushMessage("The connection will be interrupted [check session configuration]");
                subscribers.forEach(subscriber -> subscriber.getStream().complete());
                eventPublisher.publishEvent(
                        new FixConnectionStatusUpdateEvent(fixSessionID, STOP_BY_SYSTEM)
                );
            }
        });
    }

    @Override
    public void onLogon(SessionID sessionID) {
        eventPublisher.publishEvent(new FixConnectionStatusUpdateEvent(fixSessionID, OPEN));
    }

    @Override
    public void onLogout(SessionID sessionID) {

    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) {
        pushMessage(message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) {
        pushMessage(message);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        pushMessage(message);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) {
        pushMessage(message);
    }

    public void pushMessage(Message message) {
        String className = message.getClass().getSimpleName();
        String messageRaw = message.toRawString().replace('\u0001', '|');
        String messageText = className + " " + messageRaw;
        pushMessage(messageText);
    }

    public void pushMessage(String message) {
        subscribers.forEach(s -> pushMessage(s, message));
    }

    public void pushMessage(FixSessionSubscriber subscriber, String message) {
        var sse = ServerSentEvent.builder(message).build();
        subscriber.getStream().next(sse);
    }
}


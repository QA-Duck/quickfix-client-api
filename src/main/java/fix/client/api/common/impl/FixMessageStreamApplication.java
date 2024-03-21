package fix.client.api.common.impl;

import fix.client.api.common.enums.FixSubscriberStatus;
import fix.client.api.sessions.events.FixConnectionStatusUpdateEvent;
import fix.client.api.subscriptions.events.FixSubscriptionEvent;
import fix.client.api.subscriptions.events.SubscriptionCloseEvent;
import fix.client.api.subscriptions.events.SubscriptionListenEvent;
import fix.client.api.subscriptions.models.FixSessionSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.ServerSentEvent;
import quickfix.*;

import java.util.HashSet;
import java.util.Set;

import static fix.client.api.common.enums.FixConnectionStatus.CLOSE;
import static fix.client.api.common.enums.FixConnectionStatus.OPEN;

@Slf4j
@RequiredArgsConstructor
public class FixMessageStreamApplication implements Application {

    private final String fixSessionID;

    private final ApplicationEventPublisher eventPublisher;

    private final Set<FixSessionSubscriber> subscribers = new HashSet<>();

    public void addSubscriber(FixSessionSubscriber subscriber) {
        subscribers.add(subscriber);
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
                var close = new FixConnectionStatusUpdateEvent(fixSessionID, CLOSE);
                eventPublisher.publishEvent(close);
                pushMessage(exception.getMessage());
            }
        });
    }

    @Override
    public void onLogon(SessionID sessionID) {
        var open = new FixConnectionStatusUpdateEvent(fixSessionID, OPEN);
        eventPublisher.publishEvent(open);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        var close = new FixConnectionStatusUpdateEvent(fixSessionID, CLOSE);
        eventPublisher.publishEvent(close);
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

    public void pushMessage( Message message) {
        String className = message.getClass().getSimpleName();
        String messageRaw = message.toRawString().replace('\u0001', '|');
        String messageText = className + " " + messageRaw;
        pushMessage(messageText);
    }

    public void pushMessage(String message) {
        var sse = ServerSentEvent.builder(message).build();
        subscribers.forEach(s -> {
            s.getStream().next(sse);
        });
    }
}


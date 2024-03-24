package fix.client.api.common.impl;

import fix.client.api.sessions.events.FixConnectionStatusUpdateEvent;
import fix.client.api.sessions.events.FixConnectionErrorEvent;
import fix.client.api.subscriptions.events.SubscriptionMessageReceivedEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import quickfix.*;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static fix.client.api.common.enums.FixConnectionStatus.*;
import static quickfix.Session.lookupSession;

@Slf4j
@RequiredArgsConstructor
public class FixMessageStreamApplication implements Application {

    private final String fixSessionID;

    @Getter
    private final ApplicationEventPublisher eventPublisher;

    @Getter
    private final Set<Session> lookupSessions = new HashSet<>();

    @Override
    public void onCreate(SessionID sessionID) {
        lookupSession(sessionID).addStateListener(new SessionStateListener() {
            @Override
            public void onConnectException(Exception exception) {
                eventPublisher.publishEvent(
                        new FixConnectionErrorEvent(exception, fixSessionID)
                );
            }

            @Override
            public void onConnect() {
                eventPublisher.publishEvent(
                        new FixConnectionStatusUpdateEvent(fixSessionID, CONNECTED)
                );
            }

            @Override
            public void onDisconnect() {
                eventPublisher.publishEvent(
                        new FixConnectionStatusUpdateEvent(fixSessionID, DISCONNECTED)
                );
            }
        });
    }

    @Override
    public void onLogon(SessionID sessionId) {
        eventPublisher.publishEvent(
                new FixConnectionStatusUpdateEvent(fixSessionID, LOG_ON)
        );
    }

    @Override
    public void onLogout(SessionID sessionID) {
        eventPublisher.publishEvent(
                new FixConnectionStatusUpdateEvent(fixSessionID, LOG_OUT)
        );
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

    private void pushMessage(Message message) {
        eventPublisher.publishEvent(new SubscriptionMessageReceivedEvent(message, fixSessionID));
    }
}


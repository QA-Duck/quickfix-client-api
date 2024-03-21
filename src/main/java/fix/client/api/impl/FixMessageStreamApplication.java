package fix.client.api.impl;

import fix.client.api.events.FixConnectionStatusUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.codec.ServerSentEvent;
import quickfix.*;
import reactor.core.publisher.FluxSink;

import java.util.*;

import static fix.client.api.enums.FixConnectionStatus.CLOSE;
import static fix.client.api.enums.FixConnectionStatus.OPEN;

@Slf4j
@RequiredArgsConstructor
public class FixMessageStreamApplication implements Application{

    private final String fixSessionID;

    private final ApplicationEventPublisher eventPublisher;


    @Override
    public void onCreate(SessionID sessionID) {
        Session.lookupSession(sessionID).addStateListener(new SessionStateListener() {
            @Override
            public void onConnectException(Exception exception) {
                var close = new FixConnectionStatusUpdateEvent(fixSessionID, CLOSE);
                eventPublisher.publishEvent(close);
                catchMessage(exception.getMessage());
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
    public void fromAdmin(Message message, SessionID sessionID) { catchMessage(message);}

    @Override
    public void fromApp(Message message, SessionID sessionID) { catchMessage(message); }

    @Override
    public void toAdmin(Message message, SessionID sessionID) { catchMessage(message); }

    @Override
    public void toApp(Message message, SessionID sessionId) { catchMessage(message); }


}


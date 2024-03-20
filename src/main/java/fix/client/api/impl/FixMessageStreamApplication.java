package fix.client.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import quickfix.*;
import reactor.core.publisher.FluxSink;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class FixMessageStreamApplication implements Application{

    private final HashMap<String, FluxSink<ServerSentEvent<String>>> subscribers = new HashMap<>();

    public String subscribe(FluxSink<ServerSentEvent<String>> sink) {
        String uuid = "Sub_" + UUID.randomUUID();
        subscribers.put(uuid, sink);
        return uuid;
    }

    public void unsubscribe(String subscriptionUUID) {
        subscribers.remove(subscriptionUUID);
    }

    @Override
    public void onCreate(SessionID sessionID) {
        log.info("SESSION CREATE");
        Session.lookupSession(sessionID).addStateListener(new SessionStateListener() {
            @Override
            public void onConnectException(Exception exception) {
                catchMessage(exception.getMessage());
            }
        });
    }

    @Override
    public void onLogon(SessionID sessionID) {
    }

    @Override
    public void onLogout(SessionID sessionID) {
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) {
        catchMessage(message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) {
        catchMessage(message);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        catchMessage(message);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) {
        catchMessage(message);
    }

    private void catchMessage(Message message) {
        String className = message.getClass().getSimpleName();
        String messageRaw = message.toRawString().replace('\u0001', '|');
        String messageText = className + " " + messageRaw;
        log.info("catch message {}", messageText);
        catchMessage(messageText);
    }

    private void catchMessage(String message) {
        var sse = ServerSentEvent.builder(message).build();
        log.info("catch message {}", message);
        for (var sink : subscribers.values()) {
            sink.next(sse);
        }
    }
}


package fix.client.api.impl;

import fix.client.api.enums.FixConnectionStatuses;
import fix.client.api.enums.SessionStartStatus;
import fix.client.api.models.FixConnectionStatus;
import fix.client.api.models.FixSessionInfo;
import io.netty.channel.unix.Limits;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import quickfix.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static fix.client.api.enums.FixConnectionStatuses.failed;

@Slf4j
@RequiredArgsConstructor
public class FixSender implements Application{

    private final Set<FluxSink<ServerSentEvent<String>>> subscribers = new HashSet<>();

    private final FixSessionInfo sessionInfo;

    public void subscribe(FluxSink<ServerSentEvent<String>> sink) {
        subscribers.add(sink);
    }

    public void unsubscribe(FluxSink<ServerSentEvent<String>> sink) {
        subscribers.remove(sink);
    }

    @Override
    public void onCreate(SessionID sessionID) {
        log.info("SESSION CREATE");
        Session.lookupSession(sessionID).addStateListener(new SessionStateListener() {
            @Override
            public void onConnectException(Exception exception) {
                catchMessage(exception.getMessage());
                sessionInfo.setConnectionStatus(failed(exception.getMessage()));
            }
        });
    }

    @Override
    public void onLogon(SessionID sessionID) {
        sessionInfo.setConnectionStatus(FixConnectionStatuses.IS_LOGGED_ON);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        sessionInfo.setConnectionStatus(FixConnectionStatuses.IS_LOGGED_ON);
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
        for (var sink : subscribers) {
            sink.next(sse);
        }
    }
}


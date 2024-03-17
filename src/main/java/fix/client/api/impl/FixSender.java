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
import java.util.function.Supplier;

import static fix.client.api.enums.FixConnectionStatuses.failed;

@Slf4j
@RequiredArgsConstructor
public class FixSender implements Application, SessionStateListener {

    private final FixSessionInfo sessionInfo;

    @Getter
    private Sinks.Many<ServerSentEvent<String>> serverSentEventSink = Sinks
            .many().replay().limit(Duration.ZERO);

    @Override
    public void onCreate(SessionID sessionID) { }

    @Override
    public void onLogon(SessionID sessionID) {
        sessionInfo.setConnectionStatus(FixConnectionStatuses.IS_LOGGED_ON);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        sessionInfo.setConnectionStatus(FixConnectionStatuses.IS_LOGGED_ON);
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws
            FieldNotFound,
            IncorrectDataFormat,
            IncorrectTagValue,
            RejectLogon {
        catchMessage(message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws
            FieldNotFound,
            IncorrectDataFormat,
            IncorrectTagValue,
            UnsupportedMessageType {
        catchMessage(message);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        catchMessage(message);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        catchMessage(message);
    }

    @Override
    public void onConnectException(Exception exception) {
        sessionInfo.setConnectionStatus(failed(exception.getMessage()));
        serverSentEventSink
                .tryEmitNext(ServerSentEvent.builder(exception.getMessage()).build())
                .orThrow();
    }

    private void catchMessage(Message message) {
        String className = message.getClass().getSimpleName();
        String messageText = message.toRawString().replace('\u0001', '|');
        serverSentEventSink
                .tryEmitNext(ServerSentEvent.builder(className+ " " + messageText).build())
                .orThrow();
    }

    public Supplier<Flux<ServerSentEvent<String>>> getFlux() {
        return () -> serverSentEventSink.asFlux();
    }
}


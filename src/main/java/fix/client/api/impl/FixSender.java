package fix.client.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import quickfix.*;
import reactor.core.publisher.FluxSink;

@Slf4j
public class FixSender implements Application {

    public static FluxSink<ServerSentEvent<String>> logStoreSink;

    @Override
    public void onCreate(SessionID message) {}

    @Override
    public void onLogon(SessionID message) {}

    @Override
    public void onLogout(SessionID message) {}

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

    private void catchMessage(Message message) {
        if (logStoreSink != null && message != null) {
            logStoreSink.next(ServerSentEvent.builder(
                    message.getClass().getSimpleName() + " " + message.toRawString().replace('\u0001', '|')
            ).build());
        }
    }
}


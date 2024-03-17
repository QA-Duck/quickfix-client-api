package fix.client.api.impl;

import fix.client.api.enums.FixConnectionStatuses;
import fix.client.api.enums.SessionStartStatus;
import fix.client.api.models.FixConnectionStatus;
import fix.client.api.models.FixSessionInfo;
import lombok.Setter;
import org.springframework.http.codec.ServerSentEvent;
import quickfix.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Supplier;

import static fix.client.api.enums.FixConnectionStatuses.failed;

public class FixInitiator {

    private final FixSender application;
    private final FixSessionInfo fixSessionInfo;
    private final SessionSettings sessionSettings;
    private final SocketInitiator socketInitiator;

    @Setter
    private FluxSink<ServerSentEvent<String>> fluxSink;

    public FixInitiator(
            FixSessionInfo fixSessionInfo,
            SessionSettings sessionSettings
    ) {
        this.fixSessionInfo = fixSessionInfo;
        this.sessionSettings = sessionSettings;
        this.application = new FixSender(fixSessionInfo);
        FileStoreFactory fileStoreFactory = new FileStoreFactory(sessionSettings);
        ScreenLogFactory screenLogFactory = new ScreenLogFactory(sessionSettings);
        DefaultMessageFactory msgFactory = new DefaultMessageFactory();
        try {
            this.socketInitiator = new SocketInitiator(
                    this.application, fileStoreFactory, sessionSettings, screenLogFactory, msgFactory
            );
        } catch (ConfigError e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SessionID getSessionID() {
        return socketInitiator.getSessions().get(0);
    }

    public FixSessionInfo connect() {
        try {
            socketInitiator.start();
            fixSessionInfo.setConnectionStatus(FixConnectionStatuses.IN_PROGRESS);
        } catch (Exception e) {
            fixSessionInfo.setConnectionStatus(failed(e.getMessage()));
        }
        return fixSessionInfo;
    }

    public FixSessionInfo disconnect() {
        socketInitiator.stop();
        return fixSessionInfo;
    }

    public Supplier<Flux<ServerSentEvent<String>>> getFlux() {
        return application.getFlux();
    }
}

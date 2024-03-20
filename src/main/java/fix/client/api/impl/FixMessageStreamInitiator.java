package fix.client.api.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import quickfix.*;

@Slf4j
public class FixMessageStreamInitiator {
    private final SocketInitiator socketInitiator;
    @Getter
    private final FixMessageStreamApplication fixMessageStreamApplication;

    public FixMessageStreamInitiator(
            @NonNull SessionSettings sessionSettings,
            @NonNull FixMessageStreamApplication fixMessageStreamApplication
    ) {
        this.fixMessageStreamApplication = fixMessageStreamApplication;
        this.socketInitiator = initializeSocketConnector(sessionSettings);
    }

    private SocketInitiator initializeSocketConnector(SessionSettings sessionSettings) {
        FileStoreFactory fileStoreFactory = new FileStoreFactory(sessionSettings);
        ScreenLogFactory screenLogFactory = new ScreenLogFactory(sessionSettings);
        DefaultMessageFactory msgFactory = new DefaultMessageFactory();
        try {
            return new SocketInitiator(
                    this.fixMessageStreamApplication,
                    fileStoreFactory,
                    sessionSettings,
                    screenLogFactory,
                    msgFactory
            );
        } catch (ConfigError e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SessionID getSessionID() {
        return socketInitiator.getSessions().get(0);
    }

    public void connect() {
        try {
            log.info("Try to start connect");
            socketInitiator.start();
        } catch (Exception e) {
            log.error("Connect is failed {}", e.getMessage());
        }
    }

    public void disconnect() {
        socketInitiator.stop();
    }
}

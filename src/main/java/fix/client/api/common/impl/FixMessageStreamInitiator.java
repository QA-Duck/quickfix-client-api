package fix.client.api.common.impl;


import fix.client.api.sessions.events.FixConnectionStatusUpdateEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import quickfix.*;

import static fix.client.api.common.enums.FixConnectionStatus.*;

@Slf4j
public class FixMessageStreamInitiator {
    private final String sessionID;
    private final SocketInitiator socketInitiator;
    @Getter
    private final FixMessageStreamApplication fixMessageStreamApplication;

    public FixMessageStreamInitiator(
            String sessionID,
            SessionSettings sessionSettings,
            FixMessageStreamApplication fixMessageStreamApplication
    ) {
        this.sessionID = sessionID;
        this.fixMessageStreamApplication = fixMessageStreamApplication;
        this.socketInitiator = initializeSocketConnector(sessionSettings, fixMessageStreamApplication);
    }

    private SocketInitiator initializeSocketConnector(
            SessionSettings sessionSettings,
            FixMessageStreamApplication fixMessageStreamApplication
    ) {
        FileStoreFactory fileStoreFactory = new FileStoreFactory(sessionSettings);
        ScreenLogFactory screenLogFactory = new ScreenLogFactory(sessionSettings);
        DefaultMessageFactory msgFactory = new DefaultMessageFactory();
        try {
            return new SocketInitiator(
                    fixMessageStreamApplication,
                    fileStoreFactory,
                    sessionSettings,
                    screenLogFactory,
                    msgFactory
            );
        } catch (ConfigError e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void connect() {
        try {
            log.info("Try to start connect");
            socketInitiator.start();
        } catch (Exception e) {
            log.error("Connect is failed {}", e.getMessage());
            fixMessageStreamApplication.getEventPublisher().publishEvent(
                    new FixConnectionStatusUpdateEvent(sessionID, FAILED)
            );
        }
    }

    public void disconnect() {
        socketInitiator.stop();
    }
}

package fix.client.api.impl;

import org.springframework.http.codec.ServerSentEvent;
import quickfix.*;
import reactor.core.publisher.FluxSink;

public class FixInitiator {

    private final SessionSettings settings;
    private final SocketInitiator socketInitiator;
    private FluxSink<ServerSentEvent<String>> fluxSink;

    public FixInitiator(SessionSettings settings) {
        this.settings = settings;
        Application myApp = new FixSender();
        FileStoreFactory fileStoreFactory = new FileStoreFactory(settings);
        ScreenLogFactory screenLogFactory = new ScreenLogFactory(settings);
        DefaultMessageFactory msgFactory = new DefaultMessageFactory();
        try {
            this.socketInitiator = new SocketInitiator(
                    myApp, fileStoreFactory, settings, screenLogFactory, msgFactory
            );
        } catch (ConfigError e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SessionID getSessionID() {
        return socketInitiator.getSessions().get(0);
    }

    public void start() {
        try {
            socketInitiator.start();
        } catch (ConfigError e) {
            throw new RuntimeException(e);
        }
    }
}

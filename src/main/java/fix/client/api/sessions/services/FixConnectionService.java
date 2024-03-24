package fix.client.api.sessions.services;

import fix.client.api.sessions.events.FixConnectionStatusUpdateEvent;
import fix.client.api.sessions.store.FixConnectionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import static fix.client.api.common.enums.FixConnectionStatus.*;


@Slf4j
@Service
public class FixConnectionService {

    private final FixConnectionStore fixConnectionStore;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public FixConnectionService(
            FixConnectionStore fixConnectionStore,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.fixConnectionStore = fixConnectionStore;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void disconnect(String sessionID) {
        applicationEventPublisher.publishEvent(
                new FixConnectionStatusUpdateEvent(sessionID, TRY_TO_STOPPING)
        );
        fixConnectionStore
                .select(sessionID)
                .disconnect();
        applicationEventPublisher.publishEvent(
                new FixConnectionStatusUpdateEvent(sessionID, DISCONNECTED)
        );
    }

    public void connect(String sessionID) {
        applicationEventPublisher.publishEvent(
                new FixConnectionStatusUpdateEvent(sessionID, TRY_TO_STARTING)
        );
        fixConnectionStore
                .select(sessionID)
                .connect();
    }
}

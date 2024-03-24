package fix.client.api.sessions.handlers;

import fix.client.api.repositories.FixSessionMapRepository;
import fix.client.api.sessions.events.FixConnectionErrorEvent;
import fix.client.api.sessions.events.FixConnectionStatusUpdateEvent;
import fix.client.api.sessions.services.FixConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static fix.client.api.common.enums.FixConnectionStatus.DISCONNECTED;
import static fix.client.api.common.enums.FixConnectionStatus.TRY_TO_STOPPING;

@Slf4j
@Service
public class ConnectionStatusUpdateHandler {

    private final FixSessionMapRepository fixSessionMapRepository;

    private final FixConnectionService connectionService;

    private final Map<String, AtomicInteger> failsCount = new HashMap<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    @Autowired
    public ConnectionStatusUpdateHandler(
            FixSessionMapRepository fixSessionMapRepository,
            FixConnectionService connectionService
    ) {
        this.fixSessionMapRepository = fixSessionMapRepository;
        this.connectionService = connectionService;
    }

    @Async
    @EventListener
    public void handleConnectionExceptionEvent(FixConnectionErrorEvent event) {
        var status = fixSessionMapRepository.select(event.getSessionID()).getStatus();
        if (!status.equals(DISCONNECTED) && !status.equals(TRY_TO_STOPPING)) {
            log.info(
                    "[ERROR] ({}) Session exception: {}",
                    event.getSessionID(),
                    event.getException().getMessage()
            );
            executor.execute(() -> connectionService.disconnect(event.getSessionID()));
        }
    }

    @EventListener
    public void handleConnectionUpdateEvent(FixConnectionStatusUpdateEvent event) {
        log.info("Connection status was update {} for {}", event.getStatus(), event.getSessionID());
        fixSessionMapRepository.updateStatus(
                event.getStatus(),
                event.getSessionID()
        );
    }
}

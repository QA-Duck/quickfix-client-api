package fix.client.api.subscriptions.handlers;

import fix.client.api.sessions.services.FixConnectionService;
import fix.client.api.subscriptions.events.SubscriptionListenEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SubscriptionHandler {

    private final FixConnectionService connectionService;

    @Autowired
    public SubscriptionHandler(FixConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @EventListener
    public void handleSubscribeEvent(SubscriptionListenEvent event) {
        log.info("Subscribe for {}", event.getSessionID());
        connectionService.addSubscriber(
                event.getSubscriber(),
                event.getSessionID()
        );
    }
}

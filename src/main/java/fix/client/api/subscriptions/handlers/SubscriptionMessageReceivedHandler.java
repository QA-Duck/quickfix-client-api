package fix.client.api.subscriptions.handlers;

import fix.client.api.subscriptions.events.SubscriptionMessageReceivedEvent;
import fix.client.api.subscriptions.services.FixSubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubscriptionMessageReceivedHandler {

    private final FixSubscriberService subscriberService;

    @Autowired
    public SubscriptionMessageReceivedHandler(FixSubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @EventListener
    private void fixMessageHandler(SubscriptionMessageReceivedEvent event) {
        log.info("Send message all subscribers for session {}", event.getSessionID() );
        subscriberService.sendMessageToSubscribers(
                event.getSessionID(),
                event.getMessage()
        );
    }
}

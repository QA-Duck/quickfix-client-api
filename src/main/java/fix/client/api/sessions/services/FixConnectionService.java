package fix.client.api.sessions.services;

import fix.client.api.common.enums.FixSubscriberStatus;
import fix.client.api.common.impl.FixMessageStreamApplication;
import fix.client.api.common.impl.FixMessageStreamInitiator;
import fix.client.api.sessions.models.FixSessionProperties;
import fix.client.api.repositories.FixSessionMapRepository;
import fix.client.api.subscriptions.events.SubscriptionCloseEvent;
import fix.client.api.subscriptions.events.SubscriptionListenEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FixConnectionService {
    private final FixSessionMapRepository repository;
    private final ApplicationEventPublisher publisher;
    private final Map<String, FixMessageStreamInitiator> connections = new HashMap<>();

    @Autowired
    public FixConnectionService(
            FixSessionMapRepository repository,
            ApplicationEventPublisher publisher
    ) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @EventListener
    public void handleSubscribeEvent(SubscriptionListenEvent event) {
        log.info("Subscribe for {}", event.getSubscriber().getProperties().getSessionID());
        connections
                .get(event.getSubscriber().getProperties().getSessionID())
                .getFixMessageStreamApplication()
                .addSubscriber(event.getSubscriber());
    }

    @EventListener
    public void handleUnsubscribeEvent(SubscriptionCloseEvent event) {
        log.info("Unsubscribe for {}", event.getSessionID());
        connections
                .get(event.getSessionID())
                .getFixMessageStreamApplication()
                .delSubscriber(event.getSubscriberID());
    }

    public FixSessionProperties create(FixSessionProperties connectionProperties) {
        var property = repository.create(connectionProperties);
        connections.put(
                property.getID(),
                new FixMessageStreamInitiator(
                        connectionProperties.createSettings(),
                        new FixMessageStreamApplication(property.getID(), publisher)
                )
        );
        return property;
    }

    public void delete(String sessionID) {
        disconnect(sessionID);
        connections.remove(sessionID);
    }

    public void disconnect(String sessionID) {
        connections.get(sessionID).disconnect();
    }

    public void connect(String sessionID) {
        connections.get(sessionID).connect();
    }
}

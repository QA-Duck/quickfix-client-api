package fix.client.api.sessions.services;

import fix.client.api.common.enums.FixConnectionStatus;
import fix.client.api.common.impl.FixMessageStreamApplication;
import fix.client.api.common.impl.FixMessageStreamInitiator;
import fix.client.api.sessions.events.FixConnectionStatusUpdateEvent;
import fix.client.api.sessions.models.FixSessionProperties;
import fix.client.api.repositories.FixSessionMapRepository;
import fix.client.api.subscriptions.events.SubscriptionCloseEvent;
import fix.client.api.subscriptions.events.SubscriptionListenEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import static fix.client.api.common.enums.FixConnectionStatus.*;

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
        var sessionID = event.getSubscriber().getProperties().getSessionID();
        log.info("Subscribe for {}", sessionID);
        connections
                .get(sessionID)
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

    @EventListener
    public void handleConnectionUpdateEvent(FixConnectionStatusUpdateEvent event) {
        log.info("Connection status was update {} for {}", event.getStatus(), event.getSessionID());
        var properties = repository.select(event.getSessionID());
        if (event.getStatus().equals(FAILED)) {
            disconnect(event.getSessionID());
            properties.setStatus(event.getStatus());
            repository.update(properties);
        }
    }

    public FixSessionProperties create(FixSessionProperties connectionProperties) {
        var property = repository.create(connectionProperties);
        connections.put(
                property.getID(),
                new FixMessageStreamInitiator(
                        connectionProperties.getSessionID(),
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
        if (repository.select(sessionID).getStatus() != CLOSE) {
            publisher.publishEvent(
                    new FixConnectionStatusUpdateEvent(sessionID, CLOSE)
            );
            connections.get(sessionID).disconnect();
        }
    }

    public void connect(String sessionID) {
        publisher.publishEvent(
                new FixConnectionStatusUpdateEvent(sessionID, STARTING)
        );
        connections.get(sessionID).connect();
    }
}

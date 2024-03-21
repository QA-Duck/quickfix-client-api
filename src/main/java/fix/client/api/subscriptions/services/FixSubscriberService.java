package fix.client.api.subscriptions.services;

import fix.client.api.common.enums.FixSubscriberStatus;
import fix.client.api.subscriptions.events.SubscriptionCloseEvent;
import fix.client.api.subscriptions.events.SubscriptionListenEvent;
import fix.client.api.subscriptions.models.FixSessionSubscriber;
import fix.client.api.subscriptions.models.FixSessionSubscriberProperties;
import fix.client.api.repositories.FixSubscriberMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import static fix.client.api.common.enums.FixSubscriberStatus.CREATED;


@Service
public class FixSubscriberService {
    private final ApplicationEventPublisher eventPublisher;

    private final FixSubscriberMapRepository fixSubscriberMapRepository;

    @Autowired
    public FixSubscriberService(
            ApplicationEventPublisher eventPublisher,
            FixSubscriberMapRepository repository
    ) {
        this.eventPublisher = eventPublisher;
        this.fixSubscriberMapRepository = repository;
    }

    public FixSessionSubscriberProperties create(String sessionID) {
        return fixSubscriberMapRepository.create(
                new FixSessionSubscriberProperties(sessionID, "", CREATED)
        );
    }

    public void subscribe(
            String subscriberID,
            FluxSink<ServerSentEvent<String>> sink
    ) {
        var updated = fixSubscriberMapRepository.select(subscriberID);
        updated.setStatus(FixSubscriberStatus.LISTEN);
        fixSubscriberMapRepository.update(updated);
        eventPublisher.publishEvent(new SubscriptionListenEvent(
                new FixSessionSubscriber(updated, sink)
        ));
    }

    public void unsubscribe(String subscriberID) {
        var updated = fixSubscriberMapRepository.select(subscriberID);
        updated.setStatus(FixSubscriberStatus.CLOSE);
        fixSubscriberMapRepository.update(updated);
        eventPublisher.publishEvent(new SubscriptionCloseEvent(
                subscriberID, updated.getSessionID()
        ));
    }
}

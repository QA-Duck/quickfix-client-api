package fix.client.api.services;

import fix.client.api.enums.FixSubscriberStatus;
import fix.client.api.events.FixSubscriptionStatusUpdateEvent;
import fix.client.api.models.properties.FixSessionSubscriber;
import fix.client.api.repositories.FixSubscriberMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import quickfix.Message;
import reactor.core.publisher.FluxSink;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static fix.client.api.enums.FixSubscriberStatus.CREATED;


@Service
public class FixSubscriberService {
    private final ApplicationEventPublisher eventPublisher;

    private final FixSubscriberMapRepository repository;

    private final Map<String, FluxSink<ServerSentEvent<String>>> sinks = new HashMap<>();

    @Autowired
    public FixSubscriberService(
            ApplicationEventPublisher eventPublisher,
            FixSubscriberMapRepository repository
    ) {
        this.eventPublisher = eventPublisher;
        this.repository = repository;
    }

    public FixSessionSubscriber create(String sessionID) {
        return repository.create(
                new FixSessionSubscriber(sessionID, "", CREATED)
        );
    }

    public FixSessionSubscriber subscribeTo(String subscribeID, FluxSink<ServerSentEvent<String>> sink) {
        sinks.put(subscribeID, sink);
        repository.u
        return ;
    }

    public String unsubscribe(String sessionID, String subscribeID) {
        eventPublisher.publishEvent(new FixSubscriptionStatusUpdateEvent(
                sessionID,
                subscribeID,
                FixSubscriberStatus.CLOSE
        ));
        subscribers.get(sessionID).remove(subscribeID);
    }


    public void catchMessage(String sessionID, Message message) {
        String className = message.getClass().getSimpleName();
        String messageRaw = message.toRawString().replace('\u0001', '|');
        String messageText = className + " " + messageRaw;
        catchMessage(sessionID, messageText);
    }

    public void catchMessage(String sessionID, String message) {
        var sse = ServerSentEvent.builder(message).build();
        subscribers
                .get(sessionID)
                .values()
                .forEach(sink -> {
                    sink.next(sse);
                });
    }
}

package fix.client.api.subscriptions.services;

import fix.client.api.subscriptions.models.FixSessionSubscriber;
import fix.client.api.subscriptions.models.FixSessionSubscriberProperties;
import fix.client.api.repositories.FixSubscriberMapRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import quickfix.Message;
import reactor.core.publisher.FluxSink;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static fix.client.api.common.enums.FixSubscriberStatus.*;


@Slf4j
@Service
public class FixSubscriberService {

    private final FixSubscriberMapRepository fixSubscriberMapRepository;

    private final Map<String, Set<FixSessionSubscriber>> subscribers = new HashMap<>();

    @Autowired
    public FixSubscriberService(FixSubscriberMapRepository fixSubscriberMapRepository) {
        this.fixSubscriberMapRepository = fixSubscriberMapRepository;
    }

    public FixSessionSubscriberProperties create(String sessionID) {
        var properties = new FixSessionSubscriberProperties(sessionID, "", CREATED);
        return fixSubscriberMapRepository.create(properties);
    }

    public void sendMessageToSubscribers(String sessionID, Message message) {
        subscribers.get(sessionID).forEach(subscriber -> subscriber
                .getStream()
                .next(ServerSentEvent.builder(message.toRawString()).build())
        );
    }

    public void subscribe(String subscriberID, FluxSink<ServerSentEvent<String>> sink) {
        var properties = fixSubscriberMapRepository.updateStatus(LISTEN, subscriberID);
        var sessionID = properties.getSessionID();
        if (!subscribers.containsKey(sessionID))
            subscribers.put(sessionID, new HashSet<>());
        subscribers.get(sessionID).add(new FixSessionSubscriber(properties, sink));
    }

    public void unsubscribe(String subscriberID) {
        var properties = fixSubscriberMapRepository.updateStatus(CLOSE, subscriberID);
        var sessionID = properties.getSessionID();
        if (subscribers.containsKey(sessionID))
            subscribers
                    .get(sessionID)
                    .stream()
                    .filter(s -> s
                            .getProperties()
                            .getSubscriptionID()
                            .equals(properties.getSubscriptionID())
                    )
                    .findFirst()
                    .ifPresent((s) -> subscribers.get(sessionID).remove(s));
    }
}

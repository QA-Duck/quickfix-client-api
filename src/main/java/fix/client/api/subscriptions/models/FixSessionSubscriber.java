package fix.client.api.subscriptions.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

@RequiredArgsConstructor
public class FixSessionSubscriber {
    @Getter
    private final FixSessionSubscriberProperties properties;

    @Getter
    private final FluxSink<ServerSentEvent<String>> stream;
}

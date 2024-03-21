package fix.client.api.subscriptions.events;

import fix.client.api.subscriptions.models.FixSessionSubscriber;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class FixSubscriptionEvent extends ApplicationEvent {
    @Getter
    private final FixSessionSubscriber subscriber;

    public FixSubscriptionEvent(FixSessionSubscriber subscriber) {
        super(subscriber);
        this.subscriber = subscriber;
    }
}

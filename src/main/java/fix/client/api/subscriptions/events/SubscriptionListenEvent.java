package fix.client.api.subscriptions.events;

import fix.client.api.subscriptions.models.FixSessionSubscriber;
import fix.client.api.subscriptions.models.FixSessionSubscriberProperties;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class SubscriptionListenEvent extends ApplicationEvent {

    @Getter
    private final FixSessionSubscriber subscriber;

    public SubscriptionListenEvent(FixSessionSubscriber subscriber) {
        super(subscriber);
        this.subscriber = subscriber;
    }

    public String getSessionID() {
        return subscriber
                .getProperties()
                .getSessionID();
    }
}

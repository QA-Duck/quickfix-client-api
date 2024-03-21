package fix.client.api.subscriptions.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class SubscriptionCloseEvent extends ApplicationEvent {
    @Getter
    private final String sessionID;
    @Getter
    private final String subscriberID;

    public SubscriptionCloseEvent(String subscriberID, String sessionID) {
        super(subscriberID);
        this.subscriberID = subscriberID;
        this.sessionID = sessionID;
    }
}

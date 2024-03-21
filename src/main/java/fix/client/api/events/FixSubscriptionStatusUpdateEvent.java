package fix.client.api.events;

import fix.client.api.enums.FixSubscriberStatus;
import org.springframework.context.ApplicationEvent;

public class FixSubscriptionStatusUpdateEvent extends ApplicationEvent {
    private final String sessionID;
    private final String subscriptionID;
    private final FixSubscriberStatus status;

    public FixSubscriptionStatusUpdateEvent(
            String sessionID,
            String subscriptionID,
            FixSubscriberStatus status
    ) {
        super(status);
        this.subscriptionID = subscriptionID;
        this.status = status;
        this.sessionID = sessionID;
    }
}

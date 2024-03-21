package fix.client.api.subscriptions.models;

import fix.client.api.common.enums.FixSubscriberStatus;
import fix.client.api.sessions.models.EntityWithID;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FixSessionSubscriberProperties implements EntityWithID<String> {
    private String sessionID;
    private String subscriptionID;
    private FixSubscriberStatus status;

    @Override
    public String getID() {
        return subscriptionID;
    }

    @Override
    public void setID(String id) {
        setSubscriptionID(id);
    }
}

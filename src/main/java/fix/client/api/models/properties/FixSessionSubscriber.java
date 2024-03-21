package fix.client.api.models.properties;

import fix.client.api.enums.FixSubscriberStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FixSessionSubscriber implements EntityWithID<String> {
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

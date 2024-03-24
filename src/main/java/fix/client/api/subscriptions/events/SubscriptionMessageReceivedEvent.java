package fix.client.api.subscriptions.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import quickfix.Message;

@Getter
public class SubscriptionMessageReceivedEvent extends ApplicationEvent {

    private final String sessionID;
    private final Message message;

    public SubscriptionMessageReceivedEvent(Message message, String sessionID) {
        super(message);
        this.message = message;
        this.sessionID = sessionID;
    }
}

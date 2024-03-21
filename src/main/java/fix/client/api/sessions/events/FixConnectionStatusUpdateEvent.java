package fix.client.api.sessions.events;

import fix.client.api.common.enums.FixConnectionStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FixConnectionStatusUpdateEvent extends ApplicationEvent {
    private final String sessionID;
    private final FixConnectionStatus status;

    public FixConnectionStatusUpdateEvent(
            String sessionID,
            FixConnectionStatus status
    ) {
        super(status);
        this.status = status;
        this.sessionID = sessionID;
    }
}

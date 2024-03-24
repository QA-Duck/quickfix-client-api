package fix.client.api.sessions.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FixConnectionErrorEvent extends ApplicationEvent {

    private final String sessionID;
    private final Exception exception;

    public FixConnectionErrorEvent(
            Exception exception,
            String sessionID
    ) {
        super(exception);
        this.exception = exception;
        this.sessionID = sessionID;
    }
}

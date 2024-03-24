package fix.client.api.sessions.events;

import org.springframework.context.ApplicationEvent;

public class FixConnectionSuccessEvent extends ApplicationEvent {

    String sessionID;

    public FixConnectionSuccessEvent(String sessionID) {
        super(sessionID);
        this.sessionID = sessionID;
    }
}

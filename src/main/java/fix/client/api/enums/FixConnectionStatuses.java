package fix.client.api.enums;

import fix.client.api.models.FixConnectionStatus;

public class FixConnectionStatuses {

    public final static FixConnectionStatus IN_PROGRESS = new FixConnectionStatus(
            "Trying to start initiator",
            SessionStartStatus.IN_PROGRESS
    );

    public final static FixConnectionStatus IS_LOGGED_ON = new FixConnectionStatus(
            "Is logged on",
            SessionStartStatus.LOGGED_ON
    );

    public final static FixConnectionStatus failed(String message) {
        return new FixConnectionStatus(message, SessionStartStatus.FAILED);
    }
}

package fix.client.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fix.client.api.enums.SessionStartStatus;
import lombok.Data;

@Data
public class FixSessionInfo {

    @JsonProperty("connection-uuid")
    private final String uuid;

    @JsonProperty("connection-name")
    private final String name;

    @JsonProperty("connection-status")
    private FixConnectionStatus connectionStatus = new FixConnectionStatus(
            "On hold",
            SessionStartStatus.BROKEN
    );
}

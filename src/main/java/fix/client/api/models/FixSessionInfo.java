package fix.client.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fix.client.api.enums.SessionStartStatus;
import lombok.Data;

import static fix.client.api.enums.SessionStartStatus.BROKEN;

@Data
public class FixSessionInfo {

    @JsonProperty("connection_uuid")
    private final String uuid;

    @JsonProperty("connection_name")
    private final String name;

    @JsonProperty("connection_status")
    private FixConnectionStatus connectionStatus =
            new FixConnectionStatus("On hold", BROKEN);
}

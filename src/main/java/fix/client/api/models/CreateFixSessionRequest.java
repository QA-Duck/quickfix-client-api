package fix.client.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import quickfix.SessionID;

public record CreateFixSessionRequest(
        @JsonProperty("name")
        String sessionName,
        @JsonProperty("sender")
        String senderCompID,
        @JsonProperty("target")
        String targetCompID,
        @JsonProperty("host")
        String host,
        @JsonProperty("port")
        int port
) { }

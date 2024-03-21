package fix.client.api.sessions.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FixSessionCreateRequest(
        @JsonProperty("name")
        String name,

        @JsonProperty("sender")
        String senderCompID,

        @JsonProperty("target")
        String targetCompID,

        @JsonProperty("host")
        String host,

        @JsonProperty("port")
        int port
) {

}

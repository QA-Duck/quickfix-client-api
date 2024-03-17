package fix.client.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import fix.client.api.enums.SessionStartStatus;
import lombok.Data;

public record FixConnectionStatus(
        @JsonProperty("message")
        String message,

        @JsonProperty("status")
        SessionStartStatus status
) { }

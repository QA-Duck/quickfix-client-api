package fix.client.api.models.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FixSessionProperties(
        @JsonProperty("description")
        FixSessionDescriptionProperties description,
        @JsonProperty("connection")
        FixSessionConnectionProperties connection
){}

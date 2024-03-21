package fix.client.api.models.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FixSessionDescriptionProperties (
    @JsonProperty("connection_name")
    String name
) {}

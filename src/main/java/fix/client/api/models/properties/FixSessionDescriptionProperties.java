package fix.client.api.models.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FixSessionDescriptionProperties (
    @JsonProperty("connection_uuid")
    String uuid,
    @JsonProperty("connection_name")
    String name
) {}

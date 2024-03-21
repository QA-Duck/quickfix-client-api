package fix.client.api.sessions.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FixSessionDescriptionProperties (
    @JsonProperty("connection_name")
    String name
) {}

package fix.client.api.sessions.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import fix.client.api.common.settings.FixSessionSettingsFactory;
import quickfix.SessionSettings;

public record FixSessionConnectionProperties(
        @JsonProperty("sender")
        String senderCompID,
        @JsonProperty("target")
        String targetCompID,
        @JsonProperty("host")
        String host,
        @JsonProperty("port")
        int port
) {
        public SessionSettings createSettings() {
                return new FixSessionSettingsFactory().create(this);
        }
}

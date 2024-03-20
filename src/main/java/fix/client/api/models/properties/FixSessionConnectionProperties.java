package fix.client.api.models.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import fix.client.api.settings.FixSessionSettingsFactory;
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

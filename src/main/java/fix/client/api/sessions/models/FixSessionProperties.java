package fix.client.api.sessions.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import fix.client.api.common.settings.FixSessionSettingsFactory;

import quickfix.SessionSettings;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class FixSessionProperties implements EntityWithID<String> {
        @Setter
        @JsonProperty("id")
        private String sessionID;

        @JsonProperty("name")
        private final String name;

        @JsonProperty("sender")
        private final String senderCompID;

        @JsonProperty("target")
        private final String targetCompID;

        @JsonProperty("host")
        private final String host;

        @JsonProperty("port")
        private final int port;

        @Override
        public String getID() {
                return sessionID;
        }

        @Override
        public void setID(String id) {
                sessionID = id;
        }

        public SessionSettings createSettings() {
                return new FixSessionSettingsFactory().create(this);
        }
}

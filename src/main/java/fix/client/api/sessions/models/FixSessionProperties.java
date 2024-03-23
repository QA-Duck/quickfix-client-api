package fix.client.api.sessions.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import fix.client.api.common.enums.FixConnectionStatus;
import fix.client.api.common.settings.FixSessionSettingsFactory;

import quickfix.SessionSettings;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import static fix.client.api.common.enums.FixConnectionStatus.CREATED;


@Getter
@RequiredArgsConstructor
public class FixSessionProperties implements EntityWithID<String> {
        @Setter
        @JsonProperty("id")
        private String sessionID;

        @JsonProperty("name")
        private final String name;

        @JsonProperty("connection")
        private final FixConnectionProperties connection;

        @Setter
        @JsonProperty("status")
        private FixConnectionStatus status = CREATED;

        @Override
        public String getID() {
                return sessionID;
        }

        @Override
        public void setID(String id) {
                sessionID = id;
        }

        public SessionSettings createSettings() {
                return new FixSessionSettingsFactory().create(this.getConnection());
        }
}

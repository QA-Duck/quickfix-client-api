package fix.client.api.models.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class FixSessionProperties implements EntityWithID<String> {
        @Setter
        @JsonProperty("session_id")
        private String sessionID;
        @JsonProperty("description")
        private final FixSessionDescriptionProperties description;
        @JsonProperty("connection")
        private final FixSessionConnectionProperties connection;

        @Override
        public String getID() {
                return sessionID;
        }

        @Override
        public void setID(String id) {
                sessionID = id;
        }
}

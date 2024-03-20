package fix.client.api.services;

import fix.client.api.impl.FixMessageStreamApplication;
import fix.client.api.impl.FixMessageStreamInitiator;
import fix.client.api.models.properties.FixSessionConnectionProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FixConnectionService {

    private final Map<String, FixMessageStreamInitiator> connections = new HashMap<>();

    public void create(
            String sessionID,
            FixSessionConnectionProperties connectionProperties
    ) {
        var settings = connectionProperties.createSettings();
        var application = new FixMessageStreamApplication();
        var initiator = new FixMessageStreamInitiator(settings, application);
        connections.put(sessionID, initiator);
    }

    public void delete(String sessionID) {
        disconnect(sessionID);
        connections.remove(sessionID);
    }

    public void disconnect(String sessionID) {
        connections.get(sessionID).disconnect();
    }

    public void connect(String sessionID) {
        connections.get(sessionID).connect();
    }
}

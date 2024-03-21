package fix.client.api.services;

import fix.client.api.impl.FixMessageStreamApplication;
import fix.client.api.impl.FixMessageStreamInitiator;
import fix.client.api.models.properties.FixSessionConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
        connections.put(
                sessionID,
                new FixMessageStreamInitiator(
                        connectionProperties.createSettings(),
                        new FixMessageStreamApplication()
                )
        );
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

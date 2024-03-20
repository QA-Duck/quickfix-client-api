package fix.client.api.services;

import fix.client.api.impl.FixMessageStreamInitiator;
import fix.client.api.models.properties.FixSessionDescriptionProperties;
import fix.client.api.settings.FixSessionSettingsFactory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.util.*;

@Service
public class FixSessionDescriptionService {

    private final Map<String, FixSessionDescriptionProperties> sessions = new HashMap<>();

    public FixSessionDescriptionProperties createSession(CreateFixSessionRequest createFixSessionRequest) {
        var id = UUID.randomUUID().toString();
        var sessionName = createFixSessionRequest.sessionName();
        var fixSessionInfo = new FixSessionDescriptionProperties(id, sessionName);
        sessions.put(id, fixSessionInfo);
        createInitiator(createFixSessionRequest, fixSessionInfo);
        return fixSessionInfo;
    }

    private void createInitiator(
            CreateFixSessionRequest createFixSessionRequest,
            FixSessionDescriptionProperties fixSessionDescriptionProperties
    ) {
        var sessionSettings = new FixSessionSettingsFactory()
                .create(createFixSessionRequest);
        var initiator = new FixMessageStreamInitiator(fixSessionDescriptionProperties, sessionSettings);
        fixInitiatorMap.put(fixSessionDescriptionProperties.getUuid(), initiator);
    }

    public Collection<FixSessionDescriptionProperties> selectSession() {
        return sessions.values();
    }

    public FixSessionDescriptionProperties selectSession(String connectionID) {
        return sessions.get(connectionID);
    }

    public FixSessionDescriptionProperties connectSession(String connectionID) {
        return fixInitiatorMap.get(connectionID).connect();
    }

    public FixSessionDescriptionProperties disconnectSession(String connectionID) {
        return fixInitiatorMap.get(connectionID).disconnect();
    }

    public String subscribe(String connectionID, FluxSink<ServerSentEvent<String>> sink) {
        return fixInitiatorMap.get(connectionID).subscribe(sink);
    }

    public void unsubscribe(String connectionID, String subscriptionUUID) {
        fixInitiatorMap.get(connectionID).unsubscribe(subscriptionUUID);
    }
}

package fix.client.api.sessions.store;

import fix.client.api.common.impl.FixMessageStreamApplication;
import fix.client.api.common.impl.FixMessageStreamInitiator;
import fix.client.api.repositories.FixSessionMapRepository;
import fix.client.api.sessions.models.FixSessionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FixConnectionStore {
    private final FixSessionMapRepository fixSessionMapRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<String, FixMessageStreamInitiator> connections = new HashMap<>();

    @Autowired
    public FixConnectionStore(
            FixSessionMapRepository fixSessionMapRepository,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.fixSessionMapRepository = fixSessionMapRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public FixSessionProperties create(FixSessionProperties connectionProperties) {
        var property = fixSessionMapRepository.create(connectionProperties);
        var application = new FixMessageStreamApplication(
                property.getSessionID(),
                applicationEventPublisher
        );
        var connection = new FixMessageStreamInitiator(
                connectionProperties.getSessionID(),
                connectionProperties.createSettings(),
                application
        );
        connections.put(property.getID(), connection);
        return property;
    }

    public FixMessageStreamInitiator select(String sessionID) {
        return connections.get(sessionID);
    }
}

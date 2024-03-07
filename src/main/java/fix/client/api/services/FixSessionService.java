package fix.client.api.services;

import fix.client.api.impl.FixInitiator;
import fix.client.api.models.CreateInitiatorRequest;
import fix.client.api.settings.FixSessionSettingsFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FixSessionService {

    Map<String, FixInitiator> sessions = new HashMap<>();

    public String createSession(CreateInitiatorRequest createInitiatorRequest) {
        var sessionUUID = UUID.randomUUID().toString();
        var sessionFactory = new FixSessionSettingsFactory();
        var sessionSettings = sessionFactory.createInitiatorSettings(createInitiatorRequest);
        FixInitiator initiator = new FixInitiator(sessionSettings);
        sessions.put(sessionUUID, initiator);
        initiator.start();
        return sessionUUID;
    }
}

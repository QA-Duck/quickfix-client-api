package fix.client.api.services;

import fix.client.api.impl.FixInitiator;
import fix.client.api.models.CreateFixSessionRequest;
import fix.client.api.models.FixSessionInfo;
import fix.client.api.settings.FixSessionSettingsFactory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.*;
import java.util.function.Supplier;

@Service
public class FixSessionService {

    private final Map<String, FixSessionInfo> sessions = new HashMap<>();
    private final Map<String, FixInitiator> fixInitiatorMap = new HashMap<>();

    public FixSessionInfo createSession(CreateFixSessionRequest createFixSessionRequest) {
        var id = UUID.randomUUID().toString();
        var sessionName = createFixSessionRequest.sessionName();
        var fixSessionInfo = new FixSessionInfo(id, sessionName);
        sessions.put(id, fixSessionInfo);
        createInitiator(createFixSessionRequest, fixSessionInfo);
        return fixSessionInfo;
    }

    private void createInitiator(
            CreateFixSessionRequest createFixSessionRequest,
            FixSessionInfo fixSessionInfo
    ) {
        var sessionSettings = new FixSessionSettingsFactory().createInitiatorSettings(createFixSessionRequest);
        var initiator = new FixInitiator(fixSessionInfo, sessionSettings);
        fixInitiatorMap.put(fixSessionInfo.getUuid(), initiator);
    }

    public Map<String, FixSessionInfo> selectSession() {
        return sessions;
    }

    public FixSessionInfo selectSession(String connectionID) {
        return sessions.get(connectionID);
    }

    public FixSessionInfo connectSession(String connectionID) {
        return fixInitiatorMap.get(connectionID).connect();
    }

    public FixSessionInfo disconnectSession(String connectionID) {
        return fixInitiatorMap.get(connectionID).disconnect();
    }

    public void subscribe(String connectionID,  FluxSink<ServerSentEvent<String>> sink) {
        fixInitiatorMap.get(connectionID).subscribe(sink);
    }
}

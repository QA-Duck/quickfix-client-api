package fix.client.api.repositories;

import fix.client.api.common.enums.FixConnectionStatus;
import fix.client.api.common.enums.FixSubscriberStatus;
import fix.client.api.subscriptions.models.FixSessionSubscriberProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FixSubscriberMapRepository extends BaseMapRepository<FixSessionSubscriberProperties> {
    public FixSessionSubscriberProperties updateStatus(
            FixSubscriberStatus status,
            String sessionID
    ) {
        var entity = select(sessionID);
        entity.setStatus(status);
        update(entity);
        return entity;
    }

    public List<FixSessionSubscriberProperties> selectBySessionID(String sessionID) {
        return select()
                .stream()
                .filter(s -> s.getSessionID().equals(sessionID))
                .collect(Collectors.toList());
    }
}

package fix.client.api.repositories;

import fix.client.api.common.enums.FixConnectionStatus;
import fix.client.api.sessions.models.FixSessionProperties;
import fix.client.api.repositories.interfaces.IFixSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class FixSessionMapRepository extends BaseMapRepository<FixSessionProperties> implements IFixSessionRepository {
    public void updateStatus(
            FixConnectionStatus status,
            String sessionID
    ) {
        var entity = select(sessionID);
        entity.setStatus(status);
        update(entity);
    }
}

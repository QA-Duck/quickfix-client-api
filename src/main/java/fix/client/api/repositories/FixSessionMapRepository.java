package fix.client.api.repositories;

import fix.client.api.sessions.models.FixSessionProperties;
import fix.client.api.repositories.interfaces.IFixSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class FixSessionMapRepository extends BaseMapRepository<FixSessionProperties> implements IFixSessionRepository {
}

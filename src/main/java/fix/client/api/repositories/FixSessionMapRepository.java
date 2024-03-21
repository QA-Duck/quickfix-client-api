package fix.client.api.repositories;

import fix.client.api.models.properties.FixSessionProperties;
import fix.client.api.services.interfaces.IFixSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class FixSessionMapRepository extends BaseMapRepository<FixSessionProperties> implements IFixSessionRepository {
}

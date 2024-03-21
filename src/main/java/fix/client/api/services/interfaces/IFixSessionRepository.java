package fix.client.api.services.interfaces;

import fix.client.api.models.properties.FixSessionProperties;

import java.util.Collection;

public interface IFixSessionRepository {
    FixSessionProperties create(FixSessionProperties properties);

    FixSessionProperties select(String sessionID);

    Collection<FixSessionProperties> select();

    void delete(String sessionID);
}

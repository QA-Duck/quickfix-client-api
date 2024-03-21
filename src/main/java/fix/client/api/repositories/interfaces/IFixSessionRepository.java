package fix.client.api.repositories.interfaces;

import fix.client.api.sessions.models.FixSessionProperties;

import java.util.Collection;

public interface IFixSessionRepository {
    FixSessionProperties create(FixSessionProperties properties);

    FixSessionProperties select(String sessionID);

    Collection<FixSessionProperties> select();

    void delete(String sessionID);
}

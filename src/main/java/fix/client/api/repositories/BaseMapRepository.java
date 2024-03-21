package fix.client.api.repositories;


import fix.client.api.sessions.models.EntityWithID;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public abstract class BaseMapRepository<T extends EntityWithID<String>> {
    private final HashMap<String, T> storage = new HashMap<>();

    public final T create(T properties) {
        String uuid = UUID.randomUUID().toString();
        properties.setID(uuid);
        storage.put(uuid, properties);
        return properties;
    }

    public T select(String ID) {
        return storage.get(ID);
    }

    public Collection<T> select() {
        return storage.values();
    }

    public void delete(String sessionID) {

    }

    public void update(T update) {
        storage.put(update.getID(), update);
    }
}

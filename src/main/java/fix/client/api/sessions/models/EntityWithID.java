package fix.client.api.sessions.models;

public interface EntityWithID<T> {
    T getID();
    void setID(T id);
}

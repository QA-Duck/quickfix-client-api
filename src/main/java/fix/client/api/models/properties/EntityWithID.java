package fix.client.api.models.properties;

public interface EntityWithID<T> {
    T getID();
    void setID(T id);
}

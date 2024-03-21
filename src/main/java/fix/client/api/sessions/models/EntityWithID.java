package fix.client.api.sessions.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface EntityWithID<T> {
    @JsonIgnore
    T getID();
    void setID(T id);
}

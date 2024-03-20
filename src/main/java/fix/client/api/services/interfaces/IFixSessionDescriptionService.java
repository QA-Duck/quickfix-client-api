package fix.client.api.services.interfaces;

import fix.client.api.models.properties.FixSessionDescriptionProperties;

public interface IFixSessionDescriptionService {


    FixSessionDescriptionProperties create();
    FixSessionDescriptionProperties select();
}

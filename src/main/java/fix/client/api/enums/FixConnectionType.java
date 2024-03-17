package fix.client.api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FixConnectionType {
    INITIATOR("initiator"),
    ACCEPTOR("acceptor");

    @Getter
    private final String value;
}

package fix.client.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FixConnectionType {
    INITIATOR("initiator"),
    ACCEPTOR("acceptor");

    @Getter
    private final String value;
}

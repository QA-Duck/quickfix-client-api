package fix.client.api.models;

public record CreateInitiatorRequest(
        String senderCompID,
        String targetCompID,
        String host,
        int port
) { }

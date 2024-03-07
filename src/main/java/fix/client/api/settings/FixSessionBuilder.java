package fix.client.api.settings;

public class FixSessionBuilder {

    private String beginString;
    private String senderCompID;
    private String targetCompID;

    public FixSessionBuilder setSenderCompID(String senderCompID) {
        this.senderCompID = senderCompID;
        return this;
    }

    public FixSessionBuilder setTargetCompID(String targetCompID) {
        this.targetCompID = targetCompID;
        return this;
    }

    public FixSessionBuilder setBeginString(String beginString) {
        this.beginString = beginString;
        return this;
    }
}

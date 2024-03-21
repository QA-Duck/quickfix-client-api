package fix.client.api.common.settings;

import fix.client.api.common.enums.FixConnectionType;
import fix.client.api.common.enums.FixSessionSettingsField;
import fix.client.api.sessions.models.FixSessionConnectionProperties;
import quickfix.SessionID;
import quickfix.SessionSettings;

public class FixSessionSettingsFactory {

    public SessionSettings create(FixSessionConnectionProperties connectionProperties) {
        var sessionID = new SessionID(
                "FIX.4.4",
                connectionProperties.senderCompID(),
                connectionProperties.targetCompID()
        );
        return setDefaultParameters(new FixSessionSettingsBuilder())
                .setConnectionType(FixConnectionType.INITIATOR)
                .setStringField(FixSessionSettingsField.HOST, connectionProperties.host())
                .setLongField(sessionID, FixSessionSettingsField.PORT, connectionProperties.port())
                .setStringField(sessionID, FixSessionSettingsField.SENDER_COMP_ID, sessionID.getSenderCompID())
                .setStringField(sessionID, FixSessionSettingsField.TARGET_COMP_ID, sessionID.getTargetCompID())
                .setStringField(sessionID, FixSessionSettingsField.BEGIN_STRING, sessionID.getBeginString())
                .build();
    }

    public FixSessionSettingsBuilder setDefaultParameters(FixSessionSettingsBuilder builder) {
        return builder
                .setStringField(FixSessionSettingsField.END_TIME, "00:00:00")
                .setStringField(FixSessionSettingsField.START_TIME, "00:00:00")
                .setStringField(FixSessionSettingsField.USE_DATA_DICTIONARY, "N")
                .setStringField(FixSessionSettingsField.CONTINUE_INIT_ON_ERROR, "N")
                .setLongField(FixSessionSettingsField.HEART_BT_INT, 3)
                .setLongField(FixSessionSettingsField.RECONNECT_INTERVAL, 10)
                .setStringField(FixSessionSettingsField.FILE_STORE_PATH, "log")
                .setStringField(FixSessionSettingsField.FILE_LOG_PATH, "log");
    }
}

package fix.client.api.common.settings;

import fix.client.api.common.enums.FixConnectionType;
import fix.client.api.common.enums.FixSessionSettingsField;
import quickfix.SessionID;
import quickfix.SessionSettings;

public class FixSessionSettingsBuilder {

    private final SessionSettings settings = new SessionSettings();

    public FixSessionSettingsBuilder setLongField(FixSessionSettingsField field, int value) {
        settings.setLong(field.getName(), value);
        return this;
    }

    public FixSessionSettingsBuilder setStringField(FixSessionSettingsField field, String value) {
        settings.setString(field.getName(), value);
        return this;
    }

    public FixSessionSettingsBuilder setLongField(SessionID sessionID, FixSessionSettingsField field, int value) {
        settings.setLong(sessionID, field.getName(), value);
        return this;
    }

    public FixSessionSettingsBuilder setStringField(SessionID sessionID, FixSessionSettingsField field, String value) {
        settings.setString(sessionID, field.getName(), value);
        return this;
    }

    public FixSessionSettingsBuilder setConnectionType(FixConnectionType connectionType) {
        setStringField(FixSessionSettingsField.CONNECTION_TYPE, connectionType.getValue());
        return this;
    }

    public SessionSettings build() {
        return settings;
    }
}

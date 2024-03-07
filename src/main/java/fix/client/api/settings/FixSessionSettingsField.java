package fix.client.api.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FixSessionSettingsField {

    HOST("SocketConnectHost"),
    PORT("SocketConnectPort"),
    BEGIN_STRING("BeginString"),
    SENDER_COMP_ID("SenderCompID"),
    TARGET_COMP_ID("TargetCompID"),


    CONNECTION_TYPE("ConnectionType"),
    START_TIME("StartTime"),
    END_TIME("EndTime"),
    USE_DATA_DICTIONARY("UseDataDictionary"),
    CONTINUE_INIT_ON_ERROR("ContinueInitializationOnError"),
    HEART_BT_INT("HeartBtInt"),
    RECONNECT_INTERVAL("ReconnectInterval"),
    FILE_STORE_PATH("FileStorePath"),
    FILE_LOG_PATH("FileLogPath");

    @Getter
    private final String name;
}

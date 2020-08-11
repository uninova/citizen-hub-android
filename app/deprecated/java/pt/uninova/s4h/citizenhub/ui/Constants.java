package pt.uninova.s4h.citizenhub.ui;

import java.util.UUID;

public class Constants {
    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public static final UUID UUID_BUTTON_TOUCH = UUID.fromString("00000010-0000-3512-2118-0009af100700");
    public static final UUID UUID_START_HEARTRATE_CONTROL_POINT = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_NOTIFICATION_HEARTRATE = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    public static final byte[] BYTE_LAST_HEART_RATE_SCAN = {21, 1, 1};
    public static final byte[] BYTE_NEW_HEART_RATE_SCAN = {21, 2, 1};
    public static final UUID UUID_DESCRIPTOR_UPDATE_NOTIFICATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_SERVICE_HEARTBEAT = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public static final String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    static final int STATE_DISCONNECTED = 0;
    static final int STATE_CONNECTING = 1;
    static final int STATE_CONNECTED = 2;
    private static final String BASE_UUID = "0000%s-0000-1000-8000-00805f9b34fb";
    public static final UUID UUID_SERVICE_MIBAND_SERVICE = UUID.fromString(String.format(BASE_UUID, "FEE0"));
    public static final UUID UUID_SERVICE_MIBAND2_SERVICE = UUID.fromString(String.format(BASE_UUID, "FEE1"));
}

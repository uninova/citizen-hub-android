package pt.uninova;

import java.util.HashMap;
import java.util.Map;

public enum BluetoothConnectionState {
    DISCONNECTED(0),
    CONNECTING(1),
    CONNECTED(2),
    DISCONNECTING(3);
    private static final Map<Integer, BluetoothConnectionState> LOOKUP = new HashMap<>(BluetoothConnectionState.values().length);

    static {
        for (BluetoothConnectionState i : BluetoothConnectionState.values()) {
            LOOKUP.put(i.getStateId(), i);
        }
    }

    private final int id;

    BluetoothConnectionState(int value) {
        this.id = value;
    }

    public static BluetoothConnectionState find(int id) {
        return LOOKUP.get(id);
    }

    public int getStateId() {
        return id;
    }
}
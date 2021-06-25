package pt.uninova.s4h.citizenhub.persistence;

import java.util.HashMap;
import java.util.Map;

public enum ConnectionKind {
    UNKNOWN(0),
    BLUETOOTH(1),
    WEAROS(2);

    private static final Map<Integer, ConnectionKind> LOOKUP = new HashMap<>(ConnectionKind.values().length);

    static {
        for (ConnectionKind i : ConnectionKind.values()) {
            LOOKUP.put(i.getId(), i);
        }
    }

    private final int id;

    ConnectionKind(int value) {
        this.id = value;
    }

    public static ConnectionKind find(int id) {
        return LOOKUP.get(id);
    }

    public int getId() {
        return id;
    }

}

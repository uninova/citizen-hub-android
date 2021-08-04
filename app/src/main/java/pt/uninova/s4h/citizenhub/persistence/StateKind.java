package pt.uninova.s4h.citizenhub.persistence;

import java.util.HashMap;
import java.util.Map;

public enum StateKind {
    DISABLED(0),
    INACTIVE(1),
    ACTIVE(2);

    private static final Map<Integer, pt.uninova.s4h.citizenhub.persistence.StateKind> LOOKUP = new HashMap<>(pt.uninova.s4h.citizenhub.persistence.StateKind.values().length);

    static {
        for (pt.uninova.s4h.citizenhub.persistence.StateKind i : pt.uninova.s4h.citizenhub.persistence.StateKind.values()) {
            LOOKUP.put(i.getId(), i);
        }
    }

    private final int id;

    StateKind(int value) {
        this.id = value;
    }

    public static pt.uninova.s4h.citizenhub.persistence.StateKind find(int id) {
        return LOOKUP.get(id);
    }

    public int getId() {
        return id;
    }

}

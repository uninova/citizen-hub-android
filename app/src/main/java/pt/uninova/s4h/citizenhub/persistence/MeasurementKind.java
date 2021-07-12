package pt.uninova.s4h.citizenhub.persistence;

import java.util.HashMap;
import java.util.Map;

public enum MeasurementKind {
    UNKNOWN(0),
    HEART_RATE(1),
    POSTURE(2),
    RESPIRATION_RATE(3),
    ACTIVITY(4),
    ;

    private static final Map<Integer, MeasurementKind> LOOKUP = new HashMap<>(MeasurementKind.values().length);

    static {
        for (MeasurementKind i : MeasurementKind.values()) {
            LOOKUP.put(i.getId(), i);
        }
    }

    private final int id;

    MeasurementKind(int value) {
        this.id = value;
    }

    public static MeasurementKind find(int id) {
        return LOOKUP.get(id);
    }

    public int getId() {
        return id;
    }

}

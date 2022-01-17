package pt.uninova.s4h.citizenhub.persistence;

import java.util.HashMap;
import java.util.Map;

public enum MeasurementKind {
    UNKNOWN(0),
    HEART_RATE(1),
    STEPS(2),
    DISTANCE(3),
    CALORIES(4),
    GOOD_POSTURE(5),
    BAD_POSTURE(6),
    RESPIRATION_RATE(7),
    INSPIRATION(8),
    EXPIRATION(9),
    STEPS_PER_MINUTE(10),
    ACTIVITY(11),
    CADENCE(12),
    SITTING(13),
    STANDING(14),
    POSTURE(15),
    LUMBAR_EXTENSION_TRAINING(16),
    BLOOD_PRESSURE_SBP(17),
    BLOOD_PRESSURE_DBP(18),
    BLOOD_PRESSURE_MEAN_AP(19);

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

package pt.uninova.s4h.citizenhub.data;

public abstract class Measurement<T> {

    public static final int UNKNOWN = 0;
    public static final int HEART_RATE = 1;
    public static final int STEPS = 2;
    public static final int DISTANCE = 3;
    public static final int CALORIES = 4;
    public static final int GOOD_POSTURE = 5;
    public static final int BAD_POSTURE = 6;
    public static final int RESPIRATION_RATE = 7;
    public static final int INSPIRATION = 8;
    public static final int EXPIRATION = 9;
    public static final int STEPS_PER_MINUTE = 10;
    public static final int ACTIVITY = 11;
    public static final int CADENCE = 12;
    public static final int SITTING = 13;
    public static final int STANDING = 14;
    public static final int POSTURE = 15;
    public static final int LUMBAR_EXTENSION_TRAINING = 16;
    public static final int BLOOD_PRESSURE = 17;
    public static final int BLOOD_PRESSURE_SBP = 18;
    public static final int BLOOD_PRESSURE_DBP = 19;
    public static final int BLOOD_PRESSURE_MEAN_AP = 20;
    public static final int ENERGY_EXPENDED = 21;
    public static final int RR_INTERVAL = 22;

    private final int type;
    private final T value;

    protected Measurement(int type, T value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

}

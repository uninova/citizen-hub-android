package pt.uninova.s4h.citizenhub.data;

public abstract class Measurement<T> {

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_HEART_RATE = 1;
    public static final int TYPE_STEPS_SNAPSHOT = 2;
    public static final int TYPE_DISTANCE_SNAPSHOT = 3;
    public static final int TYPE_CALORIES_SNAPSHOT = 4;
    public static final int TYPE_BREATHING_RATE = 7;
    public static final int TYPE_BREATHING = 8;
    public static final int TYPE_ACTIVITY = 11;
    public static final int TYPE_CADENCE = 12;
    public static final int TYPE_LUMBAR_EXTENSION_TRAINING = 16;
    public static final int TYPE_BLOOD_PRESSURE = 17;
    public static final int TYPE_ENERGY_EXPENDED = 21;
    public static final int TYPE_RR_INTERVAL = 22;
    public static final int TYPE_PULSE_RATE = 23;
    public static final int TYPE_POSTURE = 29;
    public static final int TYPE_CALORIES = 30;
    public static final int TYPE_POSITION = 32;
    public static final int TYPE_MOTION = 33;

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

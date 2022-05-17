package pt.uninova.s4h.citizenhub.data;

public abstract class Measurement<T> {

    public static final int UNKNOWN = 0;
    public static final int HEART_RATE = 1;
    public static final int STEPS = 2;
    public static final int DISTANCE = 3;
    public static final int CALORIES = 4;
    public static final int POSTURE_CORRECT = 5;
    public static final int POSTURE_INCORRECT = 6;
    public static final int RESPIRATION_RATE = 7;
    public static final int INSPIRATION = 8;
    public static final int EXPIRATION = 9;
    public static final int ACTIVITY = 11;
    public static final int CADENCE = 12;
    public static final int LUMBAR_EXTENSION_TRAINING = 16;
    public static final int BLOOD_PRESSURE = 17;
    public static final int ENERGY_EXPENDED = 21;
    public static final int RR_INTERVAL = 22;
    public static final int PULSE_RATE = 23;
    public static final int MOTION_STOPPED = 24;
    public static final int MOTION_WALKING = 25;
    public static final int MOTION_RUNNING = 26;
    public static final int POSITION_SITTING = 27;
    public static final int POSITION_STANDING = 28;

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

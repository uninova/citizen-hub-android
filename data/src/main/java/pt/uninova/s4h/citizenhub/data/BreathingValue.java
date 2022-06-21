package pt.uninova.s4h.citizenhub.data;

public class BreathingValue {

    public static final int TYPE_INSPIRATION = 0;
    public static final int TYPE_EXPIRATION = 1;

    private final int type;
    private final double value;

    public BreathingValue(int type, double value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public double getValue() {
        return value;
    }
}

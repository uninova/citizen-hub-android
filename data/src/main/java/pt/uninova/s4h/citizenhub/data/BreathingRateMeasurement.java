package pt.uninova.s4h.citizenhub.data;

public class BreathingRateMeasurement extends Measurement<Integer> {

    public BreathingRateMeasurement(Integer value) {
        super(TYPE_BREATHING_RATE, value);
    }
}
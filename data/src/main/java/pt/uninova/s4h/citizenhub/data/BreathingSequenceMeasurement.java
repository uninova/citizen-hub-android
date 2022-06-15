package pt.uninova.s4h.citizenhub.data;

public class BreathingSequenceMeasurement extends Measurement<BreathingValue[]> {

    public BreathingSequenceMeasurement(BreathingValue[] value) {
        super(TYPE_BREATHING, value);
    }
}

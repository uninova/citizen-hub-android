package pt.uninova.s4h.citizenhub.data;

public class StepsMeasurement extends Measurement<Integer> {

    public StepsMeasurement(Integer value) {
        super(TYPE_STEPS_SNAPSHOT, value);
    }
}

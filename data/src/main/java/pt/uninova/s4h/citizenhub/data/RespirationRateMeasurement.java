package pt.uninova.s4h.citizenhub.data;

public class RespirationRateMeasurement extends Measurement<Integer> {

    public RespirationRateMeasurement(Integer value) {
        super(TYPE_RESPIRATION_RATE, value);
    }
}

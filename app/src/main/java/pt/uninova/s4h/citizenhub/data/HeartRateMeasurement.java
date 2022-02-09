package pt.uninova.s4h.citizenhub.data;

public class HeartRateMeasurement extends Measurement<Integer> {

    public HeartRateMeasurement(Integer value) {
        super(Measurement.HEART_RATE, value);
    }
}
package pt.uninova.s4h.citizenhub.data;

public class HeartRateMeasurement extends Measurement<Integer> {

    public HeartRateMeasurement(Integer value) {
        super(Measurement.TYPE_HEART_RATE, value);
    }
}
package pt.uninova.s4h.citizenhub.data;

public class RrIntervalMeasurement extends Measurement<int[]> {

    public RrIntervalMeasurement(int[] value) {
        super(Measurement.TYPE_RR_INTERVAL, value);
    }
}

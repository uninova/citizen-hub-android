package pt.uninova.s4h.citizenhub.data;

public class RrIntervalMeasurement extends Measurement<int[]> {

    public RrIntervalMeasurement(int[] value) {
        super(Measurement.RR_INTERVAL, value);
    }
}

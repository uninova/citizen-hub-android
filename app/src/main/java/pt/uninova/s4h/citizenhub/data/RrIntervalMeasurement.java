package pt.uninova.s4h.citizenhub.data;

public class RrIntervalMeasurement extends Measurement<Integer[]> {

    public RrIntervalMeasurement(Integer[] value) {
        super(Measurement.RR_INTERVAL, value);
    }
}

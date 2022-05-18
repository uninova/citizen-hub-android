package pt.uninova.s4h.citizenhub.data;

public class PulseRateMeasurement extends Measurement<Double> {

    public PulseRateMeasurement(double value) {
        super(TYPE_PULSE_RATE, value);
    }
}

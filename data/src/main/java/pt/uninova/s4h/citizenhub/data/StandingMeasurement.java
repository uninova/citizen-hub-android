package pt.uninova.s4h.citizenhub.data;

public class StandingMeasurement extends Measurement<Double> {

    public StandingMeasurement(Double value) {
        super(TYPE_POSITION_STANDING, value);
    }
}

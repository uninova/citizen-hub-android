package pt.uninova.s4h.citizenhub.data;

public class EnergyExpendedMeasurement extends Measurement<Integer> {

    public EnergyExpendedMeasurement(Integer value) {
        super(Measurement.TYPE_ENERGY_EXPENDED, value);
    }
}
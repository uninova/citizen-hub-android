package pt.uninova.s4h.citizenhub.data;

public class BloodPressureMeasurement extends Measurement<BloodPressureValue> {

    public BloodPressureMeasurement(BloodPressureValue value) {
        super(TYPE_BLOOD_PRESSURE, value);
    }
}

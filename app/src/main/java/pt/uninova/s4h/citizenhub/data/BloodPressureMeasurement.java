package pt.uninova.s4h.citizenhub.data;

public class BloodPressureMeasurement extends Measurement<BloodPressureValue> {

    public BloodPressureMeasurement(BloodPressureValue value) {
        super(BLOOD_PRESSURE, value);
    }
}

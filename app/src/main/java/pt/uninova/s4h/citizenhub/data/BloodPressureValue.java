package pt.uninova.s4h.citizenhub.data;

public class BloodPressureValue {

    private final double systolic;
    private final double diastolic;
    private final double meanArterialPressure;

    public BloodPressureValue(double systolic, double diastolic, double meanArterialPressure) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.meanArterialPressure = meanArterialPressure;
    }

    public double getDiastolic() {
        return diastolic;
    }

    public double getMeanArterialPressure() {
        return meanArterialPressure;
    }

    public double getSystolic() {
        return systolic;
    }
}

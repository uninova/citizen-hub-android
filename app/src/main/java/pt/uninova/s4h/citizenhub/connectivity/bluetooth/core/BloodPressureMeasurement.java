package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import pt.uninova.s4h.citizenhub.data.BloodPressureValue;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.PulseRateMeasurement;

public class BloodPressureMeasurement {

    private static class Flags {

        private final byte flags;

        public Flags(byte flags) {
            this.flags = flags;
        }

        public boolean isBloodPressureUnitPascal() {
            return (flags & 0x1) != 0;
        }

        public boolean isTimeStampPresent() {
            return (flags & 0x2) != 0;
        }

        public boolean isPulseRatePresent() {
            return (flags & 0x4) != 0;
        }

        public boolean isUserIdPresent() {
            return (flags & 0x8) != 0;
        }

        public boolean isMeasurementStatusPresent() {
            return (flags & 0x10) != 0;
        }
    }

    private final Flags flags;
    private final Float16 systolic;
    private final Float16 diastolic;
    private final Float16 meanArterialPressure;
    private final DateTime timeStamp;
    private final Float16 pulseRate;
    private final UInt8 userId;
    private final MeasurementStatus measurementStatus;


    public BloodPressureMeasurement(Buffer reader) {
        flags = new Flags(reader.readByte());
        systolic = reader.readFloat16();
        diastolic = reader.readFloat16();
        meanArterialPressure = reader.readFloat16();
        timeStamp = flags.isTimeStampPresent() ? new DateTime(reader) : null;
        pulseRate = flags.isPulseRatePresent() ? reader.readFloat16() : null;
        userId = flags.isUserIdPresent() ? reader.readUInt8() : null;
        measurementStatus = flags.isMeasurementStatusPresent() ? new MeasurementStatus(reader) : null;
    }

    public BloodPressureMeasurement(byte[] buffer) {
        this(new Buffer(buffer));
    }
    
    public Float16 getDiastolic() {
        return diastolic;
    }

    public Float16 getMeanArterialPressure() {
        return meanArterialPressure;
    }

    public MeasurementStatus getMeasurementStatus() {
        return measurementStatus;
    }

    public Float16 getPulseRate() {
        return pulseRate;
    }

    public Float16 getSystolic() {
        return systolic;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public UInt8 getUserId() {
        return userId;
    }

    public Measurement<?>[] toMeasurements() {
        final boolean validBloodPressure = !systolic.equals(Float16.NAN) && !systolic.equals(Float16.NRES);
        final boolean validPressure = flags.isPulseRatePresent() && !pulseRate.equals(Float16.NAN) && !pulseRate.equals(Float16.NRES);

        final Measurement<?>[] measurements = new Measurement<?>[(validBloodPressure ? 1 : 0) + (validPressure ? 1 : 0)];

        if (validBloodPressure) {
            measurements[0] = new pt.uninova.s4h.citizenhub.data.BloodPressureMeasurement(new BloodPressureValue(systolic.toDouble(), diastolic.toDouble(), meanArterialPressure.toDouble()));
        }

        if (validBloodPressure) {
            measurements[1] = new PulseRateMeasurement(pulseRate.toDouble());
        }

        return measurements;
    }
}

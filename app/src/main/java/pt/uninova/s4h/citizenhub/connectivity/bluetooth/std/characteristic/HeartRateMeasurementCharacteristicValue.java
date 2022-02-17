package pt.uninova.s4h.citizenhub.connectivity.bluetooth.std.characteristic;

import pt.uninova.s4h.citizenhub.data.EnergyExpendedMeasurement;
import pt.uninova.s4h.citizenhub.data.HeartRateMeasurement;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.RrIntervalMeasurement;

public class HeartRateMeasurementCharacteristicValue {

    private final byte flags;
    private final int value;
    private final int energyExpended;
    private final int[] rrIntervals;

    public HeartRateMeasurementCharacteristicValue(byte[] value) {
        int offset = 0;
        flags = value[offset++];

        this.value = isHeartRateValueFormatUint16() ? Transcoding.uint16(value[offset++], value[offset++]) : Transcoding.uint8(value[offset++]);
        this.energyExpended = hasEnergyExpended() ? Transcoding.uint16(value[offset++], value[offset++]) : -1;

        rrIntervals = new int[value.length - offset];

        for (int i = 0; i < rrIntervals.length; i++) {
            rrIntervals[i] = Transcoding.uint16(value[offset++], value[offset++]);
        }
    }

    public int getEnergyExpended() {
        return energyExpended;
    }

    public int[] getRrIntervals() {
        return rrIntervals;
    }

    public int getValue() {
        return value;
    }

    public boolean hasEnergyExpended() {
        return (flags & 0x8) != 0;
    }

    public boolean hasRrIntervals() {
        return (flags & 0xF) != 0;
    }

    public boolean isHeartRateValueFormatUint16() {
        return (flags & 0x1) != 0;
    }

    public boolean isSensorContactDetected() {
        return (flags & 0x2) != 0;
    }

    public boolean isSensorContactSupported() {
        return (flags & 0x4) != 0;
    }

    public Measurement<?>[] toMeasurements() {
        Measurement<?>[] measurements = new Measurement<?>[1 + (hasEnergyExpended() ? 1 : 0) + (hasRrIntervals() ? 1 : 0)];

        int offset = 0;
        measurements[offset++] = new HeartRateMeasurement(value);

        if (hasEnergyExpended())
            measurements[offset++] = new EnergyExpendedMeasurement(energyExpended);

        if (hasRrIntervals())
            measurements[offset] = new RrIntervalMeasurement(rrIntervals);

        return measurements;
    }
}

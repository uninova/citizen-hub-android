package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import pt.uninova.s4h.citizenhub.data.EnergyExpendedMeasurement;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.RrIntervalMeasurement;

public class HeartRateMeasurement {

    public static final class Flags {

        private final byte b1;

        private Flags(byte b1) {
            this.b1 = b1;
        }

        public Class<?> getFormat() {
            return (b1 & 0x1) == 0 ? UInt8.class : UInt16.class;
        }

        public boolean isSensorContactDetected() {
            return (b1 & 0x2) != 0;
        }

        public boolean isEnergyExpendedPresent() {
            return (b1 & 0x4) != 0;
        }

        public boolean isRrIntervalsPresent() {
            return (b1 & 0x8) != 0;
        }
    }

    private final Flags flags;
    private final UInt16 value;
    private final UInt16 energyExpended;
    private final UInt16[] rrIntervals;

    public HeartRateMeasurement(Buffer buffer) {
        flags = new Flags(buffer.readByte());
        value = flags.getFormat() == UInt16.class ? buffer.readUInt16() : UInt16.of(buffer.readByte(), (byte) 0);
        energyExpended = flags.isEnergyExpendedPresent() ? buffer.readUInt16() : null;

        if (flags.isRrIntervalsPresent()) {
            rrIntervals = new UInt16[buffer.getBytesLeft() / 2];
            int offset = 0;

            while (offset < rrIntervals.length) {
                rrIntervals[offset++] = buffer.readUInt16();
            }
        } else {
            rrIntervals = null;
        }
    }

    public HeartRateMeasurement(byte[] bytes) {
        this(new Buffer(bytes));
    }

    public UInt16 getEnergyExpended() {
        return energyExpended;
    }

    public Flags getFlags() {
        return flags;
    }

    public UInt16[] getRrIntervals() {
        return rrIntervals;
    }

    public UInt16 getValue() {
        return value;
    }

    public Measurement<?>[] toMeasurements() {
        final Measurement<?>[] measurements = new Measurement<?>[1 + (flags.isEnergyExpendedPresent() ? 1 : 0) + (flags.isRrIntervalsPresent() ? rrIntervals.length : 0)];
        int offset = 0;

        measurements[offset++] = new pt.uninova.s4h.citizenhub.data.HeartRateMeasurement(value.toInt());

        if (flags.isEnergyExpendedPresent()) {
            measurements[offset++] = new EnergyExpendedMeasurement(energyExpended.toInt());
        }

        if (flags.isRrIntervalsPresent()) {
            final int[] rrs = new int[rrIntervals.length];
            int rrsOffset = 0;

            for (UInt16 i : rrIntervals) {
                rrs[rrsOffset++] = i.toInt();
            }

            measurements[offset] = new RrIntervalMeasurement(rrs);
        }

        return measurements;
    }
}
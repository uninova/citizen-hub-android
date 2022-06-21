package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.Buffer;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.UInt16;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.UInt8;
import pt.uninova.s4h.citizenhub.data.BreathingRateMeasurement;
import pt.uninova.s4h.citizenhub.data.BreathingSequenceMeasurement;
import pt.uninova.s4h.citizenhub.data.BreathingValue;
import pt.uninova.s4h.citizenhub.data.Measurement;

public class BreathingMeasurement {

    public static final class Flags {

        private final byte b1;

        private Flags(byte b1) {
            this.b1 = b1;
        }

        public Class<?> getFormat() {
            return (b1 & 0x1) == 0 ? UInt8.class : UInt16.class;
        }

        public boolean isBreathingPresent() {
            return (b1 & 0x2) != 0;
        }

        public boolean isInspirationFirst() {
            return (b1 & 0x4) != 0;
        }
    }

    private final Flags flags;
    private final UInt16 rate;
    private final UInt16[] breathing;

    public BreathingMeasurement(Buffer buffer) {
        flags = new Flags(buffer.readByte());
        rate = flags.getFormat() == UInt16.class ? buffer.readUInt16() : UInt16.of(buffer.readByte(), (byte) 0);

        if (flags.isBreathingPresent()) {
            breathing = new UInt16[buffer.getBytesLeft() / 2];

            for (int i = 0; i < breathing.length; i++) {
                breathing[i] = buffer.readUInt16();
            }
        } else {
            breathing = null;
        }
    }

    public BreathingMeasurement(byte[] bytes) {
        this(new Buffer(bytes));
    }

    public Measurement<?>[] toMeasurements() {
        final Measurement<?>[] measurements = new Measurement<?>[breathing != null ? 2 : 1];

        measurements[0] = new BreathingRateMeasurement(rate.toInt());

        if (breathing != null) {
            final BreathingValue[] breathingValues = new BreathingValue[breathing.length];

            boolean isInspiration = flags.isInspirationFirst();
            int breathingOffset = 0;

            for (UInt16 i : breathing) {
                breathingValues[breathingOffset++] = new BreathingValue(isInspiration ? BreathingValue.TYPE_INSPIRATION : BreathingValue.TYPE_EXPIRATION, i.toInt() / 32.0);

                isInspiration = !isInspiration;
            }

            measurements[1] = new BreathingSequenceMeasurement(breathingValues);
        }

        return measurements;
    }
}
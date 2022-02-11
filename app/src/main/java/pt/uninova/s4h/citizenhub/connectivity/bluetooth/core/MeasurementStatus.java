package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

public class MeasurementStatus {

    private final byte b1, b2;

    public MeasurementStatus(byte[] buffer) {
        this(buffer, 0);
    }

    public MeasurementStatus(byte[] buffer, int offset) {
        this(new ByteReader(buffer, offset));
    }

    public MeasurementStatus(ByteReader reader) {
        this(reader.readByte(), reader.readByte());
    }

    private MeasurementStatus(byte b1, byte b2) {
        this.b1 = b1;
        this.b2 = b2;
    }

    public boolean isBodyMovementDetected() {
        return (b2 & 0x1) != 0;
    }

    public boolean isCuffTooLoose() {
        return (b2 & 0x2) != 0;
    }

    public boolean isIrregularPulseDetected() {

        return (b2 & 0x4) != 0;
    }

    public boolean isPulseRateWithinRange() {
        return (b2 & 0x8) == 0 && (b2 & 0x10) == 0;
    }

    public boolean isPulseRateAboveUpperLimit() {
        return (b2 & 0x8) != 0 && (b2 & 0x10) == 0;
    }

    public boolean isPulseRateBelowLowerLimit() {
        return (b2 & 0x8) == 0 && (b2 & 0x10) != 0;
    }

    public boolean isImproperMeasurementPosition() {
        return (b2 & 0x20) != 0;
    }
}

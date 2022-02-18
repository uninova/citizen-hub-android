package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

public class AdjustReason implements ByteSerializable {

    public static AdjustReason of(boolean manualTimeUpdate, boolean externalReferenceTimeUpdate, boolean changeOfTimeZone, boolean changeOfDst) {
        return new AdjustReason((byte) (((manualTimeUpdate ? 0x1 : 0) | (externalReferenceTimeUpdate ? 0x2 : 0) | (changeOfTimeZone ? 0x4 : 0) | (changeOfDst ? 0x8 : 0)) & 0xff));
    }

    private final byte b1;

    private AdjustReason(byte b1) {
        this.b1 = b1;
    }

    public AdjustReason(Buffer reader) {
        this(reader.readByte());
    }

    public AdjustReason(byte[] bytes) {
        this(new Buffer(bytes));
    }

    public boolean isChangeOfDst() {
        return (b1 & 0x8) != 0;
    }

    public boolean isChangeOfTimeZone() {
        return (b1 & 0x4) != 0;
    }

    public boolean isExternalReferenceTimeUpdate() {
        return (b1 & 0x2) != 0;
    }

    public boolean isManualTimeUpdate() {
        return (b1 & 0x1) != 0;
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{b1};
    }

    @Override
    public void write(Buffer buffer) {
        buffer.write(b1);
    }
}

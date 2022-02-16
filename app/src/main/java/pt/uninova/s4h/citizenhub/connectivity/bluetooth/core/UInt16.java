package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

public class UInt16 implements ByteSerializable {

    private final byte b1, b2;

    public static UInt16 of(byte b1, byte b2) {
        return new UInt16(b1, b2);
    }

    public static UInt16 of(int val) {
        return new UInt16((byte) (val & 0xff), (byte) (val >>> 8 & 0xff));
    }

    private UInt16(byte b1, byte b2) {
        this.b1 = b1;
        this.b2 = b2;
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{b1, b2};
    }

    public int toInt() {
        return (b2 & 0xff) << 8 | b1 & 0xff;
    }

    @Override
    public void write(Buffer writer) {
        writer.write(b1, b2);
    }

}

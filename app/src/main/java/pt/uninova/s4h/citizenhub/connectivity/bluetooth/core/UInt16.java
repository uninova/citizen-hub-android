package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import java.util.Objects;

public class UInt16 implements ByteSerializable {

    public static UInt16 of(byte b1, byte b2) {
        return new UInt16(b1, b2);
    }

    public static UInt16 of(int val) {
        return new UInt16((byte) (val & 0xff), (byte) (val >>> 8 & 0xff));
    }

    private final byte b1, b2;

    private UInt16(byte b1, byte b2) {
        this.b1 = b1;
        this.b2 = b2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UInt16 uInt16 = (UInt16) o;
        return b1 == uInt16.b1 && b2 == uInt16.b2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(b1, b2);
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{b1, b2};
    }

    public int toInt() {
        return b1 | b2 << 8;
    }

    @Override
    public void write(Buffer writer) {
        writer.write(b1, b2);
    }

}

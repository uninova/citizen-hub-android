package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import java.util.Objects;

public class UInt8 implements ByteSerializable {

    private final byte b1;

    public static UInt8 of(byte b1) {
        return new UInt8(b1);
    }

    public static UInt8 of(int val) {
        return new UInt8((byte) (val & 0xff));
    }

    private UInt8(byte b1) {
        this.b1 = b1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UInt8 uInt8 = (UInt8) o;
        return b1 == uInt8.b1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(b1);
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{b1};
    }

    public int toInt() {
        return b1 & 0xff;
    }

    @Override
    public void write(Buffer writer) {
        writer.write(b1);
    }

}
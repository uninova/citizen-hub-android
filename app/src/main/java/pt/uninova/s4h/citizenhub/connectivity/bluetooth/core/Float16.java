package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import java.util.Objects;

public class Float16 implements ByteSerializable {

    public static final Float16 NAN = Float16.of(0x7FF);
    public static final Float16 NRES = Float16.of(0x800);
    public static final Float16 INFINITY_PLUS = Float16.of(0x7FE);
    public static final Float16 INFINITY_MINUS = Float16.of(0x802);

    public final byte b1, b2;

    public static Float16 of(byte b1, byte b2) {
        return new Float16(b1, b2);
    }

    public static Float16 of(int val) {
        return new Float16((byte) (val & 0xff), (byte) (val >>> 8 & 0xff));
    }

    private Float16(byte b1, byte b2) {
        this.b1 = b1;
        this.b2 = b2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Float16 float16 = (Float16) o;
        return b1 == float16.b1 && b2 == float16.b2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(b1, b2);
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{b1, b2};
    }

    public double toDouble() {
        return ((b2 & 0xf) << 8 | b1 & 0xff) * Math.pow(10, (b2 & 0xf0) >>> 4);
    }

    @Override
    public void write(Buffer writer) {
        writer.write(b1, b2);
    }

}

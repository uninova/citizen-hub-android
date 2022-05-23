package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import java.util.Objects;

public class Float32 implements ByteSerializable {

    public static final Float32 NAN = Float32.of(0x7FFFFF);
    public static final Float32 NRES = Float32.of(0x800000);
    public static final Float32 INFINITY_PLUS = Float32.of(0x7FFFFE);
    public static final Float32 INFINITY_MINUS = Float32.of(0x800002);

    private final byte b1, b2, b3, b4;

    public static Float32 of(byte b1, byte b2, byte b3, byte b4) {
        return new Float32(b1, b2, b3, b4);
    }

    public static Float32 of(int val) {
        return new Float32((byte) (val & 0xff), (byte) (val >>> 8 & 0xff), (byte) (val >>> 16 & 0xff), (byte) (val >>> 24 & 0xff));
    }

    private Float32(byte b1, byte b2, byte b3, byte b4) {
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
        this.b4 = b4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Float32 float32 = (Float32) o;
        return b1 == float32.b1 && b2 == float32.b2 && b3 == float32.b3 && b4 == float32.b4;
    }

    @Override
    public int hashCode() {
        return Objects.hash(b1, b2, b3, b4);
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{b1, b2, b3, b4};
    }

    public double toDouble() {
        return (((b2 & 0xff) << 16) | ((b3 & 0xff) << 8) | (b4 & 0xff)) * Math.pow(10, (b1 & 0xff));
    }

    @Override
    public void write(Buffer writer) {
        writer.write(b1, b2, b3, b4);
    }

}

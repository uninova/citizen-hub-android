package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

public class Float16 {

    public static final short NAN = 0x07FF;
    public static final short NRES = 0x0800;
    public static final short INFINITY_PLUS = 0x07FE;
    public static final short INFINITY_MINUS = 0x0802;

    private final int exponent;
    private final int mantissa;

    public static Float16 of(byte b1, byte b2) {
        return new Float16(b1, b2);
    }

    private Float16(byte b1, byte b2) {
        exponent = (b1 & 0xf0) >>> 4;
        mantissa = (b1 & 0x0f) << 8 | b2 & 0xff;
    }

    public boolean isFinite() {
        return exponent == 0 && (mantissa < INFINITY_PLUS || mantissa > INFINITY_MINUS);
    }

    public double toDouble() {
        return mantissa * Math.pow(10, exponent);
    }

}

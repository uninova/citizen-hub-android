package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import java.io.BufferedWriter;

public class UInt8 implements Bufferable, Byteable {

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
    public void buffer(ByteWriter writer) {
        writer.write(b1);
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{b1};
    }

    public int toInt() {
        return b1 & 0xff;
    }
}
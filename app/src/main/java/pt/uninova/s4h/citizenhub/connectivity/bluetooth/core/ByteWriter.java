package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

public class ByteWriter {

    private final byte[] buffer;
    private int offset;

    public ByteWriter(byte[] buffer) {
        this(buffer, 0);
    }

    public ByteWriter(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void write(byte... bytes) {
        for (byte i : bytes) {
            buffer[offset++] = i;
        }
    }

    public void write(Byteable val) {
        write(val.toBytes());
    }
}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

public class ByteReader {

    private final byte[] buffer;
    private int offset;

    public ByteReader(byte[] buffer) {
        this(buffer, 0);
    }

    public ByteReader(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
    }

    public Float16 readSFLOAT() {
        return Float16.of(buffer[offset++], buffer[offset++]);
    }
}

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

    public boolean hasBytes() {
        return offset < buffer.length;
    }

    public byte readByte() {
        return buffer[offset++];
    }

    public Float16 readFloat16() {
        return Float16.of(buffer[offset++], buffer[offset++]);
    }

    public Float32 readFloat32() {
        return Float32.of(buffer[offset++], buffer[offset++], buffer[offset++], buffer[offset++]);
    }

    public UInt8 readUInt8() {
        return UInt8.of(buffer[offset++]);
    }

    public UInt16 readUInt16() {
        return UInt16.of(buffer[offset++], buffer[offset++]);
    }
}

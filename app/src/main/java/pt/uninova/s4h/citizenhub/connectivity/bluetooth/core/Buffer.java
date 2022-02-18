package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import java.util.Arrays;

public class Buffer {

    private final byte[] bufferArray;
    private int offset;

    public Buffer(int capacity) {
        bufferArray = new byte[capacity];
        offset = 0;
    }

    public Buffer(byte[] bytes) {
        bufferArray = Arrays.copyOf(bytes, bytes.length);
        offset = 0;
    }

    public byte[] getBytes() {
        return bufferArray;
    }

    public int getBytesLeft() {
        return bufferArray.length - offset;
    }

    public boolean hasBytes() {
        return offset < bufferArray.length;
    }

    public byte readByte() {
        return bufferArray[offset++];
    }

    public Float16 readFloat16() {
        return Float16.of(bufferArray[offset++], bufferArray[offset++]);
    }

    public Float32 readFloat32() {
        return Float32.of(bufferArray[offset++], bufferArray[offset++], bufferArray[offset++], bufferArray[offset++]);
    }

    public UInt8 readUInt8() {
        return UInt8.of(bufferArray[offset++]);
    }

    public UInt16 readUInt16() {
        return UInt16.of(bufferArray[offset++], bufferArray[offset++]);
    }

    public void write(byte... bytes) {
        for (byte i : bytes) {
            bufferArray[offset++] = i;
        }
    }

    public void write(ByteSerializable val) {
        write(val.toBytes());
    }
}

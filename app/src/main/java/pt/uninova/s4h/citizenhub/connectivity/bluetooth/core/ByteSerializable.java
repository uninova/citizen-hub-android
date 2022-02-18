package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

public interface ByteSerializable {

    byte[] toBytes();

    void write(Buffer buffer);

}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

public interface CharacteristicListener {

    UUID getCharacteristicUuid();

    UUID getServiceUuid();

    void onChange(byte[] value);

    void onRead(byte[] value);

    void onReadFailure(byte[] value, int status);

    void onWrite(byte[] value);

    void onWriteFailure(byte[] value, int status);

}

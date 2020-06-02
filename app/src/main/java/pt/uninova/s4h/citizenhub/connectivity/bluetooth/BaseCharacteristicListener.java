package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

public class BaseCharacteristicListener implements CharacteristicListener {

    private final UUID characteristicUuid;
    private final UUID serviceUuid;

    public BaseCharacteristicListener(UUID serviceUuid, UUID characteristicUuid) {
        this.serviceUuid = serviceUuid;
        this.characteristicUuid = characteristicUuid;
    }

    @Override
    public UUID getCharacteristicUuid() {
        return characteristicUuid;
    }

    @Override
    public UUID getServiceUuid() {
        return serviceUuid;
    }

    @Override
    public void onChange(byte[] value) {
    }

    @Override
    public void onRead(byte[] value) {
    }

    @Override
    public void onReadFailure(byte[] value, int status) {
    }

    @Override
    public void onWrite(byte[] value) {
    }

    @Override
    public void onWriteFailure(byte[] value, int status) {
    }

}

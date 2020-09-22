package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

public class BaseCharacteristicListener implements CharacteristicListener {

    final private UUID service;
    final private UUID characteristic;

    public BaseCharacteristicListener(UUID service, UUID characteristic) {
        this.service = service;
        this.characteristic = characteristic;
    }

    @Override
    public UUID getCharacteristicUuid() {
        return characteristic;
    }

    @Override
    public UUID getServiceUuid() {
        return service;
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

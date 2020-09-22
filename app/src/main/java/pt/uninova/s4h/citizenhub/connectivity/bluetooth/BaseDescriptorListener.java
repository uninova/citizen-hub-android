package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

public class BaseDescriptorListener implements DescriptorListener {

    final private UUID service;
    final private UUID characteristic;
    final private UUID descriptor;

    public BaseDescriptorListener(UUID service, UUID characteristic, UUID descriptor) {
        this.service = service;
        this.characteristic = characteristic;
        this.descriptor = descriptor;
    }

    @Override
    public UUID getCharacteristicUuid() {
        return characteristic;
    }

    @Override
    public UUID getDescriptorUuid() {
        return descriptor;
    }

    @Override
    public UUID getServiceUuid() {
        return service;
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

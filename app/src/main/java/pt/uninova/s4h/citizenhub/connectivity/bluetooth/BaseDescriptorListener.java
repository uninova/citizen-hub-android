package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

public class BaseDescriptorListener implements DescriptorListener {

    private final UUID characteristicUuid;
    private final UUID descriptorUuid;
    private final UUID serviceUuid;

    public BaseDescriptorListener(UUID serviceUuid, UUID characteristicUuid, UUID descriptorUuid) {
        this.serviceUuid = serviceUuid;
        this.characteristicUuid = characteristicUuid;
        this.descriptorUuid = descriptorUuid;
    }

    @Override
    public UUID getCharacteristicUuid() {
        return characteristicUuid;
    }

    @Override
    public UUID getDescriptorUuid() {
        return descriptorUuid;
    }

    @Override
    public UUID getServiceUuid() {
        return serviceUuid;
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

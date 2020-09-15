package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.service.DeviceManager;

public class StepFeature extends BluetoothFeature {

    private static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("");
    private static UUID stepServiceUUID = UUID.fromString("");
    private static UUID stepCharacteristicUUID = UUID.fromString("");
    private static UUID stepDescriptorUUID;
    private static UUID uuid = DeviceManager.namespaceGenerator().getUUID("bluetooth.generic.step_measurement");

    public  StepFeature(BluetoothConnection connection, UUID id) {
        super(connection, id);
    }


    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public boolean enable() {
        try {
            getConnection().enableNotifications(stepServiceUUID, stepCharacteristicUUID);
            // observer -> measurementRepository.add;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean disable() {
        try {
            getConnection().disableNotifications(stepServiceUUID, stepCharacteristicUUID);
            return true;
        } catch (Exception e) {
            return false;
            // measurementRepository.add;
        }
    }
}


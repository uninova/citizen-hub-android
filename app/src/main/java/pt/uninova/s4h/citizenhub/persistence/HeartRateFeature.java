package pt.uninova.s4h.citizenhub.persistence;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothFeature;
import pt.uninova.s4h.citizenhub.service.DeviceManager;

public class HeartRateFeature extends BluetoothFeature {
    private static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    private static UUID heartRateServiceUUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    private static UUID heartRateCharacteristicUUID = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    private static UUID heartRateDescriptorUUID;
    private static UUID uuid = DeviceManager.namespaceGenerator().getUUID("bluetooth.generic.heart_rate_measurement");


    public HeartRateFeature(BluetoothConnection connection, UUID id) {
        super(connection, id);
    }


    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public boolean enable() {
        try {
            getConnection().enableNotifications(heartRateServiceUUID, heartRateCharacteristicUUID);
            // observer -> measurementRepository.add;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean disable() {
        try {
            getConnection().disableNotifications(heartRateServiceUUID, heartRateCharacteristicUUID);
            return true;
        } catch (Exception e) {
            return false;
            // measurementRepository.add;
        }
    }
}

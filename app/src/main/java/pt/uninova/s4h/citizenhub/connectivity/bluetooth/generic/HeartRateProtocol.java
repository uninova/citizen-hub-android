package pt.uninova.s4h.citizenhub.connectivity.bluetooth.generic;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;

public abstract class HeartRateProtocol extends BluetoothProtocol {
    private static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    private static UUID heartRateServiceUUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    private static UUID heartRateCharacteristicUUID = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    private static UUID heartRateDescriptorUUID;
    private static UUID uuid = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.generic.heart_rate_measurement");


    public HeartRateProtocol(BluetoothConnection connection, UUID id) {
        super(connection, id);
    }


    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public void enable() {
        try {
            getConnection().enableNotifications(heartRateServiceUUID, heartRateCharacteristicUUID);
            // observer -> measurementRepository.add;
        } catch (Exception e) {
        }
    }

    @Override
    public void disable() {
        try {
            getConnection().disableNotifications(heartRateServiceUUID, heartRateCharacteristicUUID);
        } catch (Exception e) {
            // measurementRepository.add;
        }
    }
}

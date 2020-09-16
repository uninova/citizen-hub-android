package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbz;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothProtocol;

import java.util.UUID;

public abstract class PostureProtocol extends BluetoothProtocol {

    private static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("");
    private static UUID postureServiceUUID = UUID.fromString("");
    private static UUID postureCharacteristicUUID = UUID.fromString("");
    private static UUID postureDescriptorUUID;
    private static UUID uuid = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.generic.posture_measurement");

    public PostureProtocol(BluetoothConnection connection, UUID id) {
        super(connection, id);
    }


    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public void enable() {
        try {
            getConnection().enableNotifications(postureServiceUUID, postureCharacteristicUUID);
            // observer -> measurementRepository.add;
        } catch (Exception e) {
        }
    }

    @Override
    public void disable() {
        try {
            getConnection().disableNotifications(postureServiceUUID, postureCharacteristicUUID);
        } catch (Exception e) {
            // measurementRepository.add;
        }
    }
}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth.generic;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothProtocol;

import java.util.UUID;

public abstract class StepProtocol extends BluetoothProtocol {

    private static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("");
    private static UUID stepServiceUUID = UUID.fromString("");
    private static UUID stepCharacteristicUUID = UUID.fromString("");
    private static UUID stepDescriptorUUID;
    private static UUID uuid = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.generic.step_measurement");

    public StepProtocol(BluetoothConnection connection, UUID id) {
        super(connection, id);
    }


    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public void enable() {
        try {
            getConnection().enableNotifications(stepServiceUUID, stepCharacteristicUUID);
            // observer -> measurementRepository.add;
        } catch (Exception e) {
        }
    }

    @Override
    public void disable() {
        try {
            getConnection().disableNotifications(stepServiceUUID, stepCharacteristicUUID);
        } catch (Exception e) {
            // measurementRepository.add;
        }
    }
}


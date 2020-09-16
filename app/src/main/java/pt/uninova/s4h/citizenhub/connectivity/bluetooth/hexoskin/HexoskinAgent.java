package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.generic.GenericBluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.generic.HeartRateProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;

public abstract class HexoskinAgent extends GenericBluetoothAgent {
    private static UUID uuid = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin");
    private String device_address;
    private HeartRateProtocol heartRateFeature;

    public HexoskinAgent(BluetoothConnection connection) {
        super(connection);

    }

    public UUID getId() {
        return uuid;
    }

    public void connect() {

    }

    public void disconnect() {
        ;
    }
}

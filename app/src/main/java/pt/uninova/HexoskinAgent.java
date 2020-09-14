package pt.uninova;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;

public class HexoskinAgent extends GenericBluetoothAgent {
    private static UUID uuid = DeviceManager.namespaceGenerator().getUUID("bluetooth.hexoskin");
    private String device_address;
    private HeartRateFeature heartRateFeature;

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

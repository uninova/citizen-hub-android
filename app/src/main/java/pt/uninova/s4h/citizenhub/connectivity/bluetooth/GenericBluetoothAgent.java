package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.app.Application;

import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.service.DeviceManager;

public class GenericBluetoothAgent extends BluetoothAgent {
    private static UUID uuid = DeviceManager.namespaceGenerator().getUUID("bluetooth.generic");
    private Application app;
    private BluetoothConnection connection;
    private String device_address;
    private String name;
    private Boolean state;
    private List<Feature> featureList;

    public GenericBluetoothAgent(BluetoothConnection connection) {
        super(connection);
        this.connection = connection;
    }

    public void getFeature(UUID feature_uuid) {

    }

    @Override
    public BluetoothConnectionState getStateId() {
        return null;
    }

    @Override
    public UUID getId() {
        return null;
    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

}

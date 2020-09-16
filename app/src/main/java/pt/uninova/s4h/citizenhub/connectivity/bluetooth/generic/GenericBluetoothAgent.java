package pt.uninova.s4h.citizenhub.connectivity.bluetooth.generic;

import android.app.Application;

import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;

public abstract class GenericBluetoothAgent extends BluetoothAgent {
    private static UUID uuid = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.generic");
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
    public AgentState getState() {
        return null;
    }

    @Override
    public UUID getId() {
        return null;
    }

    public void connect() {

    }

    public void disconnect() {

    }

}

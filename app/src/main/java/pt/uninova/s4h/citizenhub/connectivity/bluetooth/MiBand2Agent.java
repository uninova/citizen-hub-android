package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.service.DeviceManager;

public class MiBand2Agent implements Agent {

    private static UUID uuid = DeviceManager.namespaceGenerator().getUUID("bluetooth.miband2");
    private String device_address;
    private HeartRateFeature heartRateFeature;

    public MiBand2Agent(BluetoothConnection connection) {
        super();

    }


    @Override
    public BluetoothConnectionState getStateId() {
        return null;
    }

    @Override
    public List<Feature> getFeatureList() {
        return null;
    }

    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public Boolean getState() {
        return null;
    }

    @Override
    public void setState(Boolean state) {

    }
}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.persistence.FeatureRepository;

public abstract class BluetoothAgent implements Agent {
    final private BluetoothConnection connection;
    // private List<Feature> featureList;

    private String deviceAddress;
    private String name;
    private Boolean state;
    private FeatureRepository featureRepository;

    protected BluetoothAgent(BluetoothConnection connection) {
        this.connection = connection;
    }

//    Set<UUID> getSupportedFeatureIds() {
//        return featureMap.keySet();
//    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    @Override
    public List<Feature> getFeatureList() {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

}


//enable feature, getfeature, connect
// criar Miband2Agent, GenericAgent


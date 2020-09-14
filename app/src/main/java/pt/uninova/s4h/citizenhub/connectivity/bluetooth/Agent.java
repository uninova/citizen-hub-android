package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.persistence.Feature;

public interface Agent {

    BluetoothConnectionState getStateId();

    List<Feature> getFeatureList();

    UUID getId();

    void connect();

    void disconnect();

    Boolean getState();

    void setState(Boolean state);


    // lista de features com uuid em que cada feature tem um id e dá para ligar/desligar para id
}

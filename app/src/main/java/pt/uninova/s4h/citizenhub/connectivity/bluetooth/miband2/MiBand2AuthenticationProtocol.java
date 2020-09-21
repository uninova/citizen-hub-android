package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothProtocol;

import java.util.UUID;

public class MiBand2AuthenticationProtocol extends BluetoothProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.authentication");

    public MiBand2AuthenticationProtocol(BluetoothConnection connection) {
        super(ID, connection);
    }

    @Override
    public void disable() {

    }

    @Override
    public void enable() {

    }

}

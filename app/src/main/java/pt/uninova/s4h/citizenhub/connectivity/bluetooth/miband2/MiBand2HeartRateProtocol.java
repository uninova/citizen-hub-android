package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import pt.uninova.s4h.citizenhub.connectivity.AbstractMessagingProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;

import java.util.UUID;

public class MiBand2HeartRateProtocol extends AbstractMessagingProtocol<Integer> {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.heartrate");

    public MiBand2HeartRateProtocol(BluetoothConnection connection) {
        super();
    }

    @Override
    public void disable() {

    }

    @Override
    public void enable() {

    }

    @Override
    public UUID getId() {
        return ID;
    }
}

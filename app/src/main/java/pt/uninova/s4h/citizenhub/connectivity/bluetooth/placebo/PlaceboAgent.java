package pt.uninova.s4h.citizenhub.connectivity.bluetooth.placebo;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2DistanceProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2HeartRateProtocol;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaceboAgent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2");

    public PlaceboAgent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(MiBand2HeartRateProtocol.ID, new MiBand2HeartRateProtocol(connection));
        protocolMap.put(MiBand2DistanceProtocol.ID, new MiBand2DistanceProtocol(connection));

        return protocolMap;
    }

    @Override
    public void disable() {
    }

    @Override
    public void enable() {

    }
}

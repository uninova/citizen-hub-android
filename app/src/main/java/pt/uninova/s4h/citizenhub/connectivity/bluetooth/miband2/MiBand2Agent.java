package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiBand2Agent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2");

    public MiBand2Agent(BluetoothConnection connection) {
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
        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        MiBand2AuthenticationProtocol auth = new MiBand2AuthenticationProtocol(getConnection());

        auth.getObservers().add(value -> {
            if (value.getNewState() == ProtocolState.ENABLED) {
                setState(AgentState.ENABLED);

                getProtocol(MiBand2HeartRateProtocol.ID).enable();
                getProtocol(MiBand2DistanceProtocol.ID).enable();
            }
        });

        auth.enable();
    }
}

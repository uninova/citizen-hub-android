package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.util.messaging.Observer;

public class UprightGoAgent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo");

    public UprightGoAgent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();
        protocolMap.put(UprightGoProtocol.ID, new UprightGoProtocol(connection));
        return protocolMap;
    }

    @Override
    public void disable() {
        for (UUID i : getPublicProtocolIds(ProtocolState.ENABLED)) {
            getProtocol(i).disable();
        }
        getConnection().close();
        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        UprightGoProtocol protocol = (UprightGoProtocol) getProtocol(UprightGoProtocol.ID);
        protocol.getObservers().add(new Observer<StateChangedMessage<ProtocolState>>() {
            @Override
            public void onChanged(StateChangedMessage<ProtocolState> value) {
                if (value.getNewState() == ProtocolState.ENABLED) {
                    UprightGoAgent.this.setState(AgentState.ENABLED);
                    protocol.getObservers().remove(this);
                }
            }
        });
        protocol.enable();
    }
}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

import pt.uninova.s4h.citizenhub.connectivity.*;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.util.messaging.Observer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KbzPostureAgent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.kbzposture");

    public KbzPostureAgent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(KbzRawProtocol.ID, new KbzRawProtocol(connection));

        return protocolMap;
    }

    @Override
    public void disable() {
        for (UUID i : getPublicProtocolIds(ProtocolState.ENABLED)) {
            getProtocol(i).disable();
        }

        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        KbzRawProtocol protocol = new KbzRawProtocol(getConnection());

        protocol.getObservers().add(new Observer<StateChangedMessage<ProtocolState>>() {
            @Override
            public void onChanged(StateChangedMessage<ProtocolState> value) {
                if (value.getNewState() == ProtocolState.ENABLED) {
                    KbzPostureAgent.this.setState(AgentState.ENABLED);

                    protocol.getObservers().remove(this);
                }
            }
        });

        protocol.enable();
    }
}

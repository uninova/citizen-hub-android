package pt.uninova.s4h.citizenhub.connectivity;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public abstract class AbstractAgent implements Agent {

    final private UUID id;

    final private Map<UUID, Protocol> protocolMap;

    final private Dispatcher<StateChangedMessage<AgentState, ? extends Agent>> stateChangedDispatcher;

    private AgentState state;

    private AbstractAgent() {
        this.protocolMap = null;
        this.id = null;
        this.stateChangedDispatcher = null;
    }

    protected AbstractAgent(UUID id) {
        this(id, new HashMap<>());
    }

    protected AbstractAgent(UUID id, Map<UUID, Protocol> protocolMap) {
        this.protocolMap = protocolMap;
        this.id = id;

        stateChangedDispatcher = new Dispatcher<>();
    }

    protected void enableProtocol(UUID protocolId, Protocol protocol) {
        this.protocolMap.put(protocolId, protocol);
        protocol.enable();
    }

    public Set<Observer<StateChangedMessage<AgentState, ? extends Agent>>> getObservers() {
        return stateChangedDispatcher.getObservers();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Protocol getProtocol(UUID protocolId) {
        return protocolMap.get(protocolId);
    }

    @Override
    public Set<UUID> getProtocolIds(ProtocolState state) {
        final Set<UUID> res = new HashSet<>();

        for (UUID i : protocolMap.keySet()) {
            final Protocol protocol = protocolMap.get(i);

            if (protocol != null && protocol.getState() == state) {
                res.add(i);
            }
        }

        return res;
    }

    @Override
    public AgentState getState() {
        return state;
    }

    protected void setState(AgentState value) {
        if (state != value) {
            final AgentState oldState = state;

            state = value;

            stateChangedDispatcher.dispatch(new StateChangedMessage<>(value, oldState, this));
        }
    }
}

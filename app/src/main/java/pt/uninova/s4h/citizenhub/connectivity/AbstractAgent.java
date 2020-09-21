package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

import java.util.*;

public abstract class AbstractAgent implements Agent {

    final private Map<UUID, Protocol> protocolMap;
    final private Dispatcher<StateChangedMessage<AgentState>> stateChangedDispatcher;

    private AgentState state;

    protected AbstractAgent() {
        this(new HashMap<>());
    }

    protected AbstractAgent(Map<UUID, Protocol> protocolMap) {
        this.protocolMap = protocolMap;
        stateChangedDispatcher = new Dispatcher<>();
    }

    public Set<Observer<StateChangedMessage<AgentState>>> getObservers() {
        return stateChangedDispatcher.getObservers();
    }

    @Override
    public Protocol getProtocol(UUID protocolId) {
        return protocolMap.get(protocolId);
    }

    public Set<UUID> getPublicProtocolIds() {
        return Collections.unmodifiableSet(protocolMap.keySet());
    }

    @Override
    public Set<UUID> getPublicProtocolIds(ProtocolState state) {
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

            stateChangedDispatcher.dispatch(new StateChangedMessage<>(value, oldState));
        }
    }
}

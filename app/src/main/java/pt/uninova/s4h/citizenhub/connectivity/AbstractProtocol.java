package pt.uninova.s4h.citizenhub.connectivity;

import java.io.Closeable;
import java.util.Set;
import java.util.UUID;

import care.data4life.fhir.r4.model.Age;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public abstract class AbstractProtocol implements Closeable, Protocol {

    final private UUID id;
    final private Dispatcher<StateChangedMessage<ProtocolState, ? extends Agent>> stateChangedDispatcher;

    private ProtocolState state;
    private Agent agent;

    protected AbstractProtocol(UUID id, Agent agent) {
        this.id = id;
        this.agent = agent;
        stateChangedDispatcher = new Dispatcher<>();
    }

    @Override
    public void close() {
        stateChangedDispatcher.close();
        //limpar memoria do agent
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
    }

    @Override
    public void enable() {
        setState(ProtocolState.ENABLED);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Set<Observer<StateChangedMessage<ProtocolState, ? extends Agent>>> getObservers() {
        return stateChangedDispatcher.getObservers();
    }

    public Dispatcher<StateChangedMessage<ProtocolState, ? extends Agent>> getStateChangedDispatcher() {
        return stateChangedDispatcher;
    }

    public Agent getAgent() {
        return agent;
    }

    @Override
    public ProtocolState getState() {
        return state;
    }

    protected void setState(ProtocolState value) {
        if (state != value) {
            final ProtocolState oldState = state;

            state = value;
            stateChangedDispatcher.dispatch(new StateChangedMessage<>(value, oldState, agent));
        }
    }
}

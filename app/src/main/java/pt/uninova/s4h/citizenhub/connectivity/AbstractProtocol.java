package pt.uninova.s4h.citizenhub.connectivity;

import java.io.Closeable;
import java.util.Set;
import java.util.UUID;

import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public abstract class AbstractProtocol implements Closeable, Protocol {

    private final UUID id;
    private final Agent agent;

    private final Dispatcher<StateChangedMessage<ProtocolState, ? extends Protocol>> stateChangedDispatcher;

    private ProtocolState state;

    protected AbstractProtocol(UUID id, Agent agent) {
        this.id = id;
        this.agent = agent;

        this.stateChangedDispatcher = new Dispatcher<>();
        this.state = ProtocolState.DISABLED;
    }

    @Override
    public void addStateObserver(Observer<StateChangedMessage<ProtocolState, ? extends Protocol>> observer) {
        this.stateChangedDispatcher.addObserver(observer);
    }

    @Override
    public void close() {
        stateChangedDispatcher.close();
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

    public Agent getAgent() {
        return agent;
    }

    @Override
    public ProtocolState getState() {
        return state;
    }

    @Override
    public void removeStateObserver(Observer<StateChangedMessage<ProtocolState, ? extends Protocol>> observer) {
        this.stateChangedDispatcher.removeObserver(observer);
    }

    protected void setState(ProtocolState value) {
        if (state != value) {
            final ProtocolState oldState = state;

            state = value;
            stateChangedDispatcher.dispatch(new StateChangedMessage<>(value, oldState, this));
        }
    }
}

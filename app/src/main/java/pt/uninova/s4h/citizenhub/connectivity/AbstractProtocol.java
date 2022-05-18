package pt.uninova.s4h.citizenhub.connectivity;

import java.io.Closeable;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.util.messaging.Dispatcher;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public abstract class AbstractProtocol implements Closeable, Protocol {

    private final UUID id;
    private final Agent agent;

    private final Dispatcher<StateChangedMessage<Integer, ? extends Protocol>> stateChangedDispatcher;

    private int state;

    protected AbstractProtocol(UUID id, Agent agent) {
        this.id = id;
        this.agent = agent;

        this.stateChangedDispatcher = new Dispatcher<>();
        this.state = Protocol.STATE_DISABLED;
    }

    @Override
    public void addStateObserver(Observer<StateChangedMessage<Integer, ? extends Protocol>> observer) {
        this.stateChangedDispatcher.addObserver(observer);
    }

    @Override
    public void close() {
        stateChangedDispatcher.close();
    }

    @Override
    public void disable() {
        setState( Protocol.STATE_DISABLED);
    }

    @Override
    public void enable() {
        setState( Protocol.STATE_ENABLED);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Agent getAgent() {
        return agent;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void removeStateObserver(Observer<StateChangedMessage<Integer, ? extends Protocol>> observer) {
        this.stateChangedDispatcher.removeObserver(observer);
    }

    protected void setState(int value) {
        if (state != value) {
            final int oldState = state;

            state = value;
            stateChangedDispatcher.dispatch(new StateChangedMessage<>(value, oldState, this));
        }
    }
}

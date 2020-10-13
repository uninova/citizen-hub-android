package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

import java.util.Set;
import java.util.UUID;

public abstract class AbstractProtocol implements Protocol {

    final private UUID id;
    final private Dispatcher<StateChangedMessage<ProtocolState>> stateChangedDispatcher;

    private ProtocolState state;

    protected AbstractProtocol(UUID id) {
        this.id = id;

        stateChangedDispatcher = new Dispatcher<>();
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
    public Set<Observer<StateChangedMessage<ProtocolState>>> getObservers() {
        return stateChangedDispatcher.getObservers();
    }

    @Override
    public ProtocolState getState() {
        return state;
    }

    protected void setState(ProtocolState value) {
        if (state != value) {
            final ProtocolState oldState = state;

            state = value;

            stateChangedDispatcher.dispatch(new StateChangedMessage<>(value, oldState));
        }
    }
}

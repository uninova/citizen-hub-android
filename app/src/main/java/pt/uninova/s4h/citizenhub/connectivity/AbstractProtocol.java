package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

import java.util.Set;

public abstract class AbstractProtocol implements Protocol {

    final private Dispatcher<StateChangedMessage<ProtocolState>> stateChangedDispatcher;

    private ProtocolState state;

    protected AbstractProtocol() {
        stateChangedDispatcher = new Dispatcher<>();
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

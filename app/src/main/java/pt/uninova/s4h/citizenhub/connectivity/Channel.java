package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.util.events.EventDispatcher;
import pt.uninova.util.events.GenericEventMessage;

import java.util.UUID;

public abstract class Channel implements AutoCloseable {

    public enum State {
        DISABLED,
        ENABLED
    }

    private State state;

    private final EventDispatcher<Channel, ChannelMessage> onMessage;
    private final EventDispatcher<Channel, GenericEventMessage<State>> onStateChanged;

    public Channel() {
        state = State.DISABLED;

        this.onMessage = new EventDispatcher<>(this);
        this.onStateChanged = new EventDispatcher<>(this);
    }

    @Override
    public void close() throws Exception {
        onMessage.clearListeners();
        onStateChanged.clearListeners();
    }

    public void disable() {
        if (state != State.DISABLED) {
            state = State.DISABLED;
            onStateChanged.dispatch(new GenericEventMessage<State>(state));
        }
    }

    public void enable() {
        if (state != State.ENABLED) {
            state = State.ENABLED;
            onStateChanged.dispatch(new GenericEventMessage<State>(state));
        }
    }

    public abstract UUID getId();

    public State getState() {
        return state;
    }

    public EventDispatcher<Channel, ChannelMessage> onMessage() {
        return onMessage;
    }

    public EventDispatcher<Channel, GenericEventMessage<State>> onStateChanged() {
        return onStateChanged;
    }
}

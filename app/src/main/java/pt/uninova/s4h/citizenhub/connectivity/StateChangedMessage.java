package pt.uninova.s4h.citizenhub.connectivity;

public class StateChangedMessage<T> {

    final private T newState;
    final private T oldState;

    public StateChangedMessage(T newState, T oldState) {
        this.newState = newState;
        this.oldState = oldState;
    }

    public T getNewState() {
        return newState;
    }

    public T getOldState() {
        return oldState;
    }
}

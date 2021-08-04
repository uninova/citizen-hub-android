package pt.uninova.s4h.citizenhub.connectivity;

public class StateChangedMessage<T, U> {

    final private T newState;
    final private T oldState;
    final private U source;

    public StateChangedMessage(T newState, T oldState, U source) {
        this.newState = newState;
        this.oldState = oldState;
        this.source = source;
    }

    public T getNewState() {
        return newState;
    }

    public T getOldState() {
        return oldState;
    }

    public U getSource() {
        return source;
    }
}

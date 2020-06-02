package pt.uninova.util.events;

public class GenericEventMessage<T> extends EventMessage {

    private final T message;

    public GenericEventMessage(T message) {
        this.message = message;
    }

    public T getMessage() {
        return message;
    }
}

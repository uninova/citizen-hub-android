package pt.uninova.util;

public class Event<T> {

    private final T message;

    public Event(T m) {
        message = m;
    }

    public T getMessage() {
        return message;
    }

}

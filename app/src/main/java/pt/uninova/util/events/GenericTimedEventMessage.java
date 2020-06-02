package pt.uninova.util.events;

import java.util.Date;

public class GenericTimedEventMessage<T> extends TimedEventMessage {

    private final T message;
    
    public GenericTimedEventMessage(T message) {
        super();

        this.message = message;
    }

    private T getMessage() {
        return message;
    }
}

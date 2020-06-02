package pt.uninova.util.events;

import java.util.Date;

public class TimedEventMessage extends EventMessage {

    private final Date timestamp;

    public TimedEventMessage() {
        super();

        this.timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }

}

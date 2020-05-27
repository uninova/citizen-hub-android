package pt.uninova.util;

import java.util.Date;

public class TimedEvent<T> extends Event<T> {

    private final Date timestamp;

    public TimedEvent(T m) {
        super(m);

        timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

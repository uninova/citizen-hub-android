package pt.uninova.util.time;

import java.time.Duration;
import java.time.Instant;

import pt.uninova.util.Pair;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public class Accumulator<T> {

    private Instant timestamp;
    private T value;

    private final Dispatcher<Pair<T, Duration>> dispatcher;

    public Accumulator() {
        this.dispatcher = new Dispatcher<>();
    }

    public void addObserver(Observer<Pair<T, Duration>> observer) {
        dispatcher.addObserver(observer);
    }

    public void clear() {
        dispatcher.close();
    }

    public void flush() {
        flush(Instant.now());
    }

    public void flush(Instant timestamp) {
        notify(value, Duration.between(timestamp, this.timestamp));

        this.timestamp = timestamp;
    }

    private void notify(T value, Duration duration) {
        dispatcher.dispatch(new Pair<>(value, duration));
    }

    public void removeObserver(Observer<Pair<T, Duration>> observer) {
        dispatcher.removeObserver(observer);
    }

    public void reset(T value) {
        reset(value, Instant.now());
    }

    public void reset(T value, Instant timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public void set(T value) {
        set(value, Instant.now());
    }

    public void set(T value, Instant timestamp) {
        if (value != this.value) {
            notify(value, Duration.between(timestamp, this.timestamp));
        }

        this.value = value;
        this.timestamp = timestamp;
    }
}

package pt.uninova.util.messaging;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;

public class Dispatcher<T> implements Closeable {

    final private Set<Observer<T>> observerSet;

    public Dispatcher() {
        observerSet = new HashSet<>();
    }

    @Override
    public void close() {
        observerSet.clear();
    }

    public void dispatch(T message) {
        for (Observer<T> i : observerSet) {
            i.onChanged(message);
        }
    }

    public Set<Observer<T>> getObservers() {
        return observerSet;
    }

}

package pt.uninova.s4h.citizenhub.util.messaging;

import java.io.Closeable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Dispatcher<T> implements Closeable {

    private final Set<Observer<T>> observerSet;

    public Dispatcher() {
        observerSet = ConcurrentHashMap.newKeySet();
    }

    public void addObserver(Observer<T> observer) {
        this.observerSet.add(observer);
    }

    @Override
    public void close() {
        observerSet.clear();
    }

    public void dispatch(T message) {
        for (Observer<T> observer : observerSet) {
            observer.observe(message);
        }
    }

    public void removeObserver(Observer<T> observer) {
        this.observerSet.remove(observer);
    }
}

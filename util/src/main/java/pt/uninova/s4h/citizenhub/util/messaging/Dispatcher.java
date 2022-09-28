package pt.uninova.s4h.citizenhub.util.messaging;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Dispatcher<T> implements Closeable {

    final private Set<Observer<T>> observerSet;
    final private Set<Observer<T>> delayedRemoval;

    private boolean dispatching;

    public Dispatcher() {
        observerSet = new HashSet<>();
        delayedRemoval = new HashSet<>();

        dispatching = false;
    }

    public void addObserver(Observer<T> observer) {
        synchronized (observerSet) {
            this.observerSet.add(observer);
        }
    }

    @Override
    public void close() {
        synchronized (observerSet) {
            observerSet.clear();
        }
    }

    public void dispatch(T message) {
        synchronized (observerSet) {
            dispatching = true;
        }

        final Iterator<Observer<T>> observerIterator = observerSet.iterator();

        while (observerIterator.hasNext()) {
            final Observer<T> observer = observerIterator.next();

            if (delayedRemoval.contains(observer)) {
                observerIterator.remove();
            } else {
                observer.observe(message);
            }
        }

        synchronized (observerSet) {
            dispatching = false;
        }
    }

    public void removeObserver(Observer<T> observer) {
        synchronized (observerSet) {
            if (dispatching) {
                delayedRemoval.add(observer);
            } else {
                this.observerSet.remove(observer);
            }
        }
    }
}

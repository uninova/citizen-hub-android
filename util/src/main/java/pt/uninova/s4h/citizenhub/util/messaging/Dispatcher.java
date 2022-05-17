package pt.uninova.util.messaging;

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

    public synchronized void dispatch(T message) {
        synchronized (observerSet) {
            dispatching = true;
        }

        for (Observer<T> observer : observerSet) {
            observer.observe(message);
        }

        final Iterator<Observer<T>> i = delayedRemoval.iterator();

        while (i.hasNext()) {
            final Observer<T> observer = i.next();

            observerSet.remove(observer);
            i.remove();
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

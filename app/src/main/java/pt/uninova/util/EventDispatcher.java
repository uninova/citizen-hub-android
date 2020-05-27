package pt.uninova.util;

import java.util.Collection;
import java.util.LinkedList;

public class EventDispatcher<T extends Event> {

    private final Collection<EventListener<T>> eventListeners;

    public EventDispatcher() {
        eventListeners = new LinkedList<>();
    }

    public void addListener(EventListener<T> l) {
        synchronized (eventListeners) {
            eventListeners.add(l);
        }
    }

    public void clearListeners() {
        synchronized (eventListeners) {
            eventListeners.clear();
        }
    }

    public void dispatch(T e) {
        for (EventListener<T> i : eventListeners) {
            i.onEvent(e);
        }
    }

    public void removeListener(T l) {
        synchronized (eventListeners) {
            eventListeners.remove(l);
        }
    }
}

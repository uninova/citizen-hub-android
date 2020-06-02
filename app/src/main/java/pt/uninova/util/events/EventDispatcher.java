package pt.uninova.util.events;

import java.util.Collection;
import java.util.LinkedList;

public class EventDispatcher<S, M extends EventMessage> {

    private final Collection<EventListener<S, M>> eventListeners;
    private final S sender;

    public EventDispatcher(S sender) {
        eventListeners = new LinkedList<>();
        this.sender = sender;
    }

    public void addListener(EventListener<S, M> listener) {
        synchronized (eventListeners) {
            eventListeners.add(listener);
        }
    }

    public void clearListeners() {
        synchronized (eventListeners) {
            eventListeners.clear();
        }
    }

    public void dispatch(M message) {
        for (EventListener<S, M> i : eventListeners) {
            i.onEvent(sender, message);
        }
    }

    public void removeListener(EventListener<S, M> listener) {
        synchronized (eventListeners) {
            eventListeners.remove(listener);
        }
    }
}

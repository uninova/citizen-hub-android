package pt.uninova.s4h.citizenhub.connectivity;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public interface Protocol {

    public static final int STATE_DISABLED = 0;
    public static final int STATE_DISABLING = 1;
    public static final int STATE_ENABLED = 2;
    public static final int STATE_ENABLING = 3;
    public static final int STATE_SUSPENDED = 4;
    public static final int STATE_COMPLETED = 5;

    void addStateObserver(Observer<StateChangedMessage<Integer, ? extends Protocol>> observer);

    void disable();

    void enable();

    UUID getId();

    int getState();

    void removeStateObserver(Observer<StateChangedMessage<Integer, ? extends Protocol>> observer);
}

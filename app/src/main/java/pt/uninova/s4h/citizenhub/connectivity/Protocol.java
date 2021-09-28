package pt.uninova.s4h.citizenhub.connectivity;

import java.util.Set;
import java.util.UUID;

import pt.uninova.util.messaging.Observer;

public interface Protocol {

    void addStateObserver(Observer<StateChangedMessage<ProtocolState, ? extends Protocol>> observer);

    void disable();

    void enable();

    UUID getId();

    ProtocolState getState();

    void removeStateObserver(Observer<StateChangedMessage<ProtocolState, ? extends Protocol>> observer);

}

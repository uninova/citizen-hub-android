package pt.uninova.s4h.citizenhub.connectivity;

import java.util.Set;
import java.util.UUID;

import pt.uninova.util.messaging.Observer;

public interface Protocol {

    void disable();

    void enable();

    UUID getId();

    Set<Observer<StateChangedMessage<ProtocolState, ? extends Agent>>> getObservers();

    ProtocolState getState();

}

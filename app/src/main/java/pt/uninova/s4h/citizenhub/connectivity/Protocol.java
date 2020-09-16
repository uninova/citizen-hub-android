package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.util.messaging.Observer;

import java.util.Set;
import java.util.UUID;

public interface Protocol {

    void disable();

    void enable();

    UUID getId();

    Set<Observer<StateChangedMessage<ProtocolState>>> getObservers();

    ProtocolState getState();

}

package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.util.messaging.Observer;

import java.util.Set;

public interface MessagingProtocol<T> extends Protocol {

    Set<Observer<T>> getMessageObservers();

}

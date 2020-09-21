package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

import java.util.Set;
import java.util.UUID;

public abstract class AbstractMessagingProtocol<T> extends AbstractProtocol implements MessagingProtocol<T> {

    final private Dispatcher<T> messageDispatcher;

    protected AbstractMessagingProtocol(UUID id) {
        super(id);

        messageDispatcher = new Dispatcher<>();
    }

    protected Dispatcher<T> getMessageDispatcher() {
        return messageDispatcher;
    }

    @Override
    public Set<Observer<T>> getMessageObservers() {
        return messageDispatcher.getObservers();
    }

}

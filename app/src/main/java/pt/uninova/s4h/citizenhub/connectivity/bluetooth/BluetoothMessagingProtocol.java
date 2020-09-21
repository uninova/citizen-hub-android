package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import pt.uninova.s4h.citizenhub.connectivity.MessagingProtocol;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

import java.util.Set;
import java.util.UUID;

public abstract class BluetoothMessagingProtocol<T> extends BluetoothProtocol implements MessagingProtocol<T> {

    final private Dispatcher<T> messageDispatcher;

    public BluetoothMessagingProtocol(UUID id, BluetoothConnection connection) {
        super(id, connection);

        messageDispatcher = new Dispatcher<>();
    }

    @Override
    public Set<Observer<T>> getMessageObservers() {
        return messageDispatcher.getObservers();
    }

}

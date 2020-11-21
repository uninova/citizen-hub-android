package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import pt.uninova.s4h.citizenhub.connectivity.AbstractProtocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.util.messaging.Observer;

import java.util.UUID;

public abstract class BluetoothProtocol extends AbstractProtocol {

    final private BluetoothConnection connection;

    protected BluetoothProtocol(UUID id, BluetoothConnection connection) {
        super(id);

        this.connection = connection;
    }

    protected BluetoothConnection getConnection() {
        return connection;
    }
}

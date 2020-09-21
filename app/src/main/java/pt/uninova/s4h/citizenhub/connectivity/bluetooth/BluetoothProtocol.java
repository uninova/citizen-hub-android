package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import pt.uninova.s4h.citizenhub.connectivity.AbstractProtocol;

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

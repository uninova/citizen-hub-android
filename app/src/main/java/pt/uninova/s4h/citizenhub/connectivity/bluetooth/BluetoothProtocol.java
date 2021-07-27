package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractProtocol;

public abstract class BluetoothProtocol extends AbstractProtocol {

    final private BluetoothConnection connection;

    protected BluetoothProtocol(UUID id, BluetoothConnection connection, Class<?> agent) {
        super(id, agent);

        this.connection = connection;
    }

    protected BluetoothConnection getConnection() {
        return connection;
    }
}

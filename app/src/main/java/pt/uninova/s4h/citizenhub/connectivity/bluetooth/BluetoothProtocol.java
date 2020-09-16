package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import pt.uninova.s4h.citizenhub.connectivity.Protocol;

import java.util.UUID;

public abstract class BluetoothProtocol implements Protocol {
    private BluetoothConnection connection;
    private UUID id;

    public BluetoothProtocol(BluetoothConnection connection, UUID featureId) {
        this.connection = connection;
        this.id = featureId;
    }

    protected BluetoothConnection getConnection() {
        return connection;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public void enable() {
        try {
            connection.enableNotifications(id, id);
            //TODO add service & characteristic id
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disable() {
        try {
            connection.disableNotifications(id, id);
            //TODO add service & characteristic id
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}

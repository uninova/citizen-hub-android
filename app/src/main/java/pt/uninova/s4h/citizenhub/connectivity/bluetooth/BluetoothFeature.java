package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

public abstract class BluetoothFeature implements Feature {
    private BluetoothConnection connection;
    private UUID id;

    public BluetoothFeature(BluetoothConnection connection, UUID featureId) {
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
    public boolean enable() {
        try {
            connection.enableNotifications(id, id);
            //TODO add service & characteristic id
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean disable() {
        try {
            connection.disableNotifications(id, id);
            //TODO add service & characteristic id
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

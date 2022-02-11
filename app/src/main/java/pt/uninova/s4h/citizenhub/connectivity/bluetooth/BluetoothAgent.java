package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractAgent;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public abstract class BluetoothAgent extends AbstractAgent {

    public static final UUID UUID_SERVICE_BLOOD_PRESSURE = UUID.fromString("00001810-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_CHARACTERISTIC_DATE_TIME = UUID.fromString("00002a08-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_BLOOD_PRESSURE_MEASUREMENT = UUID.fromString("00002a35-0000-1000-8000-00805f9b34fb");


    final private BluetoothConnection connection;

    protected BluetoothAgent(UUID id, Set<UUID> supportedProtocolsIds, Set<MeasurementKind> supportedMeasurements, BluetoothConnection connection) {
        super(id, connection.getSource(), supportedProtocolsIds, supportedMeasurements);

        this.connection = connection;
    }

    public BluetoothConnection getConnection() {
        return connection;
    }

}

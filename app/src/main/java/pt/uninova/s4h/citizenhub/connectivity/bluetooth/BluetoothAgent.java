package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractAgent;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public abstract class BluetoothAgent extends AbstractAgent {

    final private BluetoothConnection connection;

    protected BluetoothAgent(UUID id, Set<UUID> supportedProtocolsIds, Set<MeasurementKind> supportedMeasurements, BluetoothConnection connection) {
        super(id, supportedProtocolsIds, supportedMeasurements);

        this.connection = connection;
    }

    protected BluetoothConnection getConnection() {
        return connection;
    }

}

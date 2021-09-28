package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractAgent;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public abstract class BluetoothAgent extends AbstractAgent {

    final private BluetoothConnection connection;

    protected BluetoothAgent(UUID id, Set<UUID> supportedProtocolsIds, Set<MeasurementKind> supportedMeasurements, BluetoothConnection connection) {
        super(id, supportedProtocolsIds, supportedMeasurements);

        this.connection = connection;

        getConnection().addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>>() {
            @Override
            public void observe(StateChangedMessage<BluetoothConnectionState, BluetoothConnection> value) {
                if (value.getNewState() == BluetoothConnectionState.DISCONNECTED && value.getOldState() == BluetoothConnectionState.CONNECTED) {

                } else if (value.getNewState() == BluetoothConnectionState.CONNECTED && value.getOldState() == BluetoothConnectionState.DISCONNECTED) {

                }
            }
        });
    }

    protected BluetoothConnection getConnection() {
        return connection;
    }

}

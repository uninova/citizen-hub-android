package pt.uninova.s4h.citizenhub.connectivity.bluetooth.and;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.CharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.std.characteristic.BloodPressureMeasurementCharacteristicValue;
import pt.uninova.s4h.citizenhub.data.BloodPressureValue;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public class BloodPressureProtocol extends BluetoothMeasuringProtocol {

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.and.ua651ble.bloodpressure");

    private static final UUID UUID_BLOODPRESSURE_SERVICE = UUID.fromString("00001810-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_BLOODPRESSURE_CHARACTERISTIC = UUID.fromString("00002a35-0000-1000-8000-00805f9b34fb");

    private final CharacteristicListener onChange = new BaseCharacteristicListener(UUID_BLOODPRESSURE_SERVICE, UUID_BLOODPRESSURE_CHARACTERISTIC) {
        @Override
        public void onChange(byte[] value) {
            final BloodPressureMeasurementCharacteristicValue val = new BloodPressureMeasurementCharacteristicValue(value);
            final Instant timestamp = val.hasTimeStamp() && val.getTimeStampDate() != null ? Instant.from(LocalDateTime.of(val.getTimeStampDate(), val.getTimeStampTime())) : Instant.now();
            final Sample sample = new Sample(timestamp, getAgent().getSource(), val.toMeasurements());

            getSampleDispatcher().dispatch(sample);
        }
    };

    private final Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>> onReady = (StateChangedMessage<BluetoothConnectionState, BluetoothConnection> value) -> {
        if (value.getNewState() == BluetoothConnectionState.READY) {
            getConnection().enableNotifications(UUID_BLOODPRESSURE_SERVICE, UUID_BLOODPRESSURE_CHARACTERISTIC, true);
        }
    };

    public BloodPressureProtocol(BluetoothConnection connection, Dispatcher<Sample> dispatcher, BluetoothAgent agent) {
        super(ID, connection, dispatcher, agent);
    }

    @Override
    public void disable() {
        getConnection().disableNotifications(UUID_BLOODPRESSURE_SERVICE, UUID_BLOODPRESSURE_CHARACTERISTIC);
        getConnection().removeCharacteristicListener(onChange);
        getConnection().removeConnectionStateChangeListener(onReady);

        super.disable();
    }

    @Override
    public void enable() {
        getConnection().addCharacteristicListener(onChange);
        getConnection().addConnectionStateChangeListener(onReady);
        // TODO: Improve first enable
        getConnection().enableNotifications(UUID_BLOODPRESSURE_SERVICE, UUID_BLOODPRESSURE_CHARACTERISTIC, true);

        super.enable();
    }
}

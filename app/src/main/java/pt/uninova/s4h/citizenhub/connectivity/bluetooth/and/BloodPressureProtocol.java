package pt.uninova.s4h.citizenhub.connectivity.bluetooth.and;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.CharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.BloodPressureMeasurement;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.DateTime;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.util.messaging.Dispatcher;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class BloodPressureProtocol extends BluetoothMeasuringProtocol {

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.and.ua651ble.bloodpressure");

    private final CharacteristicListener onChange = new BaseCharacteristicListener(BluetoothAgent.UUID_SERVICE_BLOOD_PRESSURE, BluetoothAgent.UUID_CHARACTERISTIC_BLOOD_PRESSURE_MEASUREMENT) {
        @Override
        public void onChange(byte[] value) {
            final BloodPressureMeasurement val = new BloodPressureMeasurement(value);
            final Measurement<?>[] measurements = val.toMeasurements();

            if (measurements.length == 0)
                return;

            final DateTime dateTime = val.getTimeStamp();
            final ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime.toLocalDateTime(), ZoneId.systemDefault());
            final Instant timestamp = dateTime.isValidDate() ? Instant.from(zonedDateTime) : Instant.now();

            final Sample sample = new Sample(timestamp, getAgent().getSource(), measurements);

            getSampleDispatcher().dispatch(sample);
        }
    };

    private final Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>> onReady = (StateChangedMessage<BluetoothConnectionState, BluetoothConnection> value) -> {
        if (value.getNewState() == BluetoothConnectionState.READY) {
            getConnection().enableNotifications(BluetoothAgent.UUID_SERVICE_BLOOD_PRESSURE, BluetoothAgent.UUID_CHARACTERISTIC_BLOOD_PRESSURE_MEASUREMENT, true);
        }
    };

    public BloodPressureProtocol(BluetoothConnection connection, Dispatcher<Sample> dispatcher, BluetoothAgent agent) {
        super(ID, connection, dispatcher, agent);
    }

    @Override
    public void disable() {
        getConnection().disableNotifications(BluetoothAgent.UUID_SERVICE_BLOOD_PRESSURE, BluetoothAgent.UUID_CHARACTERISTIC_BLOOD_PRESSURE_MEASUREMENT);
        getConnection().removeCharacteristicListener(onChange);
        getConnection().removeConnectionStateChangeListener(onReady);

        super.disable();
    }

    @Override
    public void enable() {
        getConnection().addCharacteristicListener(onChange);
        getConnection().addConnectionStateChangeListener(onReady);
        // TODO: Improve first enable
        getConnection().enableNotifications(BluetoothAgent.UUID_SERVICE_BLOOD_PRESSURE, BluetoothAgent.UUID_CHARACTERISTIC_BLOOD_PRESSURE_MEASUREMENT, true);

        super.enable();
    }
}

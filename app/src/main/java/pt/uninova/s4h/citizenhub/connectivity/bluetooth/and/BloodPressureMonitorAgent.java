package pt.uninova.s4h.citizenhub.connectivity.bluetooth.and;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.DateTime;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class BloodPressureMonitorAgent extends BluetoothAgent {

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.and.ua651ble");

    private static final Set<MeasurementKind> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
            MeasurementKind.BLOOD_PRESSURE
    )));

    private static final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
            BloodPressureProtocol.ID
    )));

    public BloodPressureMonitorAgent(BluetoothConnection connection) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds, connection);
    }

    @Override
    public void enable() {
        getConnection().writeCharacteristic(UUID_SERVICE_BLOOD_PRESSURE, UUID_CHARACTERISTIC_DATE_TIME, (new DateTime(LocalDateTime.now())).toBytes());

        super.enable();
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(MeasurementKind kind) {
        if (kind == MeasurementKind.BLOOD_PRESSURE) {
            return new BloodPressureProtocol(getConnection(), getSampleDispatcher(), this);
        }

        return null;
    }

    @Override
    public String getName() {
        return "A&D Medical Blood Pressure Monitor UA-651BLE";
    }
}

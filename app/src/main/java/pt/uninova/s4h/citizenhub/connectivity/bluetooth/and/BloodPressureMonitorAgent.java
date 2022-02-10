package pt.uninova.s4h.citizenhub.connectivity.bluetooth.and;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
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
        // TODO: Set current date on device
        getConnection().writeCharacteristic(UUID.fromString("00001810-0000-1000-8000-00805f9b34fb"), UUID.fromString("00002a08-0000-1000-8000-00805f9b34fb"), new byte[]{(byte) 0xe6, 0x07, 0x02, 0x0a, 0x0f, 0x34, 0x37});
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

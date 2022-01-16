package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

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

public class UprightGo2Agent extends BluetoothAgent {

    static public final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo2");

    static private final Set<MeasurementKind> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            MeasurementKind.POSTURE
    )));

    static private final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            UprightGo2PostureProtocol.ID,
            UprightGo2CalibrationProtocol.ID,
            UprightGo2VibrationProtocol.ID
    )));

    public UprightGo2Agent(BluetoothConnection connection) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds, connection);
    }


    @Override
    public Set<MeasurementKind> getSupportedMeasurements() {
        return supportedMeasurementKinds;
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(MeasurementKind kind) {
        System.out.println("UprightGo2Agent.getMeasuringProtocol kind=" + kind);
        if (kind == MeasurementKind.POSTURE) {
            return new UprightGo2PostureProtocol(this.getConnection(), this);
        }

        return null;
    }

    @Override
    public String getName() {
        return "UprightGO2";
    }
}
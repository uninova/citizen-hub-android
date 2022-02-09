package pt.uninova.s4h.citizenhub.connectivity.bluetooth.medx;

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

public class MedXAgent extends BluetoothAgent {

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.medx");

    public static final Set<MeasurementKind> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
            MeasurementKind.LUMBAR_EXTENSION_TRAINING
    )));

    private static final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
            MedXTrainingProtocol.ID
    )));


    public MedXAgent(BluetoothConnection connection) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds, connection);
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(MeasurementKind kind) {
        if (kind == MeasurementKind.LUMBAR_EXTENSION_TRAINING) {
            return new MedXTrainingProtocol(getConnection(), getSampleDispatcher(), this);
        }

        return null;
    }

    @Override
    public String getName() {
        return "MedX";
    }
}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class KbzPostureAgent extends BluetoothAgent {

    static public final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.kbzposture");

    static private final Set<MeasurementKind> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
            MeasurementKind.POSTURE_CORRECT
    )));

    static private final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
            KbzBodyProtocol.ID
    )));

    public KbzPostureAgent(BluetoothConnection connection) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds, connection);
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(MeasurementKind kind) {
        if (kind == MeasurementKind.POSTURE_CORRECT) {
            return new KbzBodyProtocol(this.getConnection(), getSampleDispatcher(), this);
        }

        return null;
    }

    @Override
    public String getName() {
        return "KBZ Posture";
    }
}

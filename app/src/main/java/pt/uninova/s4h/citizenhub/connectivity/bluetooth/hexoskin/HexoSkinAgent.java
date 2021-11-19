package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

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

public class HexoSkinAgent extends BluetoothAgent {

    static public final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin");

    static private final Set<MeasurementKind> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            MeasurementKind.ACTIVITY,
            MeasurementKind.HEART_RATE,
            MeasurementKind.RESPIRATION_RATE
    )));

    static private final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            HexoSkinAccelerometerProtocol.ID,
            HexoSkinHeartRateProtocol.ID,
            HexoSkinRespirationProtocol.ID
    )));

    public HexoSkinAgent(BluetoothConnection connection) {
        super(ID, HexoSkinAgent.supportedProtocolsIds, HexoSkinAgent.supportedMeasurementKinds, connection);
    }

    @Override
    public Set<MeasurementKind> getSupportedMeasurements() {
        return HexoSkinAgent.supportedMeasurementKinds;
    }

    @Override
    public MeasuringProtocol getMeasuringProtocol(MeasurementKind measurementKind) {
        switch (measurementKind) {
            case ACTIVITY:
                return new HexoSkinAccelerometerProtocol(this.getConnection(), this);
            case HEART_RATE:
                return new HexoSkinHeartRateProtocol(this.getConnection(), this);
            case RESPIRATION_RATE:
                return new HexoSkinRespirationProtocol(this.getConnection(), this);
        }

        return null;
    }

    @Override
    public String getName() {
        return "HexoSkin";
    }

}

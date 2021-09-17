package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public class KbzPostureAgent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.kbzposture");

    public KbzPostureAgent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    public KbzPostureAgent() {
        super(ID, null, null);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(KbzRawProtocol.ID, null);

        return protocolMap;
    }

    @Override
    public void disable() {
        for (UUID i : getProtocolIds(ProtocolState.ENABLED)) {
            getProtocol(i).disable();
        }

        getConnection().close();

        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        setState(AgentState.ENABLED);
    }

    @Override
    public List<MeasurementKind> getSupportedMeasurements() {
        List<MeasurementKind> measurementKindList = new ArrayList<>();
        measurementKindList.add(MeasurementKind.POSTURE);

        return measurementKindList;
    }

    @Override
    public void enableMeasurement(MeasurementKind measurementKind, Observer<Measurement> observer) {
        MeasuringProtocol protocol = null;

        if (measurementKind == MeasurementKind.POSTURE) {
            protocol = new KbzRawProtocol(this.getConnection(), this);
        }

        if (protocol != null) {
            protocol.getMeasurementObservers().add(observer);
            enableProtocol(protocol.getId(), protocol);
        }
    }

    @Override
    public void disableMeasurement(MeasurementKind measurementKind) {
        //TODO
    }

    @Override
    public String getName() {
        return "KBZ Posture";
    }
}

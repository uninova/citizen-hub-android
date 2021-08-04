package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public class UpRightGo2Agent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo2");

    public UpRightGo2Agent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    public UpRightGo2Agent() {
        super(ID, null, null);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        System.out.println("FSL - Got here to posture agent");
        protocolMap.put(UpRightGo2Protocol.ID, new UpRightGo2Protocol(connection, UpRightGo2Agent.class));

        return protocolMap;
    }

    @Override
    protected void setState(AgentState value) {
        setState(value, UpRightGo2Agent.class);
    }

    @Override
    public void disable() {
        for (UUID i : getPublicProtocolIds(ProtocolState.ENABLED)) {
            getProtocol(i).disable();
        }

        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        if (getState() == AgentState.ENABLED)
            return;

        UpRightGo2Protocol protocol = (UpRightGo2Protocol) getProtocol(UpRightGo2Protocol.ID);

        protocol.getObservers().add(new Observer<StateChangedMessage<ProtocolState, Class<?>>>() {
            @Override
            public void onChanged(StateChangedMessage<ProtocolState, Class<?>> value) {
                if (value.getNewState() == ProtocolState.ENABLED) {
                    UpRightGo2Agent.this.setState(AgentState.ENABLED);

                    protocol.getObservers().remove(this);
                }
            }
        });

        protocol.enable();
    }

    @Override
    public List<MeasurementKind> getSupportedMeasurements() {
        List<MeasurementKind> measurementKindList = new ArrayList<>();
        measurementKindList.add(MeasurementKind.POSTURE);
        return measurementKindList;
    }

    @Override
    public void enableMeasurement(MeasurementKind measurementKind) {
        if (getState() == AgentState.ENABLED)
            return;
        switch (measurementKind) {
            case POSTURE:
                enable();

                case UNKNOWN:
                break;
            default:
                break;
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
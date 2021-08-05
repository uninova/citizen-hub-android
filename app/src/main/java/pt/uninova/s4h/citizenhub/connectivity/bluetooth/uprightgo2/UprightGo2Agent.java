package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.util.*;

public class UprightGo2Agent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo2");

    public UprightGo2Agent(BluetoothConnection connection) {
        super(ID, createProtocols(connection, UprightGo2Agent.class), connection);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection, Class<?> agent) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();
        //Posture
        protocolMap.put(UprightGo2PostureProtocol.ID, new UprightGo2PostureProtocol(connection, agent));
        //Vibration Settings
        protocolMap.put(UprightGo2PostureProtocol.ID, new UprightGo2VibrationProtocol(connection, agent));
        //Calibration
        protocolMap.put(UprightGo2PostureProtocol.ID, new UprightGo2CalibrationProtocol(connection, agent));

        return protocolMap;
    }

    @Override
    protected void setState(AgentState value) {
        setState(value, UprightGo2Agent.class);
    }

    @Override
    public void disable() {
        for (UUID i : getPublicProtocolIds(ProtocolState.ENABLED)) {
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
        //TODO
        return null;
    }
}
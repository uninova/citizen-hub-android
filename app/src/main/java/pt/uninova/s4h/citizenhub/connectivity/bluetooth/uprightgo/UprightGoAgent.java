package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo;

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

public class UprightGoAgent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo2");

    public UprightGoAgent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    public UpRightGo2Agent() {
        super(ID, null, null);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();
        //Posture
        protocolMap.put(UprightGoPostureProtocol.ID, new UprightGoPostureProtocol(connection));
        //Vibration Settings
        protocolMap.put(UprightGoPostureProtocol.ID, new UprightGoVibrationProtocol(connection));
        //Calibration
        protocolMap.put(UprightGoPostureProtocol.ID, new UprightGoCalibrationProtocol(connection));

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
        getConnection().close();
        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        UprightGoPostureProtocol protocolPosture = (UprightGoPostureProtocol) getProtocol(UprightGoPostureProtocol.ID);
        UprightGoPostureProtocol protocolVibration = (UprightGoPostureProtocol) getProtocol(UprightGoVibrationProtocol.ID);
        UprightGoPostureProtocol protocolCalibration = (UprightGoPostureProtocol) getProtocol(UprightGoCalibrationProtocol.ID);
        protocolPosture.getObservers().add(new Observer<StateChangedMessage<ProtocolState>>() {
            @Override
            public void onChanged(StateChangedMessage<ProtocolState, Class<?>> value) {
                if (value.getNewState() == ProtocolState.ENABLED) {
                    UprightGoAgent.this.setState(AgentState.ENABLED);
                    protocolPosture.getObservers().remove(this);
                }
            }
        });
        protocolPosture.enable();
        protocolCalibration.enable();
        protocolVibration.enable();
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
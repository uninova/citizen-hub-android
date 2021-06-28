package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class MiBand2Agent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2");

    final private static List<MeasurementKind> measurementKindList = Collections.unmodifiableList(Arrays.asList(
            MeasurementKind.HEART_RATE,
            MeasurementKind.DISTANCE,
            MeasurementKind.ACTIVITY,
            MeasurementKind.STEPS,
            MeasurementKind.STEPS_PER_MINUTE,
            MeasurementKind.CALORIES));

    public MiBand2Agent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    public MiBand2Agent() {
        super(ID, null, null);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(MiBand2HeartRateProtocol.ID, new MiBand2HeartRateProtocol(connection));
        protocolMap.put(MiBand2DistanceProtocol.ID, new MiBand2DistanceProtocol(connection));

        return protocolMap;
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
        MiBand2AuthenticationProtocol auth = new MiBand2AuthenticationProtocol(getConnection());

        auth.getObservers().add(value -> {
            if (value.getNewState() == ProtocolState.ENABLED) {
                setState(AgentState.ENABLED);
                //TODO tirar
//                getProtocol(MiBand2HeartRateProtocol.ID).enable();
//                getProtocol(MiBand2DistanceProtocol.ID).enable();
            }
        });

        auth.enable();
    }

    @Override
    public List<MeasurementKind> getSupportedMeasurements() {
        return measurementKindList;
    }

    @Override
    public void enableMeasurement(MeasurementKind measurementKind) {
//        MiBand2AuthenticationProtocol auth = new MiBand2AuthenticationProtocol(getConnection());

        switch (measurementKind) {
            case HEART_RATE:
//                if (auth.getState()==ProtocolState.DISABLED){
//                    auth.enable();
//                }
                getProtocol(MiBand2HeartRateProtocol.ID).enable();
                break;
            case ACTIVITY:
            case STEPS:
            case STEPS_PER_MINUTE:
            case DISTANCE:
            case CADENCE:
            case CALORIES:
//                if (auth.getState()==ProtocolState.DISABLED){
//                    auth.enable();
//                }
                getProtocol(MiBand2DistanceProtocol.ID).enable();
            default:
                break;
        }

        //    public void enable(MeasurementKind) {}
    }

    @Override
    public void disableMeasurement(MeasurementKind measurementKind) {
        switch (measurementKind) {
            case HEART_RATE:
//                if (auth.getState()==ProtocolState.DISABLED){
//                    auth.enable();
//                }
                getProtocol(MiBand2HeartRateProtocol.ID).disable();
                break;
            case ACTIVITY:
            case STEPS:
            case STEPS_PER_MINUTE:
            case DISTANCE:
            case CADENCE:
            case CALORIES:
//                if (auth.getState()==ProtocolState.DISABLED){
//                    auth.enable();
//                }
                getProtocol(MiBand2DistanceProtocol.ID).disable();
            default:
                break;
        }
    }

    @Override
    public String getName() {
        return null;
    }
}

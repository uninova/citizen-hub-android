package pt.uninova.s4h.citizenhub.connectivity.wearos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractAgent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;


public class WearOSAgent extends AbstractAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear");
    private static final String TAG = "WearOSAgent";



    public WearOSAgent(WearOSConnection wearOSConnection) {

        super(ID,createProtocols(wearOSConnection));
    }

    private static Map<UUID, Protocol> createProtocols(WearOSConnection wearOSConnection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();


            protocolMap.put(WearOSHeartRateProtocol.ID, new WearOSHeartRateProtocol(wearOSConnection));
            protocolMap.put(WearOSStepsProtocol.ID, new WearOSStepsProtocol(wearOSConnection));

        return protocolMap;
    }

    @Override
    public void disable() {
        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        setState(AgentState.ENABLED);
        getProtocol(WearOSHeartRateProtocol.ID).enable();
        getProtocol(WearOSStepsProtocol.ID).enable();

    }

    @Override
    public List<MeasurementKind> getSupportedMeasurements() {
        List<MeasurementKind> measurementKindList = new ArrayList<>();
        measurementKindList.add(MeasurementKind.HEART_RATE);
        measurementKindList.add(MeasurementKind.STEPS);
        measurementKindList.add(MeasurementKind.STEPS_PER_MINUTE);

        return measurementKindList;
    }

    @Override
    public void enableMeasurement(MeasurementKind measurementKind) {

        switch (measurementKind) {
            case HEART_RATE:
                setState(AgentState.ENABLED);
                getProtocol(WearOSHeartRateProtocol.ID).enable();
                break;
            case ACTIVITY:
            case STEPS:
            case STEPS_PER_MINUTE:
            case DISTANCE:
                setState(AgentState.ENABLED);
                getProtocol(WearOSStepsProtocol.ID).enable();
                break;
            default:
                break;
        }

    }

    @Override
    public void disableMeasurement(MeasurementKind measurementKind) {
        //TODO
    }

}


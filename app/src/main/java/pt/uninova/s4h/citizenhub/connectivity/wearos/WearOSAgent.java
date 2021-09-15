package pt.uninova.s4h.citizenhub.connectivity.wearos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractAgent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2DistanceProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2HeartRateProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;


public class WearOSAgent extends AbstractAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear");
    private static final String TAG = "WearOSAgent";

    final private WearOSConnection connection;

    public WearOSAgent(WearOSConnection connection) {
        super(ID, createProtocols());

        this.connection = connection;
    }

    private static Map<UUID, Protocol> createProtocols() {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(WearOSHeartRateProtocol.ID, null);
        protocolMap.put(WearOSStepsProtocol.ID, null);

        return protocolMap;
    }

    @Override
    public void disable() {
        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        setState(AgentState.ENABLED);
        //  getProtocol(WearOSHeartRateProtocol.ID).enable();
        //getProtocol(WearOSStepsProtocol.ID).enable();
    }

    @Override
    public List<MeasurementKind> getSupportedMeasurements() {
        List<MeasurementKind> measurementKindList = new ArrayList<>();

        measurementKindList.add(MeasurementKind.HEART_RATE);
        measurementKindList.add(MeasurementKind.ACTIVITY);

        return measurementKindList;
    }

    @Override
    public void enableMeasurement(MeasurementKind measurementKind, Observer<Measurement> observer) {
        if (getState() != AgentState.ENABLED) {
            System.out.println("NAPODESER!");
            //TODO ADD LISTENER FOR WHEN STATE CHANGES AND ENABLE
            return;
        }

        MeasuringProtocol protocol = null;

        switch (measurementKind) {
            case HEART_RATE:
                protocol = new WearOSHeartRateProtocol(this.connection, this);
                break;
            case ACTIVITY:
                protocol = new WearOSStepsProtocol(this.connection, this);
                break;
        }

        if (protocol != null) {
            enableProtocol(protocol.getId(), protocol);
            protocol.getMeasurementObservers().add(observer);
        }
    }

    @Override
    public void disableMeasurement(MeasurementKind measurementKind) {
        switch (measurementKind) {
            case HEART_RATE:
                setState(AgentState.DISABLED);
                getProtocol(WearOSHeartRateProtocol.ID).disable();
            case ACTIVITY:
            case STEPS:
            case STEPS_PER_MINUTE:
            case DISTANCE:
                setState(AgentState.DISABLED);
                getProtocol(WearOSStepsProtocol.ID).disable();
            default:
                break;
        }
    }

    @Override
    public String getName() {
        return "WearOS";
    }

}


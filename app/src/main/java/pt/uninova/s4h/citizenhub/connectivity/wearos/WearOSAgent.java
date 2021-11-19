package pt.uninova.s4h.citizenhub.connectivity.wearos;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractAgent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;


public class WearOSAgent extends AbstractAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear");

    static private final Set<MeasurementKind> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            MeasurementKind.ACTIVITY,
            MeasurementKind.HEART_RATE
    )));

    static private final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            WearOSHeartRateProtocol.ID,
            WearOSStepsProtocol.ID
    )));

    final private WearOSConnection connection;

    public WearOSAgent(WearOSConnection connection) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds);

        this.connection = connection;
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
    public Set<MeasurementKind> getSupportedMeasurements() {
        return supportedMeasurementKinds;
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(MeasurementKind kind) {
        switch (kind) {
            case ACTIVITY:
                return new WearOSStepsProtocol(this.connection, this);
            case HEART_RATE:
                return new WearOSHeartRateProtocol(this.connection, this);
        }

        return null;
    }

    @Override
    public String getName() {
        return "WearOS";
    }

}


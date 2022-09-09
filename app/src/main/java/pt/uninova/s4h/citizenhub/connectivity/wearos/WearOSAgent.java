package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.content.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractAgent;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Connection;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.RoomSettingsManager;
import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;


public class WearOSAgent extends AbstractAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear");

    static private final Set<Integer> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Measurement.TYPE_STEPS_SNAPSHOT,
            Measurement.TYPE_HEART_RATE
    )));

    static private final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            WearOSHeartRateProtocol.ID,
            WearOSStepsProtocol.ID
    )));

    final private WearOSConnection connection;
    CitizenHubService service;

    public WearOSAgent(WearOSConnection connection, CitizenHubService service, Context context) {
        super(ID, new Device(connection.getAddress(), Connection.CONNECTION_KIND_WEAROS), supportedProtocolsIds, supportedMeasurementKinds, new RoomSettingsManager(context, connection.getAddress()));

        this.service = service;
        this.connection = connection;
    }

    @Override
    public void disable() {
        setState(Agent.AGENT_STATE_DISABLED);
        service.getWearOSMessageService().sendMessage("WearOSAgent", "false");
    }

    @Override
    public void enable() {
        setState(Agent.AGENT_STATE_ENABLED);
        service.getWearOSMessageService().sendMessage("WearOSAgent", "true");
    }

    @Override
    public Set<Integer> getSupportedMeasurements() {
        return supportedMeasurementKinds;
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(int kind) {
        switch (kind) {
            case Measurement.TYPE_STEPS_SNAPSHOT:
                return new WearOSStepsProtocol(this.connection, getSampleDispatcher(), this, service);
            case Measurement.TYPE_HEART_RATE:
                return new WearOSHeartRateProtocol(this.connection, getSampleDispatcher(), this, service);
        }

        return null;
    }

    @Override
    public String getName() {
        return "WearOS";
    }

}


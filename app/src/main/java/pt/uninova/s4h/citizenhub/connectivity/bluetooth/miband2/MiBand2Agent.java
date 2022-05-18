package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class MiBand2Agent extends BluetoothAgent {

    static public final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2");

    static private final Set<Integer> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Measurement.TYPE_HEART_RATE,
            Measurement.TYPE_STEPS_SNAPSHOT
    )));

    static private final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            MiBand2HeartRateProtocol.ID,
            MiBand2StepsProtocol.ID
    )));

    public MiBand2Agent(BluetoothConnection connection) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds, connection);
    }

    private void authorize() {
        final MiBand2AuthenticationProtocol auth = new MiBand2AuthenticationProtocol(getConnection(), this);

        auth.addStateObserver(new Observer<StateChangedMessage<Integer, ? extends Protocol>>() {
            @Override
            public void observe(StateChangedMessage<Integer, ? extends Protocol> value) {
                if (value.getNewState() == Protocol.STATE_ENABLED) {
                    MiBand2Agent.this.setState(Agent.AGENT_STATE_ENABLED);
                    auth.removeStateObserver(this);
                }
            }
        });

        auth.enable();
    }

    @Override
    public void enable() {
        authorize();

        addStateObserver(new Observer<StateChangedMessage<Integer, ? extends Agent>>() {
            @Override
            public void observe(StateChangedMessage<Integer, ? extends Agent> value) {
                if (value.getNewState() == AGENT_STATE_ENABLED) {
                    new SetTimeProtocol(getConnection(), MiBand2Agent.this).enable();
                    removeStateObserver(this);
                }
            }
        });
    }

    @Override
    public Set<Integer> getSupportedMeasurements() {
        return supportedMeasurementKinds;
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(int kind) {
        switch (kind) {
            case Measurement.TYPE_HEART_RATE:
                return new MiBand2HeartRateProtocol(this.getConnection(), getSampleDispatcher(), this);
            case Measurement.TYPE_STEPS_SNAPSHOT:
                return new MiBand2StepsProtocol(this.getConnection(), getSampleDispatcher(), this);
        }

        return null;
    }

    @Override
    public String getName() {
        return "MI Band 2";
    }
}
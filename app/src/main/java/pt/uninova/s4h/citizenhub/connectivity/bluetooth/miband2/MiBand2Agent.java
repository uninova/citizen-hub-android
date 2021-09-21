package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public class MiBand2Agent extends BluetoothAgent {

    static public final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2");

    static private final Set<MeasurementKind> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            MeasurementKind.HEART_RATE,
            MeasurementKind.ACTIVITY
    )));

    static private final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            MiBand2DistanceProtocol.ID,
            MiBand2HeartRateProtocol.ID
    )));

    public MiBand2Agent(BluetoothConnection connection) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds, connection);
    }

    @Override
    public void enable() {
        final MiBand2AuthenticationProtocol auth = new MiBand2AuthenticationProtocol(getConnection(), this);

        auth.getObservers().add(new Observer<StateChangedMessage<ProtocolState, ? extends Agent>>() {
            @Override
            public void onChanged(StateChangedMessage<ProtocolState, ? extends Agent> value) {
                if (value.getNewState() == ProtocolState.ENABLED) {
                    auth.getObservers().remove(this);
                    MiBand2Agent.this.setState(AgentState.ENABLED);
                }
            }
        });

        auth.enable();
    }

    @Override
    public Set<MeasurementKind> getSupportedMeasurements() {
        return supportedMeasurementKinds;
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(MeasurementKind kind) {
        switch (kind) {
            case HEART_RATE:
                return new MiBand2HeartRateProtocol(this.getConnection(), this);
            case ACTIVITY:
                return new MiBand2DistanceProtocol(this.getConnection(), this);
        }

        return null;
    }

    @Override
    public void disableMeasurement(MeasurementKind measurementKind) {
        switch (measurementKind) {
            case HEART_RATE:
                getProtocol(MiBand2HeartRateProtocol.ID).disable();
                ((MeasuringProtocol) getProtocol(MiBand2HeartRateProtocol.ID)).getMeasurementObservers().clear();
            case ACTIVITY:
                getProtocol(MiBand2DistanceProtocol.ID).disable();
                ((MeasuringProtocol) getProtocol(MiBand2DistanceProtocol.ID)).getMeasurementObservers().clear();
            default:
                break;
        }

    }

    @Override
    public String getName() {
        return "MI Band 2";
    }
}
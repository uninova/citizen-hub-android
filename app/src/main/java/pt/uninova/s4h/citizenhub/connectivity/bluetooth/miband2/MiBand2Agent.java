package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;
import pt.uninova.util.messaging.Observer;

public class MiBand2Agent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2");

    final private static List<MeasurementKind> measurementKindList = Collections.unmodifiableList(Arrays.asList(
            MeasurementKind.HEART_RATE,
            MeasurementKind.ACTIVITY
    ));

    final private static UUID[] protocols = {
            MiBand2HeartRateProtocol.ID,
            MiBand2DistanceProtocol.ID
    };

    public MiBand2Agent(BluetoothConnection connection) {
        super(ID, createProtocols(), connection);
    }

    public MiBand2Agent() {
        super(ID, null, null);
    }

    private static Map<UUID, Protocol> createProtocols() {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(MiBand2HeartRateProtocol.ID, null);
        protocolMap.put(MiBand2DistanceProtocol.ID, null);

        return protocolMap;
    }

    @Override
    public void disable() {
        for (UUID i : getProtocolIds(ProtocolState.ENABLED)) {
            getProtocol(i).disable();
        }

        getConnection().close();

        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        MiBand2AuthenticationProtocol auth = new MiBand2AuthenticationProtocol(getConnection(), this);

        auth.getObservers().add(value -> {
            if (value.getNewState() == ProtocolState.ENABLED) {
                setState(AgentState.ENABLED);
            }
        });

        auth.enable();
    }

    @Override
    public List<MeasurementKind> getSupportedMeasurements() {
        return measurementKindList;
    }

    @Override
    public void enableMeasurement(MeasurementKind measurementKind, Observer<Measurement> observer) {
        if (getState() != AgentState.ENABLED) {
            final Observer<StateChangedMessage<AgentState, ? extends Agent>> stateObserver = message -> {
                if (message.getNewState() == AgentState.ENABLED) {
                    getObservers().remove(observer);
                    enableMeasurement(measurementKind, observer);
                }
            };

            this.getObservers().add(stateObserver);

            return;
        }

        MeasuringProtocol protocol = null;

        switch (measurementKind) {
            case HEART_RATE:
                protocol = new MiBand2HeartRateProtocol(this.getConnection(), this);
                break;
            case ACTIVITY:
                protocol = new MiBand2DistanceProtocol(this.getConnection(), this);
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

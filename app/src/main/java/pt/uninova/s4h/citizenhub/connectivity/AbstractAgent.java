package pt.uninova.s4h.citizenhub.connectivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public abstract class AbstractAgent implements Agent {

    private final UUID id;
    private final Device source;

    private AgentState state;

    private final Set<UUID> supportedProtocolsIds;
    private final Set<MeasurementKind> supportedMeasurements;

    private final Map<UUID, Protocol> protocolMap;
    private final Map<MeasurementKind, MeasuringProtocol> measurementMap;

    private final Dispatcher<StateChangedMessage<AgentState, ? extends Agent>> stateChangedDispatcher;
    private final Dispatcher<Sample> sampleDispatcher;

    private final Set<AgentListener> agentListenerSet;

    protected AbstractAgent(UUID id, Device source, Set<UUID> supportedProtocolsIds, Set<MeasurementKind> supportedMeasurements) {
        this.id = id;
        this.source = source;

        this.supportedProtocolsIds = supportedProtocolsIds;
        this.supportedMeasurements = supportedMeasurements;

        this.protocolMap = new HashMap<>(supportedProtocolsIds.size());
        this.measurementMap = new HashMap<>(supportedMeasurements.size());

        this.stateChangedDispatcher = new Dispatcher<>();
        this.sampleDispatcher = new Dispatcher<>();

        this.agentListenerSet = new HashSet<>();
    }

    @Override
    public void addAgentListener(AgentListener agentListener) {
        agentListenerSet.add(agentListener);
    }

    @Override
    public void addSampleObserver(Observer<Sample> observer) {
        this.sampleDispatcher.addObserver(observer);
    }

    @Override
    public void addStateObserver(Observer<StateChangedMessage<AgentState, ? extends Agent>> observer) {
        this.stateChangedDispatcher.addObserver(observer);
    }

    @Override
    public void disable() {
        for (final MeasurementKind i : this.measurementMap.keySet()) {
            disableMeasurement(i);
        }

        for (final Protocol i : this.protocolMap.values()) {
            disableProtocol(i);
        }

        setState(AgentState.DISABLED);
    }

    @Override
    public void disableMeasurement(MeasurementKind measurementKind) {
        final MeasuringProtocol protocol = this.measurementMap.get(measurementKind);

        if (protocol != null) {
            this.measurementMap.remove(measurementKind);
            protocol.clearSampleObservers();
            disableProtocol(protocol);
        }

        tellOnMeasurementDisabled(measurementKind);
    }

    @Override
    public void disableProtocol(Protocol protocol) {
        final UUID protocolId = protocol.getId();
        final Protocol currentProtocol = protocolMap.get(protocolId);

        if (currentProtocol != null && currentProtocol == protocol) {
            protocolMap.remove(protocolId);
            protocol.disable();
        }

        tellOnProtocolDisabled(protocol);
    }

    @Override
    public void enable() {
        setState(AgentState.ENABLED);
    }

    @Override
    public void enableMeasurement(MeasurementKind measurementKind) {
        System.out.println("AbstractAgent.enableMeasurement measurementKind=" + measurementKind);

        if (getState() != AgentState.ENABLED) {
            this.addStateObserver(new Observer<StateChangedMessage<AgentState, ? extends Agent>>() {

                @Override
                public void observe(StateChangedMessage<AgentState, ? extends Agent> message) {
                    if (message.getNewState() == AgentState.ENABLED) {
                        AbstractAgent.this.enableMeasurement(measurementKind);
                        removeStateObserver(this);
                    }
                }
            });

            return;
        }

        final MeasuringProtocol protocol = getMeasuringProtocol(measurementKind);

        if (protocol != null) {
            this.measurementMap.put(measurementKind, protocol);
            enableProtocol(protocol);
        }

        tellOnMeasurementEnabled(measurementKind);
    }

    @Override
    public void enableProtocol(Protocol protocol) {
        final UUID protocolId = protocol.getId();

        if (!supportedProtocolsIds.contains(protocolId)) {
            return;
        }

        final Protocol currentProtocol = protocolMap.get(protocolId);

        if (currentProtocol == null || currentProtocol != protocol) {
            protocolMap.put(protocolId, protocol);
        }

        if (currentProtocol != null) {
            disableProtocol(currentProtocol);
        }

        protocol.enable();

        tellOnProtocolEnabled(protocol);
    }

    @Override
    public Set<MeasurementKind> getEnabledMeasurements() {
        return measurementMap.keySet();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Protocol getProtocol(UUID protocolId) {
        final Protocol protocol = protocolMap.get(protocolId);

        if (protocol == null) {
            if (supportedProtocolsIds.contains(protocolId)) {
                return null; //TODO Add exceptions
            }

            return null; //TODO Add factory method
        } else {
            return protocol;
        }
    }

    protected abstract MeasuringProtocol getMeasuringProtocol(MeasurementKind kind);

    protected Dispatcher<Sample> getSampleDispatcher() {
        return sampleDispatcher;
    }

    @Override
    public Device getSource() {
        return source;
    }

    @Override
    public AgentState getState() {
        return state;
    }

    @Override
    public Set<MeasurementKind> getSupportedMeasurements() {
        return this.supportedMeasurements;
    }

    @Override
    public Set<UUID> getSupportedProtocolsIds() {
        return this.supportedProtocolsIds;
    }


    @Override
    public void removeAgentListener(AgentListener agentListener) {
        agentListenerSet.remove(agentListener);
    }

    @Override
    public void removeAllAgentListeners() {
        agentListenerSet.clear();
    }

    @Override
    public void removeSampleObserver(Observer<Sample> observer) {
        this.sampleDispatcher.removeObserver(observer);
    }

    @Override
    public void removeStateObserver(Observer<StateChangedMessage<AgentState, ? extends Agent>> observer) {
        this.stateChangedDispatcher.removeObserver(observer);
    }

    protected void setState(AgentState value) {
        if (state != value) {
            final AgentState oldState = state;

            state = value;

            stateChangedDispatcher.dispatch(new StateChangedMessage<>(value, oldState, this));
        }
    }

    protected void tellOnMeasurementDisabled(MeasurementKind measurementKind) {
        for (AgentListener i : agentListenerSet) {
            i.onMeasurementDisabled(this, measurementKind);
        }
    }

    protected void tellOnMeasurementEnabled(MeasurementKind measurementKind) {
        for (AgentListener i : agentListenerSet) {
            i.onMeasurementEnabled(this, measurementKind);
        }
    }

    protected void tellOnProtocolDisabled(Protocol protocol) {
        for (AgentListener i : agentListenerSet) {
            i.onProtocolDisabled(this, protocol);
        }
    }

    protected void tellOnProtocolEnabled(Protocol protocol) {
        for (AgentListener i : agentListenerSet) {
            i.onProtocolEnabled(this, protocol);
        }
    }
}

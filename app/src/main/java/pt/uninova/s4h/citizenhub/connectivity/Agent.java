package pt.uninova.s4h.citizenhub.connectivity;

import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public interface Agent {

    void addAgentListener(AgentListener agentListener);

    void addSampleObserver(Observer<Sample> observer);

    void addStateObserver(Observer<StateChangedMessage<AgentState, ? extends Agent>> observer);

    void disable();

    void disableMeasurement(MeasurementKind measurementKind);

    void disableProtocol(Protocol protocol);

    void enable();

    void enableMeasurement(MeasurementKind measurementKind);

    void enableProtocol(Protocol protocol);

    UUID getId();

    String getName();

    Protocol getProtocol(UUID protocolId);

    Device getSource();

    AgentState getState();

    Set<MeasurementKind> getEnabledMeasurements();

    Set<MeasurementKind> getSupportedMeasurements();

    Set<UUID> getSupportedProtocolsIds();

    void removeAllAgentListeners();

    void removeAgentListener(AgentListener agentListener);

    void removeSampleObserver(Observer<Sample> observer);

    void removeStateObserver(Observer<StateChangedMessage<AgentState, ? extends Agent>> observer);

}

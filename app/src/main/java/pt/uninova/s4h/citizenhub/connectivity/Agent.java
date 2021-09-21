package pt.uninova.s4h.citizenhub.connectivity;

import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public interface Agent {

    void disable();

    void disableMeasurement(MeasurementKind measurementKind);

    void disableProtocol(Protocol protocol);

    void enable();

    void enableMeasurement(MeasurementKind measurementKind, Observer<Measurement> observer);

    void enableProtocol(Protocol protocol);

    UUID getId();

    String getName();

    Protocol getProtocol(UUID protocolId);

    AgentState getState();

    Set<Observer<StateChangedMessage<AgentState, ? extends Agent>>> getStateObservers();

    Set<MeasurementKind> getSupportedMeasurements();

    Set<UUID> getSupportedProtocolsIds();

}

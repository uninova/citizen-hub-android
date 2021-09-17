package pt.uninova.s4h.citizenhub.connectivity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public interface Agent {

    void disable();

    void enable();

    UUID getId();

    Protocol getProtocol(UUID protocolId);

    Set<UUID> getProtocolIds(ProtocolState state);

    Set<Observer<StateChangedMessage<AgentState, ? extends Agent>>> getObservers();

    List<MeasurementKind> getSupportedMeasurements();

    void enableMeasurement(MeasurementKind measurementKind, Observer<Measurement> observer);

    void disableMeasurement(MeasurementKind measurementKind);

    AgentState getState();

    String getName();

}

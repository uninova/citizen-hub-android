package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.util.messaging.Observer;

import java.util.Set;
import java.util.UUID;

public interface Agent {

    void disable();

    void enable();

    UUID getId();

    Protocol getProtocol(UUID protocolId);

    Set<UUID> getPublicProtocolIds();

    Set<UUID> getPublicProtocolIds(ProtocolState state);

    Set<Observer<StateChangedMessage<AgentState>>> getObservers();

    AgentState getState();

}

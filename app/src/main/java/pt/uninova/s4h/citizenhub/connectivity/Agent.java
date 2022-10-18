package pt.uninova.s4h.citizenhub.connectivity;

import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public interface Agent {

    public static final int AGENT_STATE_DISABLED = 0;
    public static final int AGENT_STATE_ENABLED = 1;
    public static final int AGENT_STATE_INACTIVE = 2;

    void addAgentListener(AgentListener agentListener);

    void addSampleObserver(Observer<Sample> observer);

    void addStateObserver(Observer<StateChangedMessage<Integer, ? extends Agent>> observer);

    void disable();

    void disableMeasurement(int measurementKind);

    void disableProtocol(Protocol protocol);

    void enable();

    void enableMeasurement(int measurementKind);

    void enableProtocol(Protocol protocol);

    UUID getId();

    String getName();

    Protocol getProtocol(UUID protocolId);

    SettingsManager getSettingsManager();

    Device getSource();

    int getState();

    Set<Integer> getEnabledMeasurements();

    Set<Integer> getSupportedMeasurements();

    Set<UUID> getSupportedProtocolsIds();

    Set<UUID> getEnabledProtocols();

    void removeAllAgentListeners();

    void removeAgentListener(AgentListener agentListener);

    void removeSampleObserver(Observer<Sample> observer);

    void removeStateObserver(Observer<StateChangedMessage<Integer, ? extends Agent>> observer);

    List<Fragment> getConfigurationFragments();
}

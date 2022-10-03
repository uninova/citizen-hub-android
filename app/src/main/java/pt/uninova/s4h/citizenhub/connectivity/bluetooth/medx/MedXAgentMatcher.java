package pt.uninova.s4h.citizenhub.connectivity.bluetooth.medx;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AbstractAgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;

public class MedXAgentMatcher extends AbstractAgentMatcher {

    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.unmodifiableList(Arrays.asList(
                LumbarExtensionTrainingProtocol.UUID_SERVICE));
    }

    @Override
    public Class<?> getAgentClass() {
        return MedXAgent.class;
    }

    @Override
    public List<UUID> getAgentServices() {
        return agentServices;
    }
}

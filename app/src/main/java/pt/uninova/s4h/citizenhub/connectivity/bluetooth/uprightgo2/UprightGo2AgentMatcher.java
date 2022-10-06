package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AbstractAgentMatcher;

public class UprightGo2AgentMatcher extends AbstractAgentMatcher {

    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.unmodifiableList(Arrays.asList(
                UprightGo2CalibrationProtocol.MEASUREMENTS_SERVICE
        ));
    }

    @Override
    public Class<?> getAgentClass() {
        return UprightGo2Agent.class;
    }

    @Override
    public List<UUID> getAgentServices() {
        return agentServices;
    }
}

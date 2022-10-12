package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AbstractAgentMatcher;

public class KbzPostureAgentMatcher extends AbstractAgentMatcher {

    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.unmodifiableList(Arrays.asList(
                KbzBodyProtocol.KBZ_SERVICE));
    }

    @Override
    public Class<?> getAgentClass() {
        return KbzPostureAgent.class;
    }

    @Override
    public List<UUID> getAgentServices() {
        return agentServices;
    }
}

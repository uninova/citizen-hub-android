package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import static pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent.UUID_SERVICE_HEART_RATE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AbstractAgentMatcher;

public class MiBand2AgentMatcher extends AbstractAgentMatcher {
    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.unmodifiableList(Arrays.asList(
                MiBand2Agent.UUID_MEMBER_ANHUI_HUAMI_INFORMATION_TECHNOLOGY_CO_LTD_1,
                MiBand2Agent.XIAOMI_MIBAND2_SERVICE_AUTH,
                UUID_SERVICE_HEART_RATE));
    }

    @Override
    public Class<?> getAgentClass() {
        return MiBand2Agent.class;
    }

    @Override
    public List<UUID> getAgentServices() {
        return agentServices;
    }
}

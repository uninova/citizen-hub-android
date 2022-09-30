package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2Agent;

public class MiBand2AgentMatcher implements AgentMatcher {
    private static final List<UUID> agentServices;
    static {
        agentServices = Collections.unmodifiableList(Arrays.asList(
                MiBand2Agent.UUID_MEMBER_ANHUI_HUAMI_INFORMATION_TECHNOLOGY_CO_LTD_1,
                MiBand2Agent.XIAOMI_MIBAND2_SERVICE_AUTH,
                MiBand2Agent.UUID_SERVICE_HEART_RATE));
    }

    @Override
    public boolean doesMatch(BluetoothConnection connection) {
        boolean doesMatch = true;
        for (UUID service: agentServices
             ) {
            if(connection.hasService(service)){
                System.out.println("Agent " + getAgentClass() + "Has service: " + service);
            }
            else{
                System.out.println("Agent " + getAgentClass() + "DOESN'T HAVE service: " + service);
                doesMatch = false;
            }
            System.out.println("Match? " + doesMatch);
        }
        return doesMatch;
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

package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2AgentMatcher;

public class BluetoothAgentMatchers {

    private static final List<AgentMatcher> agentList = new ArrayList<>();
    private static final MiBand2AgentMatcher miBand2AgentMatcher = new MiBand2AgentMatcher();
    private final BluetoothConnection connection;

    public BluetoothAgentMatchers(BluetoothConnection connection) {
        this.connection = connection;
        agentList.add(miBand2AgentMatcher);
    }

    public Class<?> runAgentMatchers() {
        Class<?> matchedAgent = null;
        for (AgentMatcher agent : agentList
        ) {
            if (agent.doesMatch(connection)) {
                matchedAgent = agent.getAgentClass();
            }
        }
        return matchedAgent;
    }
}

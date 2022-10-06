package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.List;
import java.util.UUID;

public abstract class AbstractAgentMatcher implements AgentMatcher {
    List<UUID> agentServices;
    public AbstractAgentMatcher() {
    }

    @Override
    public boolean doesMatch(BluetoothConnection connection,List<UUID> agentServices) {
        boolean doesMatch = true;
        for (UUID service : agentServices
        ) {
            if (!connection.hasService(service)) {
                return false;
            }
        }
        System.out.println("Match? " + doesMatch);
        return doesMatch;
    }

    @Override
    public Class<?> getAgentClass() {
        return null;
    }

    @Override
    public List<UUID> getAgentServices() {
        return agentServices;
    }
}

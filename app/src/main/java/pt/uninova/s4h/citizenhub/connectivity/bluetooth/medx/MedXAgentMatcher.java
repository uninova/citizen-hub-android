package pt.uninova.s4h.citizenhub.connectivity.bluetooth.medx;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;

public class MedXAgentMatcher implements AgentMatcher {

    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.singletonList(
                LumbarExtensionTrainingProtocol.UUID_SERVICE);
    }

    @Override
    public boolean doesMatch(BluetoothConnection connection) {
        boolean doesMatch = true;
        for (UUID service : agentServices
        ) {
            if (connection.hasService(service)) {
                System.out.println("Agent " + getAgentClass() + "Has service: " + service);
            } else {
                System.out.println("Agent " + getAgentClass() + "DOESN'T HAVE service: " + service);
                doesMatch = false;
            }
            System.out.println("MedX Match? " + doesMatch);
        }
        return doesMatch;
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

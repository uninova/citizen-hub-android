package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;

public class UprightGo2AgentMatcher implements AgentMatcher {

    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.unmodifiableList(Arrays.asList(
                UprightGo2CalibrationProtocol.MEASUREMENTS_SERVICE,
                UprightGo2PostureProtocol.MEASUREMENTS_SERVICE,
                UprightGo2VibrationProtocol.VIBRATION_SERVICE));
        //TODO same UUID's?
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
            System.out.println("Upright Match? " + doesMatch);
        }
        return doesMatch;
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

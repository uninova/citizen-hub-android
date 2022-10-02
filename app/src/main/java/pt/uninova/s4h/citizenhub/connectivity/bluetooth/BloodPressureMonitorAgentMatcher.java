package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import static pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent.UUID_SERVICE_BLOOD_PRESSURE;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.and.BloodPressureMonitorAgent;

public class BloodPressureMonitorAgentMatcher implements AgentMatcher {

    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.singletonList(
                UUID_SERVICE_BLOOD_PRESSURE);
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
            System.out.println(" Blood Pressure Match? " + doesMatch);
        }
        return doesMatch;
    }

    @Override
    public Class<?> getAgentClass() {
        return BloodPressureMonitorAgent.class;
    }

    @Override
    public List<UUID> getAgentServices() {
        return agentServices;
    }
}

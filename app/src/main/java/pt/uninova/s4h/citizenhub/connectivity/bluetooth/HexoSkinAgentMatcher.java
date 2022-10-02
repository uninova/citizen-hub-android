package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinAccelerometerProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinHeartRateProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinRespirationProtocol;

public class HexoSkinAgentMatcher implements AgentMatcher {

    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.unmodifiableList(Arrays.asList(
                HexoSkinAccelerometerProtocol.ACCELEROMETER_SERVICE_UUID,
                HexoSkinRespirationProtocol.RESPIRATION_SERVICE_UUID,
                HexoSkinHeartRateProtocol.UUID_SERVICE_HEART_RATE,
                HexoSkinRespirationProtocol.RESPIRATION_SERVICE_UUID));
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
            System.out.println("HexoSkin Match? " + doesMatch);
        }
        return doesMatch;
    }

    @Override
    public Class<?> getAgentClass() {
        return HexoSkinAgent.class;
    }

    @Override
    public List<UUID> getAgentServices() {
        return agentServices;
    }
}

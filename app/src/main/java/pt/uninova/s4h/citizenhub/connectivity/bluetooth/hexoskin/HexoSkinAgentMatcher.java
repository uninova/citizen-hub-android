package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import static pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinHeartRateProtocol.UUID_SERVICE_HEART_RATE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AbstractAgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;

public class HexoSkinAgentMatcher extends AbstractAgentMatcher {

    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.unmodifiableList(Arrays.asList(
                HexoSkinAccelerometerProtocol.ACCELEROMETER_SERVICE_UUID,
                HexoSkinRespirationProtocol.RESPIRATION_SERVICE_UUID,
                UUID_SERVICE_HEART_RATE,
                HexoSkinRespirationProtocol.RESPIRATION_SERVICE_UUID));
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

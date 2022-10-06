package pt.uninova.s4h.citizenhub.connectivity.bluetooth.and;

import static pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent.UUID_SERVICE_BLOOD_PRESSURE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AbstractAgentMatcher;

public class BloodPressureMonitorAgentMatcher extends AbstractAgentMatcher {

    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.unmodifiableList(Arrays.asList(
                UUID_SERVICE_BLOOD_PRESSURE));
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

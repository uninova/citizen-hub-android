package pt.uninova.s4h.citizenhub.connectivity.bluetooth.digitsole;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AbstractAgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;

public class DigitSoleAgentMatcher extends AbstractAgentMatcher {

    private static final List<UUID> agentServices;

    static {
        agentServices = Collections.unmodifiableList(Arrays.asList(
                DigitsoleActivityProtocol.UUID_SERVICE_DATA));
    }

    @Override
    public Class<?> getAgentClass() {
        return DigitsoleAgent.class;
    }

    @Override
    public List<UUID> getAgentServices() {
        return agentServices;
    }
}

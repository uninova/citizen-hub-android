package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.List;
import java.util.UUID;

public interface AgentMatcher {

    boolean doesMatch(BluetoothConnection connection);

    Class<?> getAgentClass();

    List<UUID> getAgentServices();

}

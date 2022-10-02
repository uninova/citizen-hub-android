package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.and.BloodPressureMonitorAgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.digitsole.DigitSoleAgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinAgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.medx.MedXAgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2AgentMatcher;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2AgentMatcher;

public class BluetoothAgentMatchers {

    private static final MiBand2AgentMatcher miBand2AgentMatcher = new MiBand2AgentMatcher();
    private static final BloodPressureMonitorAgentMatcher bloodPressureMonitorAgentMatcher = new BloodPressureMonitorAgentMatcher();
    private static final DigitSoleAgentMatcher digitSoleAgentMatcher = new DigitSoleAgentMatcher();
    private static final HexoSkinAgentMatcher hexoSkinAgentMatcher = new HexoSkinAgentMatcher();
    private static final MedXAgentMatcher medXAgentMatcher = new MedXAgentMatcher();
    private static final UprightGo2AgentMatcher uprightGo2AgentMatcher = new UprightGo2AgentMatcher();
    private static final List<AgentMatcher> agentList;

    static {
        agentList = Collections.unmodifiableList(Arrays.asList(miBand2AgentMatcher,
                bloodPressureMonitorAgentMatcher,
                digitSoleAgentMatcher,
                hexoSkinAgentMatcher,
                medXAgentMatcher,
                uprightGo2AgentMatcher));
    }

    private final BluetoothConnection connection;

    public BluetoothAgentMatchers(BluetoothConnection connection) {
        this.connection = connection;
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

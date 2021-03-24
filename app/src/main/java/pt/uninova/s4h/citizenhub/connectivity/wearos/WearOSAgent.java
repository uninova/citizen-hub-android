package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import pt.uninova.s4h.citizenhub.connectivity.AbstractAgent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;


public class WearOSAgent extends AbstractAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear");
    private static final String TAG = "WearOSAgent";



    public WearOSAgent(WearOSConnection wearOSConnection) {

        super(ID,createProtocols(wearOSConnection));
    }

    private static Map<UUID, Protocol> createProtocols(WearOSConnection wearOSConnection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();


            protocolMap.put(WearOSHeartRateProtocol.ID, new WearOSHeartRateProtocol(wearOSConnection));
            protocolMap.put(WearOSStepsProtocol.ID, new WearOSStepsProtocol(wearOSConnection));

        return protocolMap;
    }

    @Override
    public void disable() {
        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        setState(AgentState.ENABLED);
        getProtocol(WearOSHeartRateProtocol.ID).enable();
        getProtocol(WearOSStepsProtocol.ID).enable();

    }

}


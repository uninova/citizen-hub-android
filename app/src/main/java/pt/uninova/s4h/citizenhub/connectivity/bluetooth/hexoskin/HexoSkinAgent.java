package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2DistanceProtocol;

public class HexoSkinAgent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin");

    public HexoSkinAgent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(HexoSkinHeartRateProtocol.ID, new HexoSkinHeartRateProtocol(connection));
        protocolMap.put(HexoSkinAccelerometerProtocol.ID, new HexoSkinAccelerometerProtocol(connection));
          protocolMap.put(HexoSkinRespirationProtocol.ID, new HexoSkinRespirationProtocol(connection));

        return protocolMap;
    }

    @Override
    public void disable() {
    }

    @Override
    public void enable() {
        //TODO setState enable
    }
}

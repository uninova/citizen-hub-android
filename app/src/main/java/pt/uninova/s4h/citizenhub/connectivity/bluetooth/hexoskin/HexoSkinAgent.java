package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class HexoSkinAgent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin");
    public HexoSkinAgent(BluetoothConnection connection) {
        super(ID, createProtocols(connection, HexoSkinAgent.class), connection);
    }

    public HexoSkinAgent() {
        super(ID, null, null);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection, Class<?> agent) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(HexoSkinHeartRateProtocol.ID, new HexoSkinHeartRateProtocol(connection, agent));
        protocolMap.put(HexoSkinAccelerometerProtocol.ID, new HexoSkinAccelerometerProtocol(connection, agent));
        protocolMap.put(HexoSkinRespirationProtocol.ID, new HexoSkinRespirationProtocol(connection, agent));

        return protocolMap;
    }

    @Override
    public void disable() {
        for (UUID i : getPublicProtocolIds(ProtocolState.ENABLED)) {
            getProtocol(i).disable();
        }

        getConnection().close();

        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        setState(AgentState.ENABLED);
    }


    @Override
    public List<MeasurementKind> getSupportedMeasurements() {
        List<MeasurementKind> measurementKindList = new ArrayList<>();
        measurementKindList.add(MeasurementKind.HEART_RATE);
        measurementKindList.add(MeasurementKind.RESPIRATION_RATE);
        measurementKindList.add(MeasurementKind.ACTIVITY);

        return measurementKindList;
    }


    @Override
    public void enableMeasurement(MeasurementKind measurementKind) {
        switch (measurementKind) {
            case HEART_RATE:
                getProtocol(HexoSkinHeartRateProtocol.ID).enable();
                break;
            case RESPIRATION_RATE:
                getProtocol(HexoSkinRespirationProtocol.ID).enable();
                break;
            case ACTIVITY:
                getProtocol(HexoSkinAccelerometerProtocol.ID).enable();
                break;
            default:
                break;
        }
    }

    @Override
    public void disableMeasurement(MeasurementKind measurementKind) {
        switch (measurementKind) {
            case HEART_RATE:
                getProtocol(HexoSkinHeartRateProtocol.ID).disable();
                break;
            case RESPIRATION_RATE:
                getProtocol(HexoSkinRespirationProtocol.ID).disable();
                break;
            case ACTIVITY:
                getProtocol(HexoSkinAccelerometerProtocol.ID).disable();
                break;

            default:
                break;
        }
    }

    @Override
    public String getName() {
        return "HexoSkinAgent";
    }

    @Override
    protected void setState(AgentState value) {
        super.setState(value, HexoSkinAgent.class);
    }

}

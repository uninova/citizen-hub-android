package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

import java.util.*;

public class HexoSkinAgent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin");

    public HexoSkinAgent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    public HexoSkinAgent() {
        super(ID, null, null);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(HexoSkinHeartRateProtocol.ID, null);
        protocolMap.put(HexoSkinAccelerometerProtocol.ID, null);
        protocolMap.put(HexoSkinRespirationProtocol.ID, null);

        return protocolMap;
    }

    @Override
    public void disable() {
        for (UUID i : getProtocolIds(ProtocolState.ENABLED)) {
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
    public void enableMeasurement(MeasurementKind measurementKind, Observer<Measurement> observer) {
        MeasuringProtocol protocol = null;

        switch (measurementKind) {
            case HEART_RATE:
                protocol = new HexoSkinHeartRateProtocol(this.getConnection(), this);
                break;
            case RESPIRATION_RATE:
                protocol = new HexoSkinRespirationProtocol(this.getConnection(), this);
                break;
            case ACTIVITY:
                protocol = new HexoSkinAccelerometerProtocol(this.getConnection(), this);
                break;
        }

        if (protocol != null) {
            enableProtocol(protocol.getId(), protocol);
            protocol.getMeasurementObservers().add(observer);
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
        return "HexoSkin";
    }

}

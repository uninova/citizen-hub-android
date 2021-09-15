package pt.uninova.s4h.citizenhub.connectivity.bluetooth.placebo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2DistanceProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2HeartRateProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public class PlaceboAgent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2");

    public PlaceboAgent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(MiBand2HeartRateProtocol.ID, new MiBand2HeartRateProtocol(connection, null));
        protocolMap.put(MiBand2DistanceProtocol.ID, new MiBand2DistanceProtocol(connection, null));

        return protocolMap;
    }

    @Override
    public void disable() {
    }

    @Override
    public void enable() {

    }

    @Override
    public List<MeasurementKind> getSupportedMeasurements() {

        List<MeasurementKind> measurementKindList = new ArrayList<>();
        measurementKindList.add(MeasurementKind.HEART_RATE);
        measurementKindList.add(MeasurementKind.ACTIVITY);

        return measurementKindList;
    }


    @Override
    public void disableMeasurement(MeasurementKind measurementKind) {

    }

    public void enableMeasurement(MeasurementKind measurementKind, Observer<Measurement> observer) {
        switch (measurementKind) {
            case HEART_RATE:
                getProtocol(MiBand2HeartRateProtocol.ID).enable();
                break;
            case ACTIVITY:
                getProtocol(MiBand2DistanceProtocol.ID).enable();
            default:
                break;
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    protected void setState(AgentState value) {
        setState(value);
    }
}

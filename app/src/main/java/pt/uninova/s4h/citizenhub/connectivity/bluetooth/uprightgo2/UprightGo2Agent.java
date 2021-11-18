package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinHeartRateProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzRawProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2DistanceProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2HeartRateProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

import java.util.*;

public class UprightGo2Agent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo2");

    public UprightGo2Agent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();
        //Posture
        protocolMap.put(UprightGo2PostureProtocol.ID, null);
        //Vibration Settings
        protocolMap.put(UprightGo2CalibrationProtocol.ID, null);
        //Calibration
        protocolMap.put(UprightGo2VibrationProtocol.ID, null);

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
        measurementKindList.add(MeasurementKind.POSTURE);
        return measurementKindList;
    }

    @Override
    public void enableMeasurement(MeasurementKind measurementKind, Observer<Measurement> observer) {
        MeasuringProtocol protocol = null;
        MeasuringProtocol protocolCalibration = null;

        switch (measurementKind) {
            case POSTURE:
                //protocol = new UprightGo2PostureProtocol(this.getConnection(), this);
                protocolCalibration = new UprightGo2CalibrationProtocol(this.getConnection(), this);
                break;
        }

        if (protocol != null) {
            protocol.getMeasurementObservers().add(observer);
            enableProtocol(protocol.getId(), protocol);
        }
        if (protocolCalibration != null) {
            protocolCalibration.getMeasurementObservers().add(observer);
            enableProtocol(protocolCalibration.getId(), protocolCalibration);
        }
    }

    @Override
    public void disableMeasurement(MeasurementKind measurementKind) {

        switch (measurementKind) {
            case POSTURE:
                getProtocol(UprightGo2PostureProtocol.ID).disable();
                ((MeasuringProtocol) getProtocol(UprightGo2PostureProtocol.ID)).getMeasurementObservers().clear();
                getProtocol(UprightGo2CalibrationProtocol.ID).disable();
                ((MeasuringProtocol) getProtocol(UprightGo2CalibrationProtocol.ID)).getMeasurementObservers().clear();
            default:
                break;
        }
    }

    @Override
    public String getName() {
        return "UprightGO2";
    }
}
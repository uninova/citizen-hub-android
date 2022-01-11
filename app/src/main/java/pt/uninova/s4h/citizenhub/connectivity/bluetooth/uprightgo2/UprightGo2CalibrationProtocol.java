package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;

import java.util.UUID;

public class UprightGo2CalibrationProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo2.calibration");
    final public static UUID MEASUREMENTS_SERVICE = UUID.fromString("0000bac0-0000-1000-8000-00805f9b34fb"); //bac0
    final private static UUID TRIGGER_CALIBRATION = UUID.fromString("0000bac1-0000-1000-8000-00805f9b34fb"); //bac1
    private byte[] calibrationTrigger = {0x00};

    public UprightGo2CalibrationProtocol(UprightGo2Agent agent) {
        super(ID, agent.getConnection(), agent);
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
    }

    @Override
    public void enable() {
        final BluetoothConnection connection = getConnection();
        //calibrate for current position (current position is considered good posture)
        connection.writeCharacteristic(MEASUREMENTS_SERVICE, TRIGGER_CALIBRATION, calibrationTrigger);

        super.enable();
    }
}
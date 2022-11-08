package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothProtocol;

public class UprightGo2CalibrationProtocol extends BluetoothProtocol {

    private final BaseCharacteristicListener characteristicListener = new BaseCharacteristicListener(MEASUREMENTS_SERVICE, TRIGGER_CALIBRATION) {
        @Override
        public void onWrite(byte[] value) {
            setState(Protocol.STATE_COMPLETED);
            getConnection().removeCharacteristicListener(this);
        }

        @Override
        public void onWriteFailure(byte[] value, int status) {
            setState(Protocol.STATE_FAILED);
            getConnection().removeCharacteristicListener(this);
        }
    };

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo2.calibration");
    final public static UUID MEASUREMENTS_SERVICE = UUID.fromString("0000bac0-0000-1000-8000-00805f9b34fb"); //bac0
    final static UUID TRIGGER_CALIBRATION = UUID.fromString("0000bac1-0000-1000-8000-00805f9b34fb"); //bac1
    private final byte[] calibrationTrigger = {0x00};

    public UprightGo2CalibrationProtocol(UprightGo2Agent agent) {
        super(ID, agent.getConnection(), agent);

    }

    @Override
    public void disable() {
        setState(Protocol.STATE_DISABLED);
    }

    @Override
    public void enable() {
        final BluetoothConnection connection = getConnection();
        //calibrate for current position (current position is considered good posture)
        connection.addCharacteristicListener(characteristicListener);
        connection.writeCharacteristic(MEASUREMENTS_SERVICE, TRIGGER_CALIBRATION, calibrationTrigger);
        super.enable();
    }
}
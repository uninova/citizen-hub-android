package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;

import java.util.UUID;

public class UprightGo2VibrationProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo2.vibration");
    final private static UUID VIBRATION_SERVICE = UUID.fromString("0000bab0-0000-1000-8000-00805f9b34fb"); //bab0
    final private static UUID VIBRATION_INTERVAL_CHARACTERISTIC = UUID.fromString("0000bab2-0000-1000-8000-00805f9b34fb"); //bab2
    final private static UUID VIBRATION_CHARACTERISTIC = UUID.fromString("0000bab5-0000-1000-8000-00805f9b34fb"); //bab5
    private byte[] vibrationON = {0x00, 0x00};
    private byte[] vibrationOFF = {0x01, 0x01};

    public UprightGo2VibrationProtocol(BluetoothConnection connection, UprightGo2Agent agent) {
        super(ID, connection, agent);
    }

    private void attachObservers() {
        final BluetoothConnection connection = getConnection();

        //setting up the vibration (initial), configure as needed
        //write vibration on
        connection.writeCharacteristic(VIBRATION_SERVICE, VIBRATION_CHARACTERISTIC, vibrationON);
        //connection.writeCharacteristic(VIBRATION_SERVICE,VIBRATION_CHARACTERISTIC,vibrationOFF);
        //write vibration parameters/settings
        connection.writeCharacteristic(VIBRATION_SERVICE, VIBRATION_INTERVAL_CHARACTERISTIC,
                vibrationMessage(1, 15, false, 0, 3));

        System.out.println("ENTERED VIBRATION PROTOCOL!");
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void enable() {
        attachObservers();
        super.enable();
    }

    private byte[] vibrationMessage(int angle, int interval, boolean showPattern, int pattern, int strength) {
        byte[] message = new byte[15];

        //angle: can be 1 to 6, 1 is the most strict, 6 is the most relaxed
        if (angle < 1 || angle > 6) { //default: 1
            message[0] = 0x01;
        } else {
            message[0] = (byte) angle;
        }
        //interval: can be 5, 15, 30 or 60 seconds
        if (interval == 5) {
            message[1] = 0x32;
            message[2] = 0x00;
        } else if (interval == 15) {
            message[1] = (byte) 0x96;
            message[2] = 0x00;
        } else if (interval == 30) {
            message[1] = 0x2c;
            message[2] = 0x01;
        } else if (interval == 60) {
            message[1] = 0x58;
            message[2] = 0x02;
        } else { //default: 5 seconds
            message[1] = 0x32;
            message[2] = 0x00;
        }
        //pattern: 2 numbers
        //0-only setup pattern, 8-sensor vibrates pattern and setup (used in upright app settings)
        //0-long,1-medium,2-short,3-rampup,4-knockknock,5-heartbeat,6-tuktuk,7-ecstatic,8-muzzle
        if (showPattern) {
            message[3] = (byte) (((byte) 0x80) + pattern);
        } else {
            message[3] = (byte) (((byte) 0x00) + pattern);
        }
        //strength: 1 to 3, vibration strength, 1 -> gentle, 2 -> medium, 3 -> strong
        if (strength == 1) {
            message[4] = 0x46;
        } else if (strength == 2) {
            message[4] = 0x23;
        } else if (strength == 3) {
            message[4] = 0x01;
        } else { //default: 1
            message[4] = 0x46;
        }
        //still undefined here, same for all, complete later if needed TODO
        message[5] = 0x02;
        message[6] = 0x64;
        message[7] = 0x00;
        message[8] = 0x12;
        message[9] = 0x16;
        message[10] = 0x01;
        message[11] = 0x13;
        message[12] = 0x06;
        message[13] = 0x2c;
        message[14] = 0x01;

        return message;
    }
}

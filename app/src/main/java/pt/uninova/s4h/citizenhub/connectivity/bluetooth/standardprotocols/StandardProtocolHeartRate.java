package pt.uninova.s4h.citizenhub.connectivity.bluetooth.standardprotocols;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class StandardProtocolHeartRate extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.standard.heartrate");
    private static UUID UUID_HEARTRATE_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"); //180d
    private static UUID UUID_HEARTRATE_CHARACTERISTIC = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb"); //2a37

    public StandardProtocolHeartRate(BluetoothConnection connection, BluetoothAgent agent) {
        super(ID, connection, agent);
    }

    private void attachObservers() {
        final BluetoothConnection connection = getConnection();

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_HEARTRATE_SERVICE,UUID_HEARTRATE_CHARACTERISTIC){
            @Override
            public void onChange(byte[] value){
                ByteBuffer byteBuffer = ByteBuffer.wrap(value).asReadOnlyBuffer();
                final double[] parsed = new double[]{
                        byteBuffer.get(0) & 0xFF, byteBuffer.get(1)
                };
                double heartRate = parsed[1];
                System.out.println("Heart Rate measured: " + heartRate + " bpm.");
                //getSampleDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.HEART_RATE, heartRate));
            }
        });

        if(connection.hasService(UUID_HEARTRATE_SERVICE)){
            connection.enableNotifications(UUID_HEARTRATE_SERVICE, UUID_HEARTRATE_CHARACTERISTIC);
            System.out.println("Notifications enabled for: " + UUID_HEARTRATE_CHARACTERISTIC.toString());
        }
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
    }

    @Override
    public void enable() {
        attachObservers();
        super.enable();
    }
}
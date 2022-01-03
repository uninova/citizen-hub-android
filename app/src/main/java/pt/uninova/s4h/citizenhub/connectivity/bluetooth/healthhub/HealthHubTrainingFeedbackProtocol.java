package pt.uninova.s4h.citizenhub.connectivity.bluetooth.healthhub;

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

public class HealthHubTrainingFeedbackProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.healthhub.trainingfeedback");
    private static final UUID UUID_TRAININGFEEDBACK_SERVICE = UUID.fromString("5a46791b-516e-48fd-9d29-a2f18d520aec");
    private static final UUID UUID_TRAININGFEEDBACK_CHARACTERISTIC = UUID.fromString("38fde8b6-9664-4b8e-8b3a-e52b8809a64c");

    public HealthHubTrainingFeedbackProtocol(BluetoothConnection connection, BluetoothAgent agent) {
        super(ID, connection, agent);
    }

    private void attachObservers() {
        final BluetoothConnection connection = getConnection();

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_TRAININGFEEDBACK_SERVICE,UUID_TRAININGFEEDBACK_CHARACTERISTIC){
            @Override
            public void onChange(byte[] value){
                ByteBuffer byteBuffer = ByteBuffer.wrap(value).asReadOnlyBuffer();
                final double[] parsed = new double[]{
                        byteBuffer.get(0) & 0xFF,
                        byteBuffer.get(1) & 0xFF,
                        byteBuffer.get(2) & 0xFF

                };
                double heartRate = parsed[0];
                System.out.println("Heart Rate measured: " + heartRate + " bpm.");
                getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.HEART_RATE, heartRate));
                heartRate = parsed[1];
                System.out.println("Heart Rate measured: " + heartRate + " bpm.");
                getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.HEART_RATE, heartRate));
                heartRate = parsed[2];
                System.out.println("Heart Rate measured: " + heartRate + " bpm.");
                getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.HEART_RATE, heartRate));
            }
        });

        if(connection.hasService(UUID_TRAININGFEEDBACK_SERVICE)){
            connection.enableNotifications(UUID_TRAININGFEEDBACK_SERVICE, UUID_TRAININGFEEDBACK_CHARACTERISTIC);
            System.out.println("Notifications enabled for: " + UUID_TRAININGFEEDBACK_CHARACTERISTIC.toString());
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
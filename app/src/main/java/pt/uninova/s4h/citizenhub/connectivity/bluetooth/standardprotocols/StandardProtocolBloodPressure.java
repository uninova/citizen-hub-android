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
import pt.uninova.s4h.citizenhub.data.HeartRateMeasurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class StandardProtocolBloodPressure extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.standard.heartrate");
    private static UUID UUID_BLOODPRESSURE_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"); //1810
    private static UUID UUID_BLOODPRESSURE_CHARACTERISTIC = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb"); //2a35

    public StandardProtocolBloodPressure(BluetoothConnection connection, BluetoothAgent agent) {
        super(ID, connection, agent);
    }

    private void attachObservers() {
        final BluetoothConnection connection = getConnection();

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_BLOODPRESSURE_SERVICE,UUID_BLOODPRESSURE_CHARACTERISTIC){
            @Override
            public void onChange(byte[] value){
                ByteBuffer byteBuffer = ByteBuffer.wrap(value).asReadOnlyBuffer();
                final double[] parsed = new double[]{
                        byteBuffer.get(0) & 0xFF, byteBuffer.get(1) & 0xFF, byteBuffer.get(2) & 0xFF,
                        byteBuffer.get(3) & 0xFF, byteBuffer.get(4) & 0xFF, byteBuffer.get(5) & 0xFF,
                        byteBuffer.get(6) & 0xFF, byteBuffer.get(7) & 0xFF, byteBuffer.get(8) & 0xFF,
                        byteBuffer.get(9) & 0xFF, byteBuffer.get(10) & 0xFF, byteBuffer.get(11) & 0xFF,
                        byteBuffer.get(12) & 0xFF, byteBuffer.get(13) & 0xFF, byteBuffer.get(14) & 0xFF,
                        byteBuffer.get(15) & 0xFF, byteBuffer.get(16) & 0xFF, byteBuffer.get(17) & 0xFF
                };
                double systolic = parsed[1];
                double diastolic = parsed[3];
                double meanAP = parsed[5];
                int timeHours = (int) parsed[11];
                int timeMinutes = (int) parsed[12];
                int timeSeconds = (int) parsed[13];
                double pulse = parsed[14];
                //TODO: there is more info on this message, can be improved later.

                System.out.println("Blood Pressure measured: ");
                System.out.println("Systolic: " + systolic + " mmHg");
                System.out.println("Diastolic: " + diastolic + " mmHg");
                System.out.println("Mean AP: " + meanAP + " mmHg");
                System.out.println("Time: " + timeHours + ":" + timeMinutes + ":" + timeSeconds);
                System.out.println("Pulse: " + pulse + " bpm");

                //TODO: only dispaching heart rate. implement other measurements kinds.
                /*final Sample sample = new Sample(getAgent().getSource()                        );
                getSampleDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.HEART_RATE, pulse));
                getSampleDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.BLOOD_PRESSURE_SBP, systolic));
                getSampleDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.BLOOD_PRESSURE_DBP, diastolic));
                getSampleDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.BLOOD_PRESSURE_MEAN_AP, meanAP));*/
            }
        });

        if(connection.hasService(UUID_BLOODPRESSURE_SERVICE)){
            connection.enableNotifications(UUID_BLOODPRESSURE_SERVICE,UUID_BLOODPRESSURE_CHARACTERISTIC);
            System.out.println("Notifications enabled for: " + UUID_BLOODPRESSURE_SERVICE.toString());
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
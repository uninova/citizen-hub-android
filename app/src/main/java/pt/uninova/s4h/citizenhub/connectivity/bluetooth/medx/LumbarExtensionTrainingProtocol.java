package pt.uninova.s4h.citizenhub.connectivity.bluetooth.medx;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.data.CaloriesMeasurement;
import pt.uninova.s4h.citizenhub.data.LumbarExtensionTrainingMeasurement;
import pt.uninova.s4h.citizenhub.data.LumbarExtensionTrainingValue;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.util.messaging.Dispatcher;

public class LumbarExtensionTrainingProtocol extends BluetoothMeasuringProtocol {

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.medx.medxtraining");

    public static final UUID UUID_SERVICE = UUID.fromString("5a46791b-516e-48fd-9d29-a2f18d520aec");
    public static final UUID UUID_CHARACTERISTIC = UUID.fromString("38fde8b6-9664-4b8e-8b3a-e52b8809a64c");

    public LumbarExtensionTrainingProtocol(BluetoothConnection connection, Dispatcher<Sample> sampleDispatcher, BluetoothAgent agent) {
        super(ID, connection, sampleDispatcher, agent);
    }

    @Override
    public void enable() {
        setState(Protocol.STATE_ENABLING);
        final BluetoothConnection connection = getConnection();

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_SERVICE, UUID_CHARACTERISTIC) {
            @Override
            public void onRead(byte[] value) {
                final ByteBuffer byteBuffer = ByteBuffer.wrap(value).asReadOnlyBuffer();

                final int timestamp = byteBuffer.getInt();
                final int length = byteBuffer.getInt();
                final float score = byteBuffer.getFloat();
                final float calories = byteBuffer.getFloat();
                final int repetitions = byteBuffer.getInt();
                final int weight = byteBuffer.getInt();

                final Sample sample = new Sample(getAgent().getSource(), new LumbarExtensionTrainingMeasurement(new LumbarExtensionTrainingValue(Duration.ofSeconds(length), (double) score, repetitions, weight)), new CaloriesMeasurement((double) calories));

                getSampleDispatcher().dispatch(sample);
            }

            @Override
            public void onReadFailure(byte[] value, int status) {
            }
        });

        connection.readCharacteristic(UUID_SERVICE, UUID_CHARACTERISTIC);
        setState(Protocol.STATE_ENABLED);
    }
}

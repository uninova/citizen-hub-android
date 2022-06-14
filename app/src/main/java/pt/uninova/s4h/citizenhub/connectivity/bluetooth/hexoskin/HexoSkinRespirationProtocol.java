package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.util.messaging.Dispatcher;

public class HexoSkinRespirationProtocol extends BluetoothMeasuringProtocol {

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin.respiration");

    public static final UUID RESPIRATION_SERVICE_UUID = UUID.fromString("3b55c581-bc19-48f0-bd8c-b522796f8e24");
    public static final UUID RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("9bc730c3-8cc0-4d87-85bc-573d6304403c");

    public HexoSkinRespirationProtocol(BluetoothConnection connection, Dispatcher<Sample> sampleDispatcher, HexoSkinAgent agent) {
        super(ID, connection, sampleDispatcher, agent);

        connection.addCharacteristicListener(new BaseCharacteristicListener(RESPIRATION_SERVICE_UUID, RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID) {
            @Override
            public void onChange(byte[] value) {
                final BreathingMeasurement measurement = new BreathingMeasurement(value);
                final Sample sample = new Sample(getAgent().getSource(), measurement.toMeasurements());

                getSampleDispatcher().dispatch(sample);
            }
        });
    }

    @Override
    public void disable() {
        getConnection().disableNotifications(RESPIRATION_SERVICE_UUID, RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID);
        setState(Protocol.STATE_DISABLED);
    }

    @Override
    public void enable() {
        getConnection().enableNotifications(RESPIRATION_SERVICE_UUID, RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID);
        setState(Protocol.STATE_ENABLED);
    }
}
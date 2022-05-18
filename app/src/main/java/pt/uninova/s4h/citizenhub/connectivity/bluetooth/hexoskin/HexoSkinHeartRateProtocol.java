package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.data.HeartRateMeasurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.util.messaging.Dispatcher;

public class HexoSkinHeartRateProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin.heartrate");
    final public static String name = HexoSkinHeartRateProtocol.class.getSimpleName();
    public final static UUID UUID_SERVICE_HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_HEART_RATE_CONTROL = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_HEART_RATE_DATA = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");


    public HexoSkinHeartRateProtocol(BluetoothConnection connection, Dispatcher<Sample> sampleDispatcher, HexoSkinAgent agent) {
        super(ID, connection, sampleDispatcher, agent);

        setState(Protocol.STATE_DISABLED);

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_DATA) {
            @Override
            public void onChange(byte[] value) {
                final pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.HeartRateMeasurement val = new pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.HeartRateMeasurement(value);

                getSampleDispatcher().dispatch(new Sample(getAgent().getSource(), new HeartRateMeasurement(val.getValue().toInt())));
            }
        });
    }

    @Override
    public void disable() {
        setState(Protocol.STATE_DISABLED);
        getConnection().disableNotifications(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_DATA);
    }

    @Override
    public void enable() {
        setState(Protocol.STATE_ENABLED);
        getConnection().enableNotifications(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_DATA);
    }

}

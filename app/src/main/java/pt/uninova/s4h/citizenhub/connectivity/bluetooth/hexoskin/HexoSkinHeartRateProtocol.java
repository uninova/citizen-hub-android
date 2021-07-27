package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class HexoSkinHeartRateProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin.heartrate");
    final public static String name = HexoSkinHeartRateProtocol.class.getSimpleName();
    public final static UUID UUID_SERVICE_HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_HEART_RATE_CONTROL = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_HEART_RATE_DATA = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");

    private Class<?> agent;

    public HexoSkinHeartRateProtocol(BluetoothConnection connection, Class<?> agent) {
        super(ID, connection, agent);

        setState(ProtocolState.DISABLED);

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_DATA) {
            @Override
            public void onChange(byte[] value) {

                getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.HEART_RATE, (double) value[1]));
            }
        });
    }

    @Override
    public Class<?> getAgent() {
        return agent;
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
        getConnection().disableNotifications(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_DATA);
    }

    @Override
    public void enable() {
        System.out.println("HEARTRATE_ENABLED");
        setState(ProtocolState.ENABLED);
        getConnection().enableNotifications(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_DATA);
    }

}

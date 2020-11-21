package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import android.os.Handler;
import android.os.Looper;
import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseDescriptorListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static android.os.Looper.getMainLooper;

public class MiBand2HeartRateProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.heartrate");

    public final static UUID UUID_SERVICE_HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_HEART_RATE_CONTROL = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_HEART_RATE_DATA = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");

    public MiBand2HeartRateProtocol(BluetoothConnection connection) {
        super(ID, connection);

        setState(ProtocolState.DISABLED);

        connection.addDescriptorListener(new BaseDescriptorListener(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_DATA, BluetoothConnection.ORG_BLUETOOTH_DESCRIPTOR_GATT_CLIENT_CHARACTERISTIC_CONFIGURATION) {
            @Override
            public void onWrite(byte[] value) {
                connection.writeCharacteristic(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_CONTROL, new byte[]{0x15, 0x02, 0x00});
            }
        });

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_CONTROL) {
            @Override
            public void onWrite(byte[] value) {
                if (value[0] == 0x15 && value[1] == 0x02 && value[2] == 0x00) {
                    connection.writeCharacteristic(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_CONTROL, new byte[]{0x15, 0x01, 0x00});
                } else if (value[0] == 0x15 && value[1] == 0x01 && value[2] == 0x00) {
                    connection.writeCharacteristic(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_CONTROL, new byte[]{0x15, 0x01, 0x01});
                } else if (value[0] == 0x15 && value[1] == 0x01 && value[2] == 0x01) {
                    setState(ProtocolState.ENABLED);

                    final Handler h = new Handler(Looper.getMainLooper());

                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            connection.writeCharacteristic(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_CONTROL, new byte[]{0x16});

                            if (getState() == ProtocolState.ENABLED) {
                                h.postDelayed(this, 10000);
                            }
                        }
                    }, 10000);
                }

            }
        });

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_DATA) {
            @Override
            public void onChange(byte[] value) {
                getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.HEART_RATE, (double) value[1]));
            }
        });
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
    }

    @Override
    public void enable() {
        getConnection().enableNotifications(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_DATA);
    }
}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import android.os.Handler;
import android.os.Looper;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseDescriptorListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.CharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.DescriptorListener;
import pt.uninova.s4h.citizenhub.data.HeartRateMeasurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.util.messaging.Dispatcher;

public class MiBand2HeartRateProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.heartrate");

    private final DescriptorListener heartRateMeasurementClientCharacteristicConfigurationListener = new BaseDescriptorListener(BluetoothAgent.UUID_SERVICE_HEART_RATE, BluetoothAgent.UUID_CHARACTERISTIC_HEART_RATE_MEASUREMENT, BluetoothAgent.UUID_DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION) {
        @Override
        public void onWrite(byte[] value) {
            getConnection().removeDescriptorListener(this);
            getConnection().writeCharacteristic(BluetoothAgent.UUID_SERVICE_HEART_RATE, BluetoothAgent.UUID_CHARACTERISTIC_HEART_RATE_CONTROL_POINT, new byte[]{0x15, 0x02, 0x00});
        }
    };

    private final CharacteristicListener heartRateControlPointListener = new BaseCharacteristicListener(BluetoothAgent.UUID_SERVICE_HEART_RATE, BluetoothAgent.UUID_CHARACTERISTIC_HEART_RATE_CONTROL_POINT) {
        @Override
        public void onWrite(byte[] value) {
            if (value[0] == 0x15 && value[1] == 0x02 && value[2] == 0x00) {
                getConnection().writeCharacteristic(BluetoothAgent.UUID_SERVICE_HEART_RATE, BluetoothAgent.UUID_CHARACTERISTIC_HEART_RATE_CONTROL_POINT, new byte[]{0x15, 0x01, 0x00});
            } else if (value[0] == 0x15 && value[1] == 0x01 && value[2] == 0x00) {
                getConnection().writeCharacteristic(BluetoothAgent.UUID_SERVICE_HEART_RATE, BluetoothAgent.UUID_CHARACTERISTIC_HEART_RATE_CONTROL_POINT, new byte[]{0x15, 0x01, 0x01});
            } else if (value[0] == 0x15 && value[1] == 0x01 && value[2] == 0x01) {
                getConnection().removeCharacteristicListener(this);
                setState(Protocol.STATE_ENABLED);

                final Handler h = new Handler(Looper.getMainLooper());

                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getConnection().writeCharacteristic(BluetoothAgent.UUID_SERVICE_HEART_RATE, BluetoothAgent.UUID_CHARACTERISTIC_HEART_RATE_CONTROL_POINT, new byte[]{0x16});

                        if (getState() == Protocol.STATE_ENABLED) {
                            h.postDelayed(this, 10000);
                        }
                    }
                }, 10000);
            }

        }
    };

    private final CharacteristicListener heartRateMeasurementListener = new BaseCharacteristicListener(BluetoothAgent.UUID_SERVICE_HEART_RATE, BluetoothAgent.UUID_CHARACTERISTIC_HEART_RATE_MEASUREMENT) {
        @Override
        public void onChange(byte[] value) {
            final pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.HeartRateMeasurement val = new pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.HeartRateMeasurement(value);

            getSampleDispatcher().dispatch(new Sample(getAgent().getSource(), new HeartRateMeasurement(val.getValue().toInt())));
        }
    };

    public MiBand2HeartRateProtocol(BluetoothConnection connection, Dispatcher<Sample> sampleDispatcher, MiBand2Agent agent) {
        super(ID, connection, sampleDispatcher, agent);
    }

    @Override
    public void disable() {
        getConnection().removeCharacteristicListener(heartRateMeasurementListener);
        getConnection().disableNotifications(BluetoothAgent.UUID_SERVICE_HEART_RATE, BluetoothAgent.UUID_CHARACTERISTIC_HEART_RATE_MEASUREMENT);

        super.disable();
    }

    @Override
    public void enable() {
        getConnection().addDescriptorListener(heartRateMeasurementClientCharacteristicConfigurationListener);
        getConnection().addCharacteristicListener(heartRateControlPointListener);

        getConnection().addCharacteristicListener(heartRateMeasurementListener);

        getConnection().enableNotifications(BluetoothAgent.UUID_SERVICE_HEART_RATE, BluetoothAgent.UUID_CHARACTERISTIC_HEART_RATE_MEASUREMENT);
    }
}

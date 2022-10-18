package pt.uninova.s4h.citizenhub.connectivity.bluetooth.digitsole;

import android.os.Handler;
import android.os.Looper;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.CharacteristicListener;
import pt.uninova.s4h.citizenhub.data.CaloriesMeasurement;
import pt.uninova.s4h.citizenhub.data.DistanceMeasurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.data.StepsMeasurement;
import pt.uninova.s4h.citizenhub.util.messaging.Dispatcher;

public class DigitsoleActivityProtocol extends BluetoothMeasuringProtocol {

    public static final UUID UUID_SERVICE_DATA = UUID.fromString("99ddcdab-a80c-4f94-be5d-c66b9fba40cf");
    private static final UUID UUID_CHARACTERISTIC_ACTIVITY_LOG = UUID.fromString("99dd0106-a80c-4f94-be5d-c66b9fba40cf");
    private static final UUID UUID_CHARACTERISTIC_COLLECTING_STATE = UUID.fromString("99dd0014-a80c-4f94-be5d-c66b9fba40cf");
    private static final UUID UUID_CHARACTERISTIC_BATTERY = UUID.fromString("99dd0016-a80c-4f94-be5d-c66b9fba40cf");

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.digitsole.activity");

    long lastTime = 0;
    int milliForTimer = 75000;

    private final CharacteristicListener activationListener = new BaseCharacteristicListener(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE) {
        private boolean once0x02 = false;
        private boolean isCalibrated = false;

        @Override
        public void onWrite(byte[] value) {
            switch (value[0]) {
                case 0x00:
                    if (!isCalibrated)
                        getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE, new byte[]{0x24, 0x02}); // 1st (Calibration)
                    else
                        getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE, new byte[]{0x02}); // 1st (Re-connect)
                    break;
                case 0x24:
                    getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE, new byte[]{0x46, 0x02, 0x48, 0x00, 0x00, 0x00}); // 2nd (Calibration)
                    break;
                case 0x02:
                    if (once0x02) {
                        getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE, new byte[]{0x01}); // 7th (Calibration) | 3rd (Re-connect)
                    } else {
                        once0x02 = true;
                        getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE, new byte[]{0x02}); // 6th (Calibration) | 2nd (Re-connect)
                    }
                    break;
                case 0x01:
                    once0x02 = false; // 8th (Calibration) | 4th (Re-connect)
                    break;
                case 0x46:
                    if(!isCalibrated){
                        switch (value[1]) {
                            case 0x02:
                                getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE, new byte[]{0x46, 0x03, (byte) 0xb4, 0x00, 0x00, 0x00}); // 3rd (Calibration)
                                break;
                            case 0x03:
                                getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE, new byte[]{0x46, 0x04, 0x2a, 0x00, 0x00, 0x00}); // 4th (Calibration)
                                break;
                            case 0x04:
                                getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE, new byte[]{0x02}); // 5th (Calibration)
                                isCalibrated = true;
                                break;
                        }
                    }
                    break;
            }
        }
    };

    private final CharacteristicListener dataListener = new BaseCharacteristicListener(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_ACTIVITY_LOG) {
        @Override
        public void onChange(byte[] value) {
            double steps = value[4] & 0xff;

            DecimalFormat f = new DecimalFormat("##.00");
            double distance = steps * 0.76;
            f.format(distance);
            double calories = steps * 0.04;
            f.format(calories);

            final Sample sample = new Sample(getAgent().getSource(), new StepsMeasurement(steps),
                    new DistanceMeasurement(distance),
                    new CaloriesMeasurement(calories));
            getSampleDispatcher().dispatch(sample);

            lastTime = System.currentTimeMillis();

            System.out.println("VALUES FROM DIGITSOLE: " + Arrays.toString(value));
            System.out.println("STEPS DETECTED: " + steps);
        }
    };

    private final CharacteristicListener dataListenerBattery = new BaseCharacteristicListener(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_BATTERY) {
        @Override
        public void onChange(byte[] value) {
            double batteryLeft = value[0] & 0xff;
            double batteryRight = value[2] & 0xff;

            System.out.println("Battery SOLAS: " + batteryLeft + " | " + batteryRight + " | " + Arrays.toString(value));
        }
    };

    public DigitsoleActivityProtocol(BluetoothConnection connection, Dispatcher<Sample> dispatcher, BluetoothAgent agent) {
        super(ID, connection, dispatcher, agent);
    }

    @Override
    public void disable() {
        System.out.println("DISABLE");
        final BluetoothConnection connection = getConnection();
        connection.removeCharacteristicListener(activationListener);
        connection.removeCharacteristicListener(dataListener);
        connection.removeCharacteristicListener(dataListenerBattery);
        connection.disableNotifications(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_ACTIVITY_LOG);
        connection.disableNotifications(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE);
        setState(Protocol.STATE_DISABLED);
    }

    @Override
    public void enable() {
        System.out.println("ENABLE 1st");
        long currentTime = System.currentTimeMillis();
        runTimer();
        if ((currentTime - lastTime) > milliForTimer) {
            System.out.println("ENABLE 2nd");
            final BluetoothConnection connection = getConnection();
            connection.addCharacteristicListener(activationListener);
            connection.addCharacteristicListener(dataListener);
            connection.addCharacteristicListener(dataListenerBattery);
            connection.enableNotifications(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_ACTIVITY_LOG, true);
            connection.enableNotifications(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_BATTERY, true);
            connection.writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTING_STATE, new byte[]{0x00});
            setState(Protocol.STATE_ENABLED);
        }
    }

    private void runTimer() {
        new Handler(Looper.getMainLooper()).postDelayed(this::enable, milliForTimer);
    }
}
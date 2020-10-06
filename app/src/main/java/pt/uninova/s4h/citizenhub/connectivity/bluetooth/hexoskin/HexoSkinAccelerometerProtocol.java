package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class HexoSkinAccelerometerProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin.respiration");
    private static UUID ACCELEROMETER_SERVICE_UUID = UUID.fromString("bdc750c7-2649-4fa8-abe8-fbf25038cda3");
    private static UUID ACCELEROMETER_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("75246a26-237a-4863-aca6-09b639344f43");
    private Integer lastSteps;
    private Integer lastActivity;
    private Double lastCadence;

    public HexoSkinAccelerometerProtocol(BluetoothConnection connection) {
        super(ID, connection);
        setState(ProtocolState.DISABLED);

        connection.addCharacteristicListener(new BaseCharacteristicListener(ACCELEROMETER_SERVICE_UUID, ACCELEROMETER_MEASUREMENT_CHARACTERISTIC_UUID) {
            @Override
            public void onChange(byte[] value) {
                ByteBuffer val = ByteBuffer.wrap(value);
               //   val.order(ByteOrder.LITTLE_ENDIAN);
                byte flag = value[0];
                int format = BluetoothGattCharacteristic.FORMAT_UINT16;
                int dataIndex = 1;

                //TODO add getIntValue

                boolean isStepCountPresent = (flag & 0x01) != 0;
                boolean isActivityPresent = (flag & 0x02) != 0;
                boolean isCadencePresent = (flag & 0x04) != 0;
                String hexString = byteArrayToHex(value);

                //TODO https://bitbucket.org/carre/hexoskin-smart-demo/src/master/hexoskin-smart-android/app/src/main/java/com/hexoskin/hexoskin_smart_android/DeviceActivity.java
                if (isStepCountPresent) {
                    int stepCount = val.get(dataIndex);
                    Log.d("steps","STEP COUNT " + stepCount + ", (" + hexString + ")");
                    dataIndex = dataIndex + 2;
                    getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.STEPS, (double) value[1]));
                    //TODO Corrigir os kinds
                }

                if (isActivityPresent) {
                    float activity = val.get(dataIndex)/256.0f;
                    Log.d("activity","ACTIVITY " + activity + ", (" + hexString + ")");
                    dataIndex = dataIndex + 2;
                    getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.ACCELEROMETER, (double) value[1]));

                }

                if (isCadencePresent) {
                    int cadence = val.get(dataIndex);
                    Log.d("cadence", "CADENCE " + cadence + ", (" + hexString + ")");
                    getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.ACCELEROMETER, (double) value[1]));

                }
            }
        });
    }

    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
    }

    @Override
    public void enable() {
        getConnection().enableNotifications(ACCELEROMETER_SERVICE_UUID, ACCELEROMETER_MEASUREMENT_CHARACTERISTIC_UUID);
    }

}

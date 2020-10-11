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

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

public class HexoSkinAccelerometerProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin.accelerometer");
    private static UUID ACCELEROMETER_SERVICE_UUID = UUID.fromString("bdc750c7-2649-4fa8-abe8-fbf25038cda3");
    private static UUID ACCELEROMETER_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("75246a26-237a-4863-aca6-09b639344f43");
    private Integer lastSteps;
    private Integer lastActivity;
    private Double lastCadence;

    public HexoSkinAccelerometerProtocol(BluetoothConnection connection) {
        super(ID, connection);
        setState(ProtocolState.DISABLED);
      //  characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_FLOAT, BluetoothGattCharacteristic.FORMAT_FLOAT);

        connection.addCharacteristicListener(new BaseCharacteristicListener(ACCELEROMETER_SERVICE_UUID, ACCELEROMETER_MEASUREMENT_CHARACTERISTIC_UUID) {
            @Override
            public void onChange(byte[] value) {
                byte flag = value[0];
                int format = FORMAT_UINT16;
                int dataIndex = 1;

                boolean isStepCountPresent = (flag & 0x01) != 0;
                boolean isActivityPresent = (flag & 0x02) != 0;
                boolean isCadencePresent = (flag & 0x04) != 0;
                String hexString = byteArrayToHex(value);

                //TODO https://bitbucket.org/carre/hexoskin-smart-demo/src/master/hexoskin-smart-android/app/src/main/java/com/hexoskin/hexoskin_smart_android/DeviceActivity.java
                if (isStepCountPresent) {
                    int stepCount = getIntValue(format,dataIndex,value);
                    // int stepCount = val.get(dataIndex);
                    Log.d("steps","STEP COUNT " + stepCount + ", (" + hexString + ")");
                    dataIndex = dataIndex + 2;
                    getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.STEPS, (double) stepCount));
                }

                if (isActivityPresent) {
                    float activity = getIntValue(format,dataIndex,value)/256.0f;

                    //The activity is a representation of your movement intensity over the last second.
                    // Looking at your activity lets you know when you started and stopped running, for example.
                    // The intensity of activity also gives you some insight of your activity efficiency:
                    // In general, if you can perform an activity by moving less, you are being more efficient in your movements.

                  //  float activity = val.get(dataIndex)/256.0f;
                    Log.d("activity","ACTIVITY " + activity + ", (" + hexString + ")");
                    dataIndex = dataIndex + 2;
                    getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.ACTIVITY, (double) activity));

                }


                if (isCadencePresent) {
                    int cadence = getIntValue(format,dataIndex,value);
                    //steps per minute

                    // int cadence = val.get(dataIndex);
                    Log.d("cadence", "CADENCE " + cadence + ", (" + hexString + ")");
                    getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.CADENCE, (double) cadence));

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

    public Integer getIntValue(int formatType, int offset, byte[] value) {
        if ((offset + getTypeLen(formatType)) > value.length) return null;

        switch (formatType) {
            case FORMAT_UINT8:
                return unsignedByteToInt(value[offset]);

            case FORMAT_UINT16:
                return unsignedBytesToInt(value[offset], value[offset + 1]);
        }

        return null;
    }

    private int getTypeLen(int formatType) {
        return formatType & 0xF;
    }

    private int unsignedByteToInt(byte b) {
        return b & 0xFF;
    }

    /**
     * Convert signed bytes to a 16-bit unsigned int.
     */
    private int unsignedBytesToInt(byte b0, byte b1) {
        return (unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8));
    }

}

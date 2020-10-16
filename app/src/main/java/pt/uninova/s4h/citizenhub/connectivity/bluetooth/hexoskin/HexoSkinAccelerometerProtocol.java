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

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

public class HexoSkinAccelerometerProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin.accelerometer");
    private static UUID ACCELEROMETER_SERVICE_UUID = UUID.fromString("bdc750c7-2649-4fa8-abe8-fbf25038cda3");
    private static UUID ACCELEROMETER_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("75246a26-237a-4863-aca6-09b639344f43");

    private int lastStepCount;

    public HexoSkinAccelerometerProtocol(BluetoothConnection connection) {
        super(ID, connection);
        setState(ProtocolState.DISABLED);

        lastStepCount = 0;

        connection.addCharacteristicListener(new BaseCharacteristicListener(ACCELEROMETER_SERVICE_UUID, ACCELEROMETER_MEASUREMENT_CHARACTERISTIC_UUID) {
            @Override
            public void onChange(byte[] value) {
                byte flag = value[0];
                int format = FORMAT_UINT16;
                int dataIndex = 1;

                boolean isStepCountPresent = (flag & 0x01) != 0;
                boolean isActivityPresent = (flag & 0x02) != 0;
                boolean isCadencePresent = (flag & 0x04) != 0;

                if (isStepCountPresent) {
                    int stepCount = getIntValue(format, dataIndex, value);
                    dataIndex = dataIndex + 2;

                    if (stepCount < lastStepCount) {
                        lastStepCount = 0;
                    }

                    getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.STEPS, (double) stepCount - lastStepCount));

                    lastStepCount = stepCount;
                }

                if (isActivityPresent) {
                    float activity = getIntValue(format, dataIndex, value) / 256.0f;

                    dataIndex = dataIndex + 2;
                    getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.ACTIVITY, (double) activity));

                }


                if (isCadencePresent) {
                    int cadence = getIntValue(format, dataIndex, value);
                    getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.CADENCE, (double) cadence));

                }
            }
        });
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

    private int unsignedBytesToInt(byte b0, byte b1) {
        return (unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8));
    }

}

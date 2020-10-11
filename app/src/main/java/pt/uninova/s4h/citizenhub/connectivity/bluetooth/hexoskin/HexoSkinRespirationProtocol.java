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
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

public class HexoSkinRespirationProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin.respiration");
    private static UUID RESPIRATION_SERVICE_UUID = UUID.fromString("3b55c581-bc19-48f0-bd8c-b522796f8e24");
    private static UUID RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("9bc730c3-8cc0-4d87-85bc-573d6304403c");

    public HexoSkinRespirationProtocol(BluetoothConnection connection) {
        super(ID, connection);
        setState(ProtocolState.DISABLED);

        connection.addCharacteristicListener(new BaseCharacteristicListener(RESPIRATION_SERVICE_UUID, RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID) {
            @Override
            public void onChange(byte[] value) {
                ByteBuffer val = ByteBuffer.wrap(value);
                val.order(ByteOrder.LITTLE_ENDIAN);
                byte flag = value[0];
                int format;
                if ((flag & 0x01) == 0) {
                    format = BluetoothGattCharacteristic.FORMAT_UINT8;
                } else {
                    format = BluetoothGattCharacteristic.FORMAT_UINT16;
                }

                int respRate = getIntValue(format, 1, value);
                getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.RESPIRATION_RATE, (double) respRate));

                Log.d("Respiration", "Respiration Rate: " + respRate);

                boolean isInspExpPresent = (flag & 0x02) != 0;
                if (isInspExpPresent) {
                    int startOffset = 1 + (format == BluetoothGattCharacteristic.FORMAT_UINT8 ? 1 : 2);
                    boolean inspFirst = (flag & 0x04) == 0;
                    for (int i = startOffset; i < value.length; i += 2) {
                        float result = getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, i, value) / 32.0f;
                        if (inspFirst) {
                            Log.d("Respiration", "Inspiration: " + result);
                            getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.INSPIRATION, (double) result));

                            inspFirst = false;
                        } else {
                            getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.EXPIRATION, (double) result));

                            Log.d("Respiration", "Expiration: " + result);
                            inspFirst = true;
                        }
                    }
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
        getConnection().enableNotifications(RESPIRATION_SERVICE_UUID, RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID);
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


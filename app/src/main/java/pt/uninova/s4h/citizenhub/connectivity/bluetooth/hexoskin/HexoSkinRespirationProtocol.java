package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import android.bluetooth.BluetoothGattCharacteristic;

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

public class HexoSkinRespirationProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin.respiration");
    private static UUID RESPIRATION_SERVICE_UUID = UUID.fromString("0x3b55c581-bc19-48f0-bd8c-b522796f8e24");
    private static UUID RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("9bc730c3-8cc0-4d87-85bc-573d6304403c");

    private Integer lastSteps;
    private Integer lastDistance;
    private Double lastCalories;

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
//                int respRate = val.getInt(format, 1);
//                _data.set(2, "RESP. RATE " + respRate + ", (" + hexString + ")");
//
//                boolean isInspExpPresent = (flag & 0x02) != 0;
//                if (isInspExpPresent) {
//                    int startOffset = 1 + (format == BluetoothGattCharacteristic.FORMAT_UINT8 ? 1 : 2);
//                    boolean inspFirst = (flag & 0x04) == 0;
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("INSP/EXP ");
//                    for (int i = startOffset; i < data.length; i += 2) {
//                        float value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, i) / 32.0f;
//                        if (inspFirst) {
//                            sb.append(value).append("(I), ");
//                            inspFirst = false;
//                        } else {
//                            sb.append(value).append("(E), ");
//                            inspFirst = true;
//                        }
//                    }
//                    _data.set(3, sb.toString());
//                }

                getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.RESPIRATION, (double) value[1]));
            }
        });
    }
    @Override
    public void disable () {
        setState(ProtocolState.DISABLED);
    }

    @Override
    public void enable () {
        getConnection().enableNotifications(RESPIRATION_SERVICE_UUID, RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID);
    }
}


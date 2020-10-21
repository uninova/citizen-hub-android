package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import static pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinDataConverter.getIntValue;

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
                byte flag = value[0];
                int format;

                if ((flag & 0x01) == 0) {
                    format = BluetoothGattCharacteristic.FORMAT_UINT8;
                } else {
                    format = BluetoothGattCharacteristic.FORMAT_UINT16;
                }

                int respRate = getIntValue(format, 1, value);

                getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.RESPIRATION_RATE, (double) respRate));

                boolean isInspExpPresent = (flag & 0x02) != 0;

                if (isInspExpPresent) {

                    int startOffset = 1 + (format == BluetoothGattCharacteristic.FORMAT_UINT8 ? 1 : 2);
                    boolean inspFirst = (flag & 0x04) == 0;

                    for (int i = startOffset; i < value.length; i += 2) {
                        float result = getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, i, value) / 32.0f;

                        if (inspFirst) {
                            getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.INSPIRATION, (double) result));
                            inspFirst = false;
                        } else {
                            getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.EXPIRATION, (double) result));
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

}


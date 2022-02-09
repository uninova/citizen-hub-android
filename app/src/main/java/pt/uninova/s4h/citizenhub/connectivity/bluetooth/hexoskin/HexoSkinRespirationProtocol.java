package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import static pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinDataConverter.getIntValue;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.data.ExpirationMeasurement;
import pt.uninova.s4h.citizenhub.data.InspirationMeasurement;
import pt.uninova.s4h.citizenhub.data.RespirationRateMeasurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.util.messaging.Dispatcher;

public class HexoSkinRespirationProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin.respiration");
    final public static String name = HexoSkinRespirationProtocol.class.getSimpleName();
    public static final UUID RESPIRATION_SERVICE_UUID = UUID.fromString("3b55c581-bc19-48f0-bd8c-b522796f8e24");
    public static final UUID RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("9bc730c3-8cc0-4d87-85bc-573d6304403c");

    public HexoSkinRespirationProtocol(BluetoothConnection connection, Dispatcher<Sample> sampleDispatcher, HexoSkinAgent agent) {
        super(ID, connection, sampleDispatcher, agent);
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
                boolean isInspExpPresent = (flag & 0x02) != 0;

                final pt.uninova.s4h.citizenhub.data.Measurement<?>[] measurements = new pt.uninova.s4h.citizenhub.data.Measurement[isInspExpPresent ? 2 : 1];

                measurements[0] = new RespirationRateMeasurement(respRate);

                if (isInspExpPresent) {
                    int startOffset = 1 + (format == BluetoothGattCharacteristic.FORMAT_UINT8 ? 1 : 2);
                    boolean inspFirst = (flag & 0x04) == 0;

                    for (int i = startOffset; i < value.length; i += 2) {
                        float result = getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, i, value) / 32.0f;

                        if (inspFirst) {
                            measurements[1] = new InspirationMeasurement(result);
                            inspFirst = false;
                        } else {
                            measurements[1] = new ExpirationMeasurement(result);
                            inspFirst = true;
                        }
                    }
                }

                final Sample sample = new Sample(getAgent().getSource(), measurements);

                getSampleDispatcher().dispatch(sample);
            }
        });
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
        getConnection().disableNotifications(RESPIRATION_SERVICE_UUID, RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID);

    }

    @Override
    public void enable() {
        System.out.println("RESPIRATION_ENABLED");
        setState(ProtocolState.ENABLED);
        getConnection().enableNotifications(RESPIRATION_SERVICE_UUID, RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID);
    }

}


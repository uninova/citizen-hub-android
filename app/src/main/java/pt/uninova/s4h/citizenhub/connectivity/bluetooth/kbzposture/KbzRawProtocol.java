package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static android.os.Looper.getMainLooper;

public class KbzRawProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.kbzposture.raw");

    final private static UUID KBZ_SERVICE = UUID.fromString("0000ff30-0000-1000-8000-00805f9b34fb");
    final private static UUID KBZ_CHARACTERISTIC = UUID.fromString("0000ff35-0000-1000-8000-00805f9b34fb");

    public KbzRawProtocol(BluetoothConnection connection) {
        super(ID, connection);
    }

    private void attachObservers() {
        final BluetoothConnection connection = getConnection();

        connection.addCharacteristicListener(new BaseCharacteristicListener(KBZ_SERVICE, KBZ_CHARACTERISTIC) {

        });
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void enable() {
        attachObservers();

        //getConnection().writeCharacteristic(KBZ_SERVICE, KBZ_CHARACTERISTIC,1 );

        super.enable();
    }

}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import android.os.Handler;
import android.os.Looper;
import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static android.os.Looper.getMainLooper;

public class MiBand2DistanceProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.distance");

    final private static UUID UUID_SERVICE = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    final private static UUID UUID_CHARACTERISTIC_STEPS = UUID.fromString("00000007-0000-3512-2118-0009af100700");

    private Integer lastSteps;
    private Double lastDistance;
    private Double lastCalories;

    public MiBand2DistanceProtocol(BluetoothConnection connection) {
        super(ID, connection);

        setState(ProtocolState.DISABLED);

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_SERVICE, UUID_CHARACTERISTIC_STEPS) {
            @Override
            public void onRead(byte[] value) {
                ByteBuffer val = ByteBuffer.wrap(value);
                val.order(ByteOrder.LITTLE_ENDIAN);

                final int steps = val.getInt(1);
                final double distance;
                final double calories;

                if (value.length > 5) {
                    distance = val.getInt(5);
                    calories = val.getInt(9);
                } else {
                    distance = steps * 0.5;
                    calories = steps * 0.04;
                }

                final Date now = new Date();

                if (lastSteps != null) {
                    if (steps < lastSteps) {
                        lastSteps = 0;
                        lastDistance = 0.0;
                        lastCalories = 0.0;
                    }

                    getMeasurementDispatcher().dispatch(new Measurement(now, MeasurementKind.STEPS, (double) steps - lastSteps));
                    getMeasurementDispatcher().dispatch(new Measurement(now, MeasurementKind.DISTANCE, distance - lastDistance));
                    getMeasurementDispatcher().dispatch(new Measurement(now, MeasurementKind.CALORIES, calories - lastCalories));
                }

                lastSteps = steps;
                lastDistance = distance;
                lastCalories = calories;
            }
        });
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
    }

    @Override
    public void enable() {
        final Handler h = new Handler(Looper.getMainLooper());

        setState(ProtocolState.ENABLED);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                getConnection().readCharacteristic(UUID_SERVICE, UUID_CHARACTERISTIC_STEPS);

                if (getState() == ProtocolState.ENABLED) {
                    h.postDelayed(this, 10000);
                }
            }
        }, 0);
    }

}

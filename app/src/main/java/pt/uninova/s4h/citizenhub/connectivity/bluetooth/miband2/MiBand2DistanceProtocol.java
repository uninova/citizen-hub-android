package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import android.os.Handler;
import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static android.os.Looper.getMainLooper;

public class MiBand2DistanceProtocol extends AbstractMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.distance");

    public MiBand2DistanceProtocol(BluetoothConnection connection) {
        super(ID);
    }

    @Override
    public void disable() {

    }

    @Override
    public void enable() {
        Handler handler = new Handler(getMainLooper());
        int delay = 10000;
        final Random random = new Random();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double val = random.nextDouble();

                MiBand2DistanceProtocol.this.getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.STEPS, val * 2));
                MiBand2DistanceProtocol.this.getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.DISTANCE, val * 1));
                MiBand2DistanceProtocol.this.getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.CALORIES, val * 0.04));

                handler.postDelayed(this, random.nextInt(delay));
            }
        }, random.nextInt(delay));
    }

}

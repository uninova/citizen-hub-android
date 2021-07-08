package pt.uninova.s4h.citizenhub.connectivity.bluetooth.placebo;

import android.os.Handler;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import static android.os.Looper.getMainLooper;

public class PlaceboAllProtocol extends AbstractMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.heartrate");

    public PlaceboAllProtocol(BluetoothConnection connection) {
        super(ID);
    }

    @Override
    public void disable() {

    }

    @Override
    public void enable() {
        final Handler handler = new Handler(getMainLooper());
        final Random random = new Random();
        int delay = 10000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final MeasurementKind kind = MeasurementKind.values()[random.nextInt(MeasurementKind.values().length)];

                PlaceboAllProtocol.this.getMeasurementDispatcher().dispatch(new Measurement(new Date(), kind, (double) random.nextInt(100)));

                handler.postDelayed(this, random.nextInt(delay));
            }
        }, random.nextInt(delay));
    }

}

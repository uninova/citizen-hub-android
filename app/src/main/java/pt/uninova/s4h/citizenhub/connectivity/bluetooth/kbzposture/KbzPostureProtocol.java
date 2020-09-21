package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

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

public class KbzPostureProtocol extends AbstractMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.kbzposture.posture");

    public KbzPostureProtocol(BluetoothConnection connection) {
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

                KbzPostureProtocol.this.getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.GOOD_POSTURE, val * 5));
                KbzPostureProtocol.this.getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.BAD_POSTURE, val * 5));

                handler.postDelayed(this, random.nextInt(delay));
            }
        }, random.nextInt(delay));
    }

}

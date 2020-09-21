package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import android.os.Handler;
import pt.uninova.s4h.citizenhub.connectivity.AbstractMessagingProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;

import java.util.Random;
import java.util.UUID;

import static android.os.Looper.getMainLooper;

public class MiBand2HeartRateProtocol extends AbstractMessagingProtocol<Integer> {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.heartrate");

    public MiBand2HeartRateProtocol(BluetoothConnection connection) {
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

        handler.postDelayed(() -> getMessageDispatcher().dispatch(random.nextInt(20) + 89), random.nextInt(random.nextInt(delay)));
    }

}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth.digitsole;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.DeadObjectException;

import java.util.UUID;

import androidx.core.content.ContextCompat;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.CharacteristicListener;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.data.StepCountMeasurement;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public class DigitsoleActivityProtocol extends BluetoothMeasuringProtocol {

    private static final UUID UUID_SERVICE_DATA = UUID.fromString("99ddcdab-a80c-4f94-be5d-c66b9fba40cf");
    private static final UUID UUID_CHARACTERISTIC_ACTIVITYLOG = UUID.fromString("99dd0106-a80c-4f94-be5d-c66b9fba40cf");
    private static final UUID UUID_CHARACTERISTIC_COLLECTINGSTATE = UUID.fromString("99dd0014-a80c-4f94-be5d-c66b9fba40cf");

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.digitsole.activity");

    Context context;
    long lastTime = 0;

    private final CharacteristicListener activationListener = new BaseCharacteristicListener(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTINGSTATE) {

        private boolean once0x02 = false;

        @Override
        public void onWrite(byte[] value) {
            switch (value[0]) {
                case 0x00:
                    getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTINGSTATE, new byte[]{0x02});
                    break;
                case 0x02:
                    if (once0x02) {
                        getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTINGSTATE, new byte[]{0x01});
                    } else {
                        once0x02 = true;
                        getConnection().writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTINGSTATE, new byte[]{0x02});
                    }
                    break;
                case 0x01:
                    once0x02 = false;
                    setState(ProtocolState.ENABLED);
                    break;
            }
        }
    };

    private final CharacteristicListener dataListener = new BaseCharacteristicListener(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_ACTIVITYLOG) {

        @Override
        public void onChange(byte[] value) {
            int steps = value[4] & 0xff;

            final Sample sample = new Sample(getAgent().getSource(), new StepCountMeasurement(steps));

            lastTime = System.currentTimeMillis();

            getSampleDispatcher().dispatch(sample);
        }
    };

    private final Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>> reconnectionListener = new Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>>() {

        @Override
        public void observe(StateChangedMessage<BluetoothConnectionState, BluetoothConnection> value) {
            if (value.getNewState() == BluetoothConnectionState.READY) {
                value.getSource().enableNotifications(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_ACTIVITYLOG, true);
            }
        }
    };

    public DigitsoleActivityProtocol(BluetoothConnection connection, Dispatcher<Sample> dispatcher, BluetoothAgent agent, Context context) {
        super(ID, connection, dispatcher, agent);
        this.context = context;
    }

    @Override
    public void disable() {
        final BluetoothConnection connection = getConnection();

        connection.disableNotifications(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_ACTIVITYLOG);
        connection.removeCharacteristicListener(dataListener);

        connection.removeCharacteristicListener(activationListener);

        super.disable();
    }

    @Override
    public void enable() {

        long currentTime = System.currentTimeMillis();

        //System.out.println("Current Time: " + currentTime);
        //System.out.println("Last Time: " + lastTime);
        //System.out.println("Difference Time: " + (currentTime-lastTime));

        if(currentTime-lastTime < (2*60*1000)) //3 minutes
        {
            System.out.println("Last segment is recent, restarting timer...");
            runTimer();
            return;
        }
        System.out.println("Last segment is old or first time connecting, enabling...");
        runTimer();

        final BluetoothConnection connection = getConnection();
        connection.addCharacteristicListener(activationListener);
        connection.addCharacteristicListener(dataListener);
        connection.enableNotifications(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_ACTIVITYLOG, true);
        connection.writeCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_COLLECTINGSTATE, new byte[]{0x00});
    }

    private void runTimer(){
        ContextCompat.getMainExecutor(context).execute(()  -> {
            //set Countdown, 120 seconds
            new CountDownTimer(120000, 1000) {
                @Override
                public void onTick(long millisecondsUntilDone) {
                    System.out.println("Running Timer... " + millisecondsUntilDone);
                }

                @Override
                public void onFinish() {
                    //System.out.println("I've reached onFinish on the DigitsoleStateChecker.");
                    enable();
                }
            }.start();
        });
    }

}

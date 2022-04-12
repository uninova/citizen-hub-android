package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

import java.time.Instant;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.CharacteristicListener;
import pt.uninova.s4h.citizenhub.data.BadPostureMeasurement;
import pt.uninova.s4h.citizenhub.data.GoodPostureMeasurement;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;
import pt.uninova.util.time.Accumulator;
import pt.uninova.util.time.FlushingAccumulator;

public class KbzBodyProtocol extends BluetoothMeasuringProtocol {

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo2.posture");

    private static final UUID KBZ_SERVICE = UUID.fromString("0000ff30-0000-1000-8000-00805f9b34fb");
    private static final UUID KBZ_CHARACTERISTIC = UUID.fromString("0000ff35-0000-1000-8000-00805f9b34fb");
    private static final UUID KBZ_RAW_CHARACTERISTIC = UUID.fromString("0000ff38-0000-1000-8000-00805f9b34fb");

    private static final double ACC_RATIO = 4.0 / 32767;
    private static final double GYR_RATIO = 1000.0 / 32767;
    private static final double MAG_RATIO = 48.0 / 32767;

    private static final double[] RATIOS = {ACC_RATIO, GYR_RATIO, MAG_RATIO};

    private final Accumulator<Integer> motion;
    private final Accumulator<Boolean> posture;
    private final Accumulator<Boolean> position;

    private final CharacteristicListener changeListener = new BaseCharacteristicListener(KBZ_SERVICE, KBZ_RAW_CHARACTERISTIC) {
        @Override
        public void onChange(byte[] value) {
            final Instant now = Instant.now();
            final double[] values = parse(value);

            posture.set(Math.abs(values[1]) > 0.95, now);
        }
    };

    private final CharacteristicListener writeListener = new BaseCharacteristicListener(KBZ_SERVICE, KBZ_CHARACTERISTIC) {
        @Override
        public void onWrite(byte[] value) {
            getConnection().enableNotifications(KBZ_SERVICE, KBZ_RAW_CHARACTERISTIC);
        }
    };

    private final Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>> connectionStateObserver = new Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>>() {
        @Override
        public void observe(StateChangedMessage<BluetoothConnectionState, BluetoothConnection> value) {
            if (value.getNewState() == BluetoothConnectionState.READY) {
                KbzBodyProtocol.this.setState(ProtocolState.ENABLED);
                KbzBodyProtocol.this.getConnection().writeCharacteristic(KBZ_SERVICE, KBZ_CHARACTERISTIC, new byte[]{1 & 0xFF});
            } else {
                KbzBodyProtocol.this.setState(ProtocolState.SUSPENDED);
                posture.stop();
            }
        }
    };

    protected KbzBodyProtocol(BluetoothConnection connection, Dispatcher<Sample> dispatcher, KbzPostureAgent agent) {
        super(ID, connection, dispatcher, agent);

        this.motion = new Accumulator<>();
        this.position = new Accumulator<>();
        this.posture = new FlushingAccumulator<>(5000);

        this.posture.addObserver(value -> {
            final double seconds = value.getSecond().toNanos() * 0.000000001;
            final Measurement<?> measurement = value.getFirst() ? new GoodPostureMeasurement(seconds) : new BadPostureMeasurement(seconds);
            final Sample sample = new Sample(getAgent().getSource(), measurement);

            getSampleDispatcher().dispatch(sample);
        });
    }

    private static double byteToDouble(byte a, byte b) {
        return (b & 0xFF) << 8 | a & 0xFF;
    }

    @Override
    public void close() {
        motion.clear();
        position.clear();
        posture.clear();

        super.close();
    }

    @Override
    public void disable() {
        getConnection().removeCharacteristicListener(changeListener);
        getConnection().removeCharacteristicListener(writeListener);
        getConnection().removeConnectionStateChangeListener(connectionStateObserver);

        super.disable();
    }

    @Override
    public void enable() {
        getConnection().addCharacteristicListener(changeListener);
        getConnection().addCharacteristicListener(writeListener);
        getConnection().addConnectionStateChangeListener(connectionStateObserver);

        getConnection().writeCharacteristic(KBZ_SERVICE, KBZ_CHARACTERISTIC, new byte[]{1 & 0xFF});

        super.enable();
    }

    private double[] parse(byte[] bytes) {
        final int length = (bytes.length / 2) - 1;
        final double[] doubles = new double[length];
        int offset = 2;

        for (int i = 0; i < length; i++) {
            doubles[i] = RATIOS[i / 3] * shift(byteToDouble(bytes[offset++], bytes[offset++]));
        }

        return doubles;
    }

    private static double shift(double value) {
        return value > 32767 ? -(65534 - value) : value;
    }
}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.CharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2Agent;
import pt.uninova.s4h.citizenhub.data.BadPostureMeasurement;
import pt.uninova.s4h.citizenhub.data.GoodPostureMeasurement;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.data.SittingMeasurement;
import pt.uninova.s4h.citizenhub.data.StandingMeasurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.time.Accumulator;

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

    protected KbzBodyProtocol(BluetoothConnection connection, Dispatcher<Sample> dispatcher, UprightGo2Agent agent) {
        super(ID, connection, agent);

        this.motion = new Accumulator<>();
        this.position = new Accumulator<>();
        this.posture = new Accumulator<>();

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

        super.disable();
    }

    @Override
    public void enable() {
        getConnection().addCharacteristicListener(changeListener);
        getConnection().addCharacteristicListener(writeListener);

        getConnection().writeCharacteristic(KBZ_SERVICE, KBZ_CHARACTERISTIC, new byte[]{1 & 0xFF});

        super.enable();
    }

    private double[] parse(byte[] bytes) {
        final int length = (bytes.length / 2) - 1;
        final double[] doubles = new double[length];
        int offset = 2;

        for (int i = 0; i < length; i++) {
            doubles[i] = RATIOS[i % 3] * shift(byteToDouble(bytes[offset++], bytes[offset++]));
        }

        return doubles;
    }

    private static double shift(double value) {
        return value > 32767 ? -(65534 - value) : value;
    }
}

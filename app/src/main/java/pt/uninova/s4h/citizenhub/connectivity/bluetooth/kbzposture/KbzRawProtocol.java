package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.data.BadPostureMeasurement;
import pt.uninova.s4h.citizenhub.data.GoodPostureMeasurement;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.data.SittingMeasurement;
import pt.uninova.s4h.citizenhub.data.StandingMeasurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Dispatcher;

public class KbzRawProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.kbzposture.raw");
    final public static UUID KBZ_SERVICE = UUID.fromString("0000ff30-0000-1000-8000-00805f9b34fb");
    final public static String name = KbzRawProtocol.class.getSimpleName();

    final private static UUID KBZ_CHARACTERISTIC = UUID.fromString("0000ff35-0000-1000-8000-00805f9b34fb");
    final private static UUID KBZ_RAW_CHARACTERISTIC = UUID.fromString("0000ff38-0000-1000-8000-00805f9b34fb");


    final private static double ACC_RATIO = 4.0 / 32767;
    final private static double GYR_RATIO = 1000.0 / 32767;


    private MeasurementKind lastPosition;
    private MeasurementKind lastPosture;
    private LocalDateTime lastTimestamp;

    public KbzRawProtocol(BluetoothConnection connection, Dispatcher<Sample> sampleDispatcher, BluetoothAgent agent) {
        super(ID, connection, sampleDispatcher, agent);
    }

    private void attachObservers() {
        final BluetoothConnection connection = getConnection();

        connection.addCharacteristicListener(new BaseCharacteristicListener(KBZ_SERVICE, KBZ_CHARACTERISTIC) {
            @Override
            public void onWrite(byte[] value) {
                connection.enableNotifications(KBZ_SERVICE, KBZ_RAW_CHARACTERISTIC);
            }
        });

        connection.addCharacteristicListener(new BaseCharacteristicListener(KBZ_SERVICE, KBZ_RAW_CHARACTERISTIC) {
            @Override
            public void onChange(byte[] value) {
                final LocalDateTime now = LocalDateTime.now();

                if (lastTimestamp != null) {
                    final Duration duration = Duration.between(lastTimestamp, now);
                    final double seconds = duration.toNanos() * 0.000000001;

                    final Measurement<?> posture = lastPosture == MeasurementKind.GOOD_POSTURE ? new GoodPostureMeasurement(seconds) : new BadPostureMeasurement(seconds);
                    final Measurement<?> position = lastPosition == MeasurementKind.STANDING ? new StandingMeasurement(seconds) : new SittingMeasurement(seconds);

                    final Sample sample = new Sample(getAgent().getSource(), posture, position);

                    getSampleDispatcher().dispatch(sample);
                }

                final double[] parsed = parseAccelerometer(value);

                lastTimestamp = now;
                lastPosture = isGoodPosture(parsed[2], parsed[3]) ? MeasurementKind.GOOD_POSTURE : MeasurementKind.BAD_POSTURE;
                lastPosition = isStanding(parsed[1], parsed[2], parsed[3]) ? MeasurementKind.STANDING : MeasurementKind.SITTING;
            }
        });
    }


    private double byteToDouble(byte a, byte b) {
        return (b & 0xFF) << 8 | a & 0xFF;
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void enable() {
        attachObservers();

        lastPosition = null;
        lastPosture = null;
        lastTimestamp = null;

        getConnection().writeCharacteristic(KBZ_SERVICE, KBZ_CHARACTERISTIC, new byte[]{1 & 0xFF});

        super.enable();
    }

    private boolean isGoodPosture(double accY, double accZ) {
        return Math.abs(accY) > 0.88 && Math.abs(accZ) < 0.37;
    }

    private boolean isStanding(double accX, double accY, double accZ) {
        final boolean cond1 = Math.abs(accZ) > Math.abs(accX);
        final boolean cond2 = Math.abs(accZ) > Math.abs(accY);

        return !((cond1 && cond2) || ((cond1 || cond2) && (Math.abs(accZ) > 0.4)));
    }

    private double[] parse(byte[] bytes) {
        int offset = 0;

        return new double[]{
                byteToDouble(bytes[offset++], bytes[offset++]),
                ACC_RATIO * shift(byteToDouble(bytes[offset++], bytes[offset++])),
                ACC_RATIO * shift(byteToDouble(bytes[offset++], bytes[offset++])),
                ACC_RATIO * shift(byteToDouble(bytes[offset++], bytes[offset++])),
                GYR_RATIO * shift(byteToDouble(bytes[offset++], bytes[offset++])),
                GYR_RATIO * shift(byteToDouble(bytes[offset++], bytes[offset++])),
                GYR_RATIO * shift(byteToDouble(bytes[offset++], bytes[offset]))
        };
    }

    private double[] parseAccelerometer(byte[] bytes) {
        int offset = 0;

        return new double[]{
                byteToDouble(bytes[offset++], bytes[offset++]),
                ACC_RATIO * shift(byteToDouble(bytes[offset++], bytes[offset++])),
                ACC_RATIO * shift(byteToDouble(bytes[offset++], bytes[offset++])),
                ACC_RATIO * shift(byteToDouble(bytes[offset++], bytes[offset]))
        };
    }

    private double shift(double value) {
        return value > 32767 ? -(65534 - value) : value;
    }

}

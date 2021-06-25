package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class UprightGoProtocol extends BluetoothMeasuringProtocol{

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.kbzposture.raw");
    final public static UUID KBZ_SERVICE = UUID.fromString("0000ff30-0000-1000-8000-00805f9b34fb");

    final private static UUID KBZ_CHARACTERISTIC = UUID.fromString("0000ff35-0000-1000-8000-00805f9b34fb");
    final private static UUID KBZ_RAW_CHARACTERISTIC = UUID.fromString("0000ff38-0000-1000-8000-00805f9b34fb");


    final private static double ACC_RATIO = 4.0 / 32767;
    final private static double GYR_RATIO = 1000.0 / 32767;


    private MeasurementKind lastBodyPosition;
    private MeasurementKind lastPosture;
    private LocalDateTime lastTimestamp;

    public UprightGoProtocol(BluetoothConnection connection) {
        super(ID, connection);
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
                final double[] parsed = parseAccelerometer(value);
                final LocalDateTime now = LocalDateTime.now();

                if (isGoodPosture(parsed[2], parsed[3])) {
                    if (lastPosture == MeasurementKind.GOOD_POSTURE) {
                        final Duration duration = Duration.between(lastTimestamp, now);

                        getMeasurementDispatcher().dispatch(new Measurement(Date.from(Instant.from(now.atZone(ZoneId.systemDefault()))), MeasurementKind.GOOD_POSTURE, duration.toNanos() * 0.000000001));
                    } else {
                        lastPosture = MeasurementKind.GOOD_POSTURE;
                    }
                } else {
                    if (lastPosture == MeasurementKind.BAD_POSTURE) {
                        final Duration duration = Duration.between(lastTimestamp, now);

                        getMeasurementDispatcher().dispatch(new Measurement(Date.from(Instant.from(now.atZone(ZoneId.systemDefault()))), MeasurementKind.BAD_POSTURE, duration.toNanos() * 0.000000001));
                    } else {
                        lastPosture = MeasurementKind.BAD_POSTURE;
                    }
                }

                if (isStanding(parsed[1], parsed[2], parsed[3])) {
                    if (lastBodyPosition == MeasurementKind.STANDING) {
                        final Duration duration = Duration.between(lastTimestamp, now);

                        getMeasurementDispatcher().dispatch(new Measurement(Date.from(Instant.from(now.atZone(ZoneId.systemDefault()))), MeasurementKind.STANDING, duration.toNanos() * 0.000000001));
                    } else {
                        lastBodyPosition = MeasurementKind.STANDING;
                    }
                } else {
                    if (lastBodyPosition == MeasurementKind.SITTING) {
                        final Duration duration = Duration.between(lastTimestamp, now);

                        getMeasurementDispatcher().dispatch(new Measurement(Date.from(Instant.from(now.atZone(ZoneId.systemDefault()))), MeasurementKind.SITTING, duration.toNanos() * 0.000000001));
                    } else {
                        lastBodyPosition = MeasurementKind.SITTING;
                    }
                }

                lastTimestamp = now;
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

        lastBodyPosition = null;
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

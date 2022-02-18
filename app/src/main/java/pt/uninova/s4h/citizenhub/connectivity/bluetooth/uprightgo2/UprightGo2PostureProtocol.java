package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.data.BadPostureMeasurement;
import pt.uninova.s4h.citizenhub.data.GoodPostureMeasurement;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Dispatcher;

public class UprightGo2PostureProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.uprightgo2.posture");
    final public static UUID MEASUREMENTS_SERVICE = UUID.fromString("0000bac0-0000-1000-8000-00805f9b34fb"); //bac0
    final private static UUID POSTURE_CORRECTION = UUID.fromString("0000bac3-0000-1000-8000-00805f9b34fb"); //bac3
    final private static UUID CHARACTERISTIC = UUID.fromString("0000bac4-0000-1000-8000-00805f9b34fb"); //bac4

    /*
    ALL KNOWN SERVICES
    0x1800 -> Generic Access Service
        0x2A00 -> Device Name (UprightGO2)
    0x180A -> Generic Information Service
        0x2A24 -> Model Number
        0x2A25 -> Serial Number
        0x2A26 -> Firmware Revision
        0x2A27 -> Hardware Revision
        0x2A28 -> Software Revision
        0x2A29 -> Manufacturer Name
    BAA0 -> ???
        BAA6 -> Counter
    BAC0 -> Measurements Service
        BAC1 -> Calibration (Current User Posture will be new Good Posture Reference)
        0x00 to trigger/start
        BAC3 -> Posture Correction
        Vibration Status | Posture OK or NOT OK | Correcting Posture
        0x00 0x00 0x00 - Good Posture, not vibrating
        0x01 0x01 0x00 - Bad Posture, not vibrating (in countdown to vibrating)
        0x02 0x01 0x01 - Bad Posture, vibrating
        0x00 0x00 0x01 - Good Posture, User corrected position while vibrating, so the vibration
        was interrupted
        BAC4 -> Accelerometer Raw Data (acc1,acc2,acc3)
    BAB0 -> Vibration Service
        BAB2 -> Time Interval (5sec/15sec/30sec/60sec) + Vibration Angle(1 to 6, strict to relaxed)
        + Vibration Strength (1-3) 1-gentle, 2-medium, 3-strong
        + Vibration Pattern (9 different, check functions below, 00 defines patern 0, 80
        makes sensor show pattern 0 and also defines it)
        ! ONLY WORKS WHEN VIBRATION ON !
        BAB3 -> AUTO-CALIBRATION ON or OFF
        BAB5 -> Vibration On/off (0000/0101)

        DONE WITHIN THE UPRIGHT APP:
        - Minutes with Good Posture (It only counts when using the Upright App, we should set our
        own counters, BAA6 is an internal counter)
     */

    private MeasurementKind lastPosture;
    private Instant lastTimestamp;

    public UprightGo2PostureProtocol(BluetoothConnection connection, UprightGo2Agent agent) {
        super(ID, connection, agent);
    }

    public UprightGo2PostureProtocol(BluetoothConnection connection, Dispatcher<Sample> dispatcher, UprightGo2Agent agent) {
        super(ID, connection, dispatcher, agent);
    }

    private void attachObservers() {
        final BluetoothConnection connection = getConnection();

        if (connection.hasService(MEASUREMENTS_SERVICE)) {
            connection.enableNotifications(MEASUREMENTS_SERVICE, POSTURE_CORRECTION);
        }

        //handle sensor evaluation of good posture
        connection.addCharacteristicListener(new BaseCharacteristicListener(MEASUREMENTS_SERVICE, POSTURE_CORRECTION) {
            @Override
            public void onChange(byte[] value) {
                final Instant now = Instant.now();

                if (lastTimestamp != null) {
                    final Duration duration = Duration.between(lastTimestamp, now);
                    final double seconds = duration.toNanos() * 0.000000001;

                    final Measurement<?> posture = lastPosture == MeasurementKind.GOOD_POSTURE ? new GoodPostureMeasurement(seconds) : new BadPostureMeasurement(seconds);


                    final Sample sample = new Sample(getAgent().getSource(), posture);

                    getSampleDispatcher().dispatch(sample);
                }

                lastTimestamp = now;
                lastPosture = value[0] == 0 ? MeasurementKind.GOOD_POSTURE : MeasurementKind.BAD_POSTURE;
            }
        });
    }

    @Override
    public void disable() {
        final BluetoothConnection connection = getConnection();
        if (connection.hasService(MEASUREMENTS_SERVICE)) {
            connection.disableNotifications(MEASUREMENTS_SERVICE, POSTURE_CORRECTION);
        }
        if (connection.hasService(MEASUREMENTS_SERVICE)) {
            connection.disableNotifications(MEASUREMENTS_SERVICE, CHARACTERISTIC);
        }
        setState(ProtocolState.DISABLED);
    }

    @Override
    public void enable() {
        attachObservers();

        lastPosture = null;
        lastTimestamp = null;

        super.enable();
    }
}
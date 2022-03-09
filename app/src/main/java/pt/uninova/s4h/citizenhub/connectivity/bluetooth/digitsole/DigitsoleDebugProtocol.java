package pt.uninova.s4h.citizenhub.connectivity.bluetooth.digitsole;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothMeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import static android.content.ContentValues.TAG;

public class DigitsoleDebugProtocol extends BluetoothMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.digitsole.debug");
    final public static String name = DigitsoleDebugProtocol.class.getSimpleName();
    final private static UUID UUID_SERVICE_DATA = UUID.fromString("99ddcdab-a80c-4f94-be5d-c66b9fba40cf");
    final private static UUID UUID_SERVICE_DATA_COLLECTINGSTATE = UUID.fromString("99dd0014-a80c-4f94-be5d-c66b9fba40cf");
    final private static UUID UUID_SERVICE_DATA_COLLECTINGSTATE_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    final private static UUID UUID_SERVICE_DATA_BATTERY = UUID.fromString("99dd0016-a80c-4f94-be5d-c66b9fba40cf");
    final private static UUID UUID_SERVICE_DATA_ACTIVITYSTATE = UUID.fromString("99dd0105-a80c-4f94-be5d-c66b9fba40cf");
    final private static UUID UUID_SERVICE_DATA_ACTIVITYLOG = UUID.fromString("99dd0106-a80c-4f94-be5d-c66b9fba40cf");
    final private static UUID UUID_SERVICE_DATA_ACTIVITYLOG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private byte[] messageStartActivity = {0x01};
    private byte[] messageStopActivity = {(byte) 00};
    private byte[] messageRestartActivity = {0x02};
    private byte[] messageConfigureActivity = {(byte) 9218};
    private byte[] messageServiceDescriptor = {0x02, 0x00};
    int protocolCycle = 0;
    int activitySegment = 0;

    //TEST DEVICE: EB:2A:30:53:70:04
    //TODO: upon reconnecting, protocol must be restarted

    public DigitsoleDebugProtocol(BluetoothConnection connection, DigitsoleAgent agent) {
        super(ID, connection, agent);
    }

    private void attachObservers(BluetoothConnection connection) {

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_SERVICE_DATA,UUID_SERVICE_DATA_COLLECTINGSTATE) {
            @Override
            public void onChange(byte[] value) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(value).asReadOnlyBuffer();
                final double[] parsed = new double[]{
                        byteBuffer.get(0) & 0xFF, byteBuffer.get(1) & 0xFF, byteBuffer.get(2) & 0xFF
                };
                //System.out.println("Got something in the change listener SERVICE_DATA_COLLECTINGSTATE " + parsed[0] + " " + parsed[1] + " " + parsed[2]);
            }
        });

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_SERVICE_DATA,UUID_SERVICE_DATA_BATTERY) {
            @Override
            public void onChange(byte[] value) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(value).asReadOnlyBuffer();
                final double[] parsed = new double[]{
                        byteBuffer.get(0) & 0xFF, byteBuffer.get(1) & 0xFF, byteBuffer.get(2) & 0xFF
                };
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                //System.out.println("Got something in the change listener SERVICE_DATA_BATTERY " + parsed[0] + " " + parsed[1] + " " + parsed[2] + " " + timeStamp);
            }
        });

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_SERVICE_DATA,UUID_SERVICE_DATA_ACTIVITYSTATE) {
            @Override
            public void onChange(byte[] value) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(value).asReadOnlyBuffer();
                final double[] parsed = new double[]{
                        byteBuffer.get(0) & 0xFF, byteBuffer.get(1) & 0xFF, byteBuffer.get(2) & 0xFF, byteBuffer.get(3) & 0xFF,
                        byteBuffer.get(4) & 0xFF, byteBuffer.get(5) & 0xFF,
                };
                //System.out.println("Got something in the change listener SERVICE_DATA_ACTIVITYSTATE " + parsed[0] + " " + parsed[1] + " " + parsed[2]
                //    + " " + parsed[3] + " " + parsed[4] + " " + parsed[5]);
            }
            public void onRead(byte[] value) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(value).asReadOnlyBuffer();
                final double[] parsed = new double[]{
                        byteBuffer.get(0) & 0xFF, byteBuffer.get(1) & 0xFF, byteBuffer.get(2) & 0xFF, byteBuffer.get(3) & 0xFF,
                        byteBuffer.get(4) & 0xFF, byteBuffer.get(5) & 0xFF,
                };
                //System.out.println("Got something in the read listener SERVICE_DATA_ACTIVITYSTATE " + parsed[0] + " " + parsed[1] + " " + parsed[2]
                //        + " " + parsed[3] + " " + parsed[4] + " " + parsed[5]);
            }
        });

        connection.addCharacteristicListener(new BaseCharacteristicListener(UUID_SERVICE_DATA,UUID_SERVICE_DATA_ACTIVITYLOG) {
            @Override
            public void onChange(byte[] value) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(value).asReadOnlyBuffer();
                final double[] parsed = new double[67];
                //get the 67 bytes
                //System.out.println("Got something in the change listener SERVICE_DATA_ACTIVITYLOG");
                for (int i = 0; i <67; i++) {
                    parsed[i] = byteBuffer.get(i) & 0xFF;
                    System.out.print(" " + parsed[i]);
                }
                System.out.println("-");
                if ((int)parsed[0] == activitySegment){
                    System.out.println("At the segment: " + activitySegment);
                    activitySegment++;
                    int steps = (int) parsed[4];
                    System.out.println("Got Steps: " + steps);
                    //getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.STEPS, (double) steps));
                    //TODO: delete this. temporary.
                    //write to file here
                    writeToExternalFile(parsed);
                }
            }
            public void onRead(byte[] value) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(value).asReadOnlyBuffer();
                final double[] parsed = new double[67];
                //get the 67 bytes
                //System.out.println("Got something in the read listener SERVICE_DATA_ACTIVITYLOG");
                for (int i = 0; i <67; i++) {
                    parsed[i] = byteBuffer.get(i) & 0xFF;
                    System.out.print(" " + parsed[i]);
                }
                System.out.println("-");
                if ((int)parsed[0] == activitySegment){
                    System.out.println("At the segment: " + activitySegment);
                    activitySegment++;
                    int steps = (int) parsed[4];
                    System.out.println("Got Steps: " + steps);
                   //getMeasurementDispatcher().dispatch(new Measurement(new Date(), MeasurementKind.STEPS, (double) steps));
                    //TODO: delete this. temporary.
                    //write to file here
                    writeToExternalFile(parsed);
                }
            }
        });
        enableNotifications(connection);
    }

    private void writeToExternalFile(double[] message){
        //for this to work, permission to handle all data must be given (on the App settings)
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, "digitsole.txt");
        System.out.print("Writing to file!");
        System.out.println(" @ " + root.getAbsolutePath() + "/download");
        if (!file.exists()) {
            System.out.println("ENTERED HERE, NO FILE FOUND");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream f = new FileOutputStream(file, true);
            PrintWriter pw = new PrintWriter(f);
            for (int i = 0; i <67; i++) {
                pw.println(message[i]);
            }
            pw.println("--");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "******* File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enableNotifications(BluetoothConnection connection){
        //setting up measurements
        if (connection.hasService(UUID_SERVICE_DATA)) {
            connection.enableNotifications(UUID_SERVICE_DATA, UUID_SERVICE_DATA_COLLECTINGSTATE);
            connection.writeDescriptor(UUID_SERVICE_DATA, UUID_SERVICE_DATA_COLLECTINGSTATE,
                    UUID_SERVICE_DATA_COLLECTINGSTATE_DESCRIPTOR, messageServiceDescriptor);
            //System.out.println("Notifications Enabled for SERVICE_DATA_COLLECTINGSTATE");
            connection.enableNotifications(UUID_SERVICE_DATA, UUID_SERVICE_DATA_BATTERY);
            //System.out.println("Notifications Enabled for SERVICE_DATA_BATTERY");
            connection.enableNotifications(UUID_SERVICE_DATA, UUID_SERVICE_DATA_ACTIVITYSTATE);
            //System.out.println("Notifications Enabled for SERVICE_DATA_ACTIVITYSTATE");
            connection.enableNotifications(UUID_SERVICE_DATA, UUID_SERVICE_DATA_ACTIVITYLOG);
            connection.writeDescriptor(UUID_SERVICE_DATA, UUID_SERVICE_DATA_ACTIVITYLOG,
                    UUID_SERVICE_DATA_ACTIVITYLOG_DESCRIPTOR, messageServiceDescriptor);
            //System.out.println("Notifications Enabled for SERVICE_DATA_ACTIVITYLOG");
        }
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
    }

    @Override
    public void enable() {
        final BluetoothConnection connection = getConnection();
        attachObservers(connection);
        super.enable();

        final Handler h = new Handler(Looper.getMainLooper());

        setState(ProtocolState.ENABLED);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                //This is the sequence to enable data collecting every time the Protocol starts/restarts
                protocolCycle++;
                if (protocolCycle==1) {
                    //System.out.println("Soles start.");
                }
                if (protocolCycle==2) {
                    //System.out.println("Soles cleared/stopped previous activity.");
                    connection.writeCharacteristic(UUID_SERVICE_DATA, UUID_SERVICE_DATA_COLLECTINGSTATE, messageStopActivity);
                }
                if (protocolCycle==3){
                    //System.out.println("Soles configurated.");
                    connection.writeCharacteristic(UUID_SERVICE_DATA, UUID_SERVICE_DATA_COLLECTINGSTATE, messageConfigureActivity);
                }
                if (protocolCycle==4){
                    //System.out.println("Soles restarted.");
                    connection.writeCharacteristic(UUID_SERVICE_DATA, UUID_SERVICE_DATA_COLLECTINGSTATE, messageRestartActivity);
                }
                if (protocolCycle==5) {
                    //System.out.println("Soles started an activity.");
                    connection.writeCharacteristic(UUID_SERVICE_DATA, UUID_SERVICE_DATA_COLLECTINGSTATE, messageStartActivity);
                }
                if (protocolCycle>5){
                    //System.out.println("Logging current info from soles: " + protocolCycle + " protocol cycles.");
                    getConnection().readCharacteristic(UUID_SERVICE_DATA, UUID_SERVICE_DATA_ACTIVITYLOG);
                }
                if (getState() == ProtocolState.ENABLED) {
                    h.postDelayed(this, 2500);
                }
            }
        }, 0);
    }
}
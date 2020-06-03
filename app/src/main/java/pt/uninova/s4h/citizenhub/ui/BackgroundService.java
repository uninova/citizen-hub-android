package pt.uninova.s4h.citizenhub.ui;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAccessor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import datastorage.DatabaseHelperInterface;
import datastorage.DeviceDbHelper;
import datastorage.MeasurementsContract;
import datastorage.SourceContract;

import static android.content.ContentValues.TAG;
import static datastorage.DeviceContract.DeviceEntry.*;
import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_CHARACTERISTIC_NAME;
import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_MEASUREMENT_VALUE;
import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_SOURCE_UUID;
import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_TIMESTAMP;
import static datastorage.SourceContract.SourceEntry.COLUMN_SOURCE_INTERVAL;
import static datastorage.SourceContract.SourceEntry.COLUMN_SOURCE_TYPE;
import static pt.uninova.s4h.citizenhub.ui.Constants.ACTION_DATA_AVAILABLE;
import static pt.uninova.s4h.citizenhub.ui.Constants.ACTION_GATT_CONNECTED;
import static pt.uninova.s4h.citizenhub.ui.Constants.ACTION_GATT_DISCONNECTED;
import static pt.uninova.s4h.citizenhub.ui.Constants.ACTION_GATT_SERVICES_DISCOVERED;
import static pt.uninova.s4h.citizenhub.ui.Constants.CLIENT_CHARACTERISTIC_CONFIG;
import static pt.uninova.s4h.citizenhub.ui.Constants.EXTRA_DATA;

import datastorage.pdf;
import pt.uninova.s4h.citizenhub.ui.MiBand2;

public class BackgroundService extends Service{



    private static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    public ArrayList<BluetoothDevice> deviceList;
    private Handler mHandler;
    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000;
    public BluetoothLeScanner mBluetoothScanner;
    public int mConnectionState = Constants.STATE_DISCONNECTED;
    public Context myContext = null;
    BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt bluetoothGatt;

    HashMap<String, List<BluetoothGattCharacteristic>> FullDeviceList
            = new HashMap<>();
    public boolean isConnectedToGatt = false;
    private final IBinder mBinder = new LocalBinder();
    private int connectionState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    int hours_sitting = 0, minutes_sitting = 0, hours_goodPosture = 0, minutes_goodPosture = 0;
    int hours_standing = 0, minutes_standing = 0;
    int minutes_badPosture = 0, hours_badPosture = 0;



    /**
     * Core of the service, receives updates from the device's gatt server and handles them
     * Broadcast updates from data received through specified bluetooth characteristic
     *
     * @param action
     * @param characteristic
     */



    public final BluetoothGattCallback gattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    String intentAction;
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        intentAction = ACTION_GATT_CONNECTED;
                        connectionState = STATE_CONNECTED;
                        broadcastUpdate(intentAction);
                        Log.i(TAG, "Connected to GATT server.");
                        Log.i(TAG, "Attempting to start service discovery:");
                        bluetoothGatt.discoverServices();
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        intentAction = ACTION_GATT_DISCONNECTED;
                        connectionState = STATE_DISCONNECTED;
                        Log.i(TAG, "Disconnected from GATT server.");
                        broadcastUpdate(intentAction);
                    }
                }

                @Override
                // New services discovered
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    List<BluetoothGattService> AllServices = gatt.getServices();
                    List<BluetoothGattCharacteristic> mGattCharacteristics = new ArrayList<BluetoothGattCharacteristic>();
                    for (BluetoothGattService service : AllServices) {
                        Log.i("SERVICESS", service.getUuid().toString());
                        List<BluetoothGattCharacteristic> gattCharacteristics =
                                service.getCharacteristics();
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            Log.i("CHARACTERISTICS", gattCharacteristic.getUuid().toString());
                            mGattCharacteristics.add(gattCharacteristic);
                        }
                    }
                    FullDeviceList.put(gatt.getDevice().getAddress(), mGattCharacteristics);
                    Log.i("ARRAY DE DEVICES", String.valueOf(FullDeviceList.size()));
                    //   saveHashMap("ConnectedDevices", FullDeviceList);
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        BluetoothGattService service = gatt.getService(Constants.UUID_SERVICE_HEARTBEAT);
                        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID_HEART_RATE_MEASUREMENT);
                        if (characteristic != null) {
                            //SaveAvailableCharacteristics(gatt.getDevice(), "Heartrate");
                            String deviceType = "deviceType";
                            UpdateDevice(gatt.getDevice(), deviceType);
                            String sourceType = "HeartRate";
                            int sourceInterval = 10;
                            UpdateSource(gatt.getDevice(), characteristic.getUuid().toString(),sourceType, sourceInterval);
                            setCharacteristicNotification(characteristic, true);
                            broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                        }
                    } else {
                        Log.w(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                public void UpdateDevice(BluetoothDevice device, String deviceType){
                    DeviceDbHelper databaseHelper = new DeviceDbHelper(getApplicationContext());
                    SQLiteDatabase db= databaseHelper.getWritableDatabase();
                    String type = deviceType;
                    ContentValues values = new ContentValues();
                    String state = "todo";
                    values.put(COLUMN_DEVICE_TYPE, type);
                    db.update("devices",values,"address = ?",new String[]{String.valueOf(device.getAddress())});
                }

                public void UpdateSource(BluetoothDevice device, String sourceUUID, String sourceType, int sourceInterval){
                    DeviceDbHelper databaseHelper = new DeviceDbHelper(getApplicationContext());
                    SQLiteDatabase db= databaseHelper.getWritableDatabase();
                    String uuid = sourceUUID;
                    String address = device.getAddress();
                    String type = sourceType;
                    ContentValues values = new ContentValues();
                    int interval = sourceInterval;
                    values.put(SourceContract.SourceEntry.COLUMN_SOURCE_UUID, uuid);
                    values.put(SourceContract.SourceEntry.COLUMN_DEVICE_UUID, address);
                    values.put(COLUMN_SOURCE_TYPE, type);
                    values.put(COLUMN_SOURCE_INTERVAL, interval);
                    db.insertWithOnConflict("source",null,values, SQLiteDatabase.CONFLICT_IGNORE);
                }

                @Override
                // Result of a characteristic read operation
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                    }
                }

                @Override
// Characteristic notification
                public void onCharacteristicChanged(BluetoothGatt gatt,
                                                    BluetoothGattCharacteristic characteristic) {
                    broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                    setCharacteristicNotification(characteristic, true);
                }
            };

    /**
     * Broadcast actions through the application
     * @param action
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    /**
     * Broadcast updates from data received through specified bluetooth characteristic
     * @param action
     * @param characteristic
     */
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is carried out as per profile specifications.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d("heart_rate", "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d("heart_rate", "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            int HeartRateToString = heartRate;
            Log.d("heartrate", String.format("Received heart rate: %d", heartRate));
      //      Globals.setHeartRate(( String.valueOf(heartRate)));
            DeviceDbHelper databaseHelper = new DeviceDbHelper(getApplicationContext());
            SQLiteDatabase db= databaseHelper.getWritableDatabase();
            Calendar cal = Calendar.getInstance();
            String timestamp = cal.getTime().toString();
            String value  = String.valueOf(heartRate);
            String name = "HeartRate";
            String uuid = characteristic.getUuid().toString();

            ContentValues values = new ContentValues();
            values.put(COLUMN_TIMESTAMP, timestamp);
            values.put(COLUMN_MEASUREMENT_VALUE, value);
            values.put(COLUMN_CHARACTERISTIC_NAME, name);
            values.put(COLUMN_SOURCE_UUID, uuid);

            db.insertWithOnConflict(MeasurementsContract.MeasureEntry.TABLE_NAME,null,values, SQLiteDatabase.CONFLICT_IGNORE);
            //Log.d("TABLEWTF", String.valueOf(Home.databaseHelper.getReadableDatabase().query(MeasurementsContract.MeasureEntry.TABLE_NAME, null, null,null,null, null, null)));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder;
                stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
                        stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
    }

    /**
     * Enabled automatic data notifications from a specified characteristic
     * @param characteristic
     * @param enabled
     */
    //todo Acidental recursive? name of function = function inside lol
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(descriptor);
        } else {
            BluetoothGattDescriptor descriptor2 = characteristic.getDescriptor(
                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
            if (descriptor2 != null) {
                if (BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE != null) {
                    descriptor2.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    bluetoothGatt.writeDescriptor(descriptor2);
                }
            }
        }
    }


    /**
     * Binder to allow communication between the service and the activity
     */
    public class LocalBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }
/*
    public void SaveAvailableCharacteristics(BluetoothDevice device, String NewCharacteristic){
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db= databaseHelper.getWritableDatabase();
        String characteristics = NewCharacteristic;
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEVICE_CHARACTERISTICS, characteristics);
        db.update("devices",values,"address = ?",new String[]{String.valueOf(device.getAddress())});

    }
 */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Handle the results of bluetooth scans
     */
    public final ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result.getDevice() != null && /*result.getScanRecord().getDeviceName() != null &&*/ result.getDevice().getAddress() != null) {
                Log.i("HERE"," " + result.getScanRecord().getDeviceName());
                if (!deviceList.contains(result.getDevice())) {

                    deviceList.add(result.getDevice());
                    Log.i("infos", " " + result.getDevice().getName());
                    DeviceDbHelper databaseHelper = new DeviceDbHelper(getApplicationContext());
                    //TODO put insert into function
                    SQLiteDatabase db= databaseHelper.getWritableDatabase();
                    String name = result.getScanRecord().getDeviceName();
                    String address = result.getDevice().getAddress();
                    String state = "scanned";
                    String type;
                   /*if(result.getDevice().getName().contains("MI Band 2")){
                         type = "MI Band 2";
                    }else {*/
                        type = "n/a";
                    //}
                    Log.d("testeasd", type);
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_DEVICE_NAME, name);
                    values.put(COLUMN_DEVICE_ADDRESS, address);
                    values.put(COLUMN_DEVICE_STATE, state);
                    values.put(COLUMN_DEVICE_TYPE, type);
                    db.insertWithOnConflict("devices",null,values, SQLiteDatabase.CONFLICT_IGNORE);

                    Log.d("TABLEWTF", String.valueOf(Home.deviceDbHelper.getReadableDatabase().query(TABLE_NAME, null, null,null,null, null, null)));
                    SendList();
                }
            }
        }

        public Cursor ViewData(SQLiteOpenHelper databaseHelperInterface){
            SQLiteDatabase db = databaseHelperInterface.getReadableDatabase();
            String query = "Select * from measurements";
            Cursor cursor = db.rawQuery(query, null);
            return cursor;
        }

        public void goPDF(){
            int steps = 0, calories = 0, heartRateAvg = 0;
            int min_HR = 0, max_HR = 0;
            float distance = 0;
            int counter_heartRateAvg = 0;


            int counter = 0;
            int aux_minutes_before = 0;
            int aux_minutes_now = 0;
            int before = -1;
            int now = -1;

            //set current day
            LocalDate dayCurrent = LocalDate.now();
            CalendarDay currentDay = CalendarDay.from(dayCurrent);

            //getValues
            //sitting time and good posture TODO

            //steps, calories, distance
            SQLiteOpenHelper databaseHelperInterface = new DatabaseHelperInterface(getApplicationContext());
            Cursor cursor = ViewData(databaseHelperInterface);
            if(cursor.getCount()==0) {
                Log.i("Home_SetValues", "No info from steps.");
            }
            else {
                while (cursor.moveToNext()){
                    Log.i("Home_SetValues", "Valores da tabela:" + cursor.getString(2)
                            + cursor.getString(3)
                            + cursor.getString(4));
                    if (cursor.getString(4).equals("Steps"))
                    {
                        String timestamp = cursor.getString(2);
                        int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                        String month = timestamp.substring(4, 7);
                        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                                .withLocale(Locale.ENGLISH);
                        TemporalAccessor accessor = parser.parse(month);
                        int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                        int day = Integer.parseInt(timestamp.substring(8,10));
                        int value = Integer.parseInt(cursor.getString(3));
                        CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                        Log.i("Home_SetValues", dayExample.toString() + " " + currentDay.toString());
                        if (dayExample.equals(currentDay) && value > steps)
                        {
                            steps = value;
                        }
                    }
                    else if (cursor.getString(4).equals("Calories"))
                    {
                        String timestamp = cursor.getString(2);
                        int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                        String month = timestamp.substring(4, 7);
                        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                                .withLocale(Locale.ENGLISH);
                        TemporalAccessor accessor = parser.parse(month);
                        int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                        int day = Integer.parseInt(timestamp.substring(8,10));
                        int value = Integer.parseInt(cursor.getString(3));
                        CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                        Log.i("Home_SetValues", dayExample.toString() + " " + currentDay.toString());
                        if (dayExample.equals(currentDay) && value > calories)
                        {
                            calories = value;
                        }
                    }
                    else if (cursor.getString(4).equals("Distance"))
                    {
                        String timestamp = cursor.getString(2);
                        int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                        String month = timestamp.substring(4, 7);
                        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                                .withLocale(Locale.ENGLISH);
                        TemporalAccessor accessor = parser.parse(month);
                        int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                        int day = Integer.parseInt(timestamp.substring(8,10));
                        int value = Integer.parseInt(cursor.getString(3));
                        CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                        Log.i("Home_SetValues", dayExample.toString() + " " + currentDay.toString());
                        if (dayExample.equals(currentDay) && value > distance)
                        {
                            distance = value;
                        }
                    }
                    else if (cursor.getString(4).equals("HeartRate"))
                    {
                        String timestamp = cursor.getString(2);
                        int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                        String month = timestamp.substring(4, 7);
                        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                                .withLocale(Locale.ENGLISH);
                        TemporalAccessor accessor = parser.parse(month);
                        int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                        int day = Integer.parseInt(timestamp.substring(8,10));
                        int value = Integer.parseInt(cursor.getString(3));
                        CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                        Log.i("Home_SetValues", dayExample.toString() + " " + currentDay.toString());
                        if (dayExample.equals(currentDay))
                        {
                            heartRateAvg += value;
                            counter_heartRateAvg++;
                            if (min_HR == 0 || max_HR == 0) {
                                min_HR = value;
                                max_HR = value;
                            }
                            if (value < min_HR && value > 0)
                                min_HR = value;
                            if (value > max_HR)
                                max_HR = value;
                        }
                    }
                    else if (cursor.getString(4).equals("BackPosture"))
                    {
                        Log.i("Reports_drawCheese", "GOT HERE3");
                        String timestamp = cursor.getString(2);
                        int year = Integer.parseInt(timestamp.substring(timestamp.length() - 4));
                        String month = timestamp.substring(4, 7);
                        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                                .withLocale(Locale.ENGLISH);
                        TemporalAccessor accessor = parser.parse(month);
                        int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                        int day = Integer.parseInt(timestamp.substring(8, 10));
                        int value = Integer.parseInt(cursor.getString(3));
                        int hours = Integer.parseInt(timestamp.substring(11, 13));
                        int minute = Integer.parseInt(timestamp.substring(14, 16));
                        int minutes = hours * 60 + minute;
                        CalendarDay dayExample = CalendarDay.from(year, month_number, day);
                        Log.i("CRA", dayExample.toString() + " " + currentDay.toString());
                        if (dayExample.equals(currentDay)) {
                            if (counter == 0) {
                                //Log.i("debug_positions",line);
                                aux_minutes_before = minutes;
                                before = value;
                                counter++;
                                continue;
                            }
                            counter++;
                            aux_minutes_now = minutes;
                            now = value;

                            //Log.i("debug_positions", String.valueOf(aux_minutes_before));
                            //Log.i("debug_positions", String.valueOf(aux_minutes_now));
                            //Log.i("debug_positions", before);
                            //Log.i("debug_positions", now);

                            count_minutes("BackPosture", before, now,
                                    aux_minutes_before, aux_minutes_now);

                            before = now;
                            aux_minutes_before = aux_minutes_now;
                        }
                    }
                    else if (cursor.getString(4).equals("BodyPosture")) {
                        Log.i("Reports_drawCheese", "GOT HERE4");
                        String timestamp = cursor.getString(2);
                        int year = Integer.parseInt(timestamp.substring(timestamp.length() - 4));
                        String month = timestamp.substring(4, 7);
                        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                                .withLocale(Locale.ENGLISH);
                        TemporalAccessor accessor = parser.parse(month);
                        int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                        int day = Integer.parseInt(timestamp.substring(8, 10));
                        int value = Integer.parseInt(cursor.getString(3));
                        int hours = Integer.parseInt(timestamp.substring(11, 13));
                        int minute = Integer.parseInt(timestamp.substring(14, 16));
                        int minutes = hours * 60 + minute;
                        CalendarDay dayExample = CalendarDay.from(year, month_number, day);
                        Log.i("CRA", dayExample.toString() + " " + currentDay.toString());
                        if (dayExample.equals(currentDay)) {
                            if (counter == 0) {
                                aux_minutes_before = minutes;
                                before = value;
                                counter++;
                                continue;
                            }
                            counter++;
                            aux_minutes_now = minutes;
                            now = value;

                            //Log.i("debug_positions", String.valueOf(aux_minutes_before));
                            //Log.i("debug_positions", String.valueOf(aux_minutes_now));
                            //Log.i("debug_positions", before);
                            //Log.i("debug_positions", now);

                            count_minutes("BodyPosture", before, now,
                                    aux_minutes_before, aux_minutes_now);

                            before = now;
                            aux_minutes_before = aux_minutes_now;
                        }
                    }
                    else
                    {
                        Log.i("Reports_drawGauge","characteristic not identified");
                    }
                }
                if (counter_heartRateAvg > 0)
                    heartRateAvg = heartRateAvg / counter_heartRateAvg;
                distance = distance / 1000;
            }

            //final values
            int final_hours_goodPosture = minutes_goodPosture / 60;
            int final_minutes_goodPosture = minutes_goodPosture % 60;
            int final_hours_sitting = (minutes_goodPosture + minutes_badPosture) / 60;
            int final_minutes_sitting = (minutes_goodPosture + minutes_badPosture) % 60;

            //if (min_HR < 30)
            //    min_HR = 46;

            pdf PDF = new pdf(heartRateAvg, steps, (int)distance, calories,final_hours_sitting,
                    final_minutes_sitting,final_hours_goodPosture,final_minutes_goodPosture,
                    Home.loggedEmail, 26, 10000,
                    minutes_standing / 60, minutes_standing % 60,
                    min_HR,max_HR, "MinTime", "maxTime");
            PDF.createPDF();
        }

        public void count_minutes(String characteristic, int before, int now, int aux_minutes_before, int aux_minutes_now)
        {
            Log.i("debug_positions", "GOT HERe " + before + " " + now + " " +
                    aux_minutes_before + " " + aux_minutes_now);
            if (now == 1 && characteristic.equals("BodyPosture"))
            {
                minutes_standing += aux_minutes_now-aux_minutes_before;
                Log.i("debug_positions", "Contei standing " + String.valueOf(minutes_standing));
            }
            if (now == 0 && characteristic.equals("BodyPosture"))
            {
                return; //TODO currently, it's ignoring sitting time
            }
            if (now == 1 && characteristic.equals("BackPosture"))
            {
                minutes_goodPosture += aux_minutes_now-aux_minutes_before;
                Log.i("debug_positions", "Contei good " + String.valueOf(minutes_goodPosture));
            }
            if (now == 0 && characteristic.equals("BackPosture"))
            {
                minutes_badPosture += aux_minutes_now-aux_minutes_before;
                Log.i("debug_positions", "Contei bad " + String.valueOf(minutes_badPosture));
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            switch (errorCode) {
                case SCAN_FAILED_ALREADY_STARTED:
                    Log.d(TAG, "already started");
                    break;
                case SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                    Log.d(TAG, "cannot be registered");
                    break;
                case SCAN_FAILED_FEATURE_UNSUPPORTED:
                    Log.d(TAG, "power optimized scan not supported");
                    break;
                case SCAN_FAILED_INTERNAL_ERROR:
                    Log.d(TAG, "internal error");
                    break;
            }
        }
    };

    /**
     * Broadcast a list of all devices scanned to activity
     */
    public void SendList() {
        Intent intent = new Intent("IntentFilterSendData");
        intent.putParcelableArrayListExtra("DEVICE_LIST", deviceList);
        LocalBroadcastManager.getInstance(BackgroundService.this).sendBroadcast(intent);
    }


    public List<BluetoothDevice> CheckConnectedDevices (BluetoothManager bluetoothManager) {
        List<BluetoothDevice> connected_devices_list = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
        for(BluetoothDevice device : connected_devices_list) {
            Log.d("PREVCONNECTED:", device.getName() + ": " +  device.getAddress());
        }
        return connected_devices_list;
    }

    public void CheckDeviceConnection (BluetoothManager bluetoothManager, BluetoothDevice device){
        int a = bluetoothManager.getConnectionState(device,bluetoothGatt.GATT);
        if(a == BluetoothProfile.STATE_CONNECTED ) {
            Log.d("PREVCONNECTED:", "Woah, device:" + device + " is currently connected" );
        }
        else if(a == BluetoothProfile.STATE_CONNECTING){
            Log.d("PREVCONNECTED:", "Woah, device:" + device + " is trying to connect" );
        }
        else {
            Log.d("PREVCONNECTED:",  device + " not connected" );
        }

    }

    /**
     * Scan Bluetooth Devices
     * @param enable
     */
    public void scanDevice(final boolean enable) {
        if (enable) {
            Log.i("infos", "got into scanDevice");
            Handler scanHandler = new Handler();
            scanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothScanner.stopScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            //this scan doesn't have a filter
            mBluetoothScanner.startScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothScanner.stopScan(mLeScanCallback);
        }
    }

    /**
     *Tries to connect to device's gatt server
     * @param device
     */
    public void getData(BluetoothDevice device){
        if(device.getName().contains("MI Band 2")) {
            device.connectGatt(getApplicationContext(),true, new MiBand2());
        }else {
            bluetoothGatt = device.connectGatt(getApplicationContext(), true, gattCallback);
        }
        }
    /**
     * Handles all Bluetooth Scanner's needs to execute the scan
     */
    public void PreScan() {
        deviceList = new ArrayList<>();
        mHandler = new Handler();
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        mBluetoothAdapter = Home.mBluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            return;
        }
        mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();
    }

}
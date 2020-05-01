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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import datastorage.DatabaseHelperInterface;
import datastorage.MeasurementsContract;
import datastorage.SourceContract;
import datastorage.pdf;

import static android.content.ContentValues.TAG;
import static datastorage.DeviceContract.DeviceEntry.COLUMN_DEVICE_ADDRESS;
import static datastorage.DeviceContract.DeviceEntry.COLUMN_DEVICE_NAME;
import static datastorage.DeviceContract.DeviceEntry.COLUMN_DEVICE_STATE;
import static datastorage.DeviceContract.DeviceEntry.COLUMN_DEVICE_TYPE;
import static datastorage.DeviceContract.DeviceEntry.TABLE_NAME;
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

public class BackgroundService extends Service implements BackgroundServiceInterface {


    private static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    private static final long SCAN_PERIOD = 10000;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private final IBinder mBinder = new LocalBinder();
    public ArrayList<BluetoothDevice> deviceList;
    /**
     * Handle the results of bluetooth scans
     */
    public final ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result.getDevice() != null && result.getDevice().getName() != null && result.getDevice().getAddress() != null) {
                if (!deviceList.contains(result.getDevice())) {
                    String asd = "imprime";

                    pdf PDF = new pdf(70, 1500, 2, 500,3,50,2,45);
                    PDF.createPDF();
                    deviceList.add(result.getDevice());
                    Log.i("infos", result.getDevice().getName());
                    SQLiteOpenHelper SQLiteOpenHelper = new DatabaseHelperInterface(getApplicationContext());
                    //TODO put insert into function
                    SQLiteDatabase db = SQLiteOpenHelper.getWritableDatabase();
                    String name = result.getDevice().getName();
                    String address = result.getDevice().getAddress();
                    String state = "scanned";
                    String type;
                    if (result.getDevice().getName().contains("MI Band 2")) {
                        type = "MI Band 2";
                    } else {
                        type = "n/a";
                    }
                    Log.d("testeasd", type);
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_DEVICE_NAME, name);
                    values.put(COLUMN_DEVICE_ADDRESS, address);
                    values.put(COLUMN_DEVICE_STATE, state);
                    values.put(COLUMN_DEVICE_TYPE, type);
                    db.insertWithOnConflict("devices", null, values, SQLiteDatabase.CONFLICT_IGNORE);

                    Log.d("TABLEWTF", String.valueOf(Home.SQLiteOpenHelper.getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null)));
                    SendList();
                }
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
    public BluetoothLeScanner mBluetoothScanner;
    public int mConnectionState = Constants.STATE_DISCONNECTED;
    public Context myContext = null;
    public boolean isConnectedToGatt = false;
    BluetoothAdapter mBluetoothAdapter;
    HashMap<String, List<BluetoothGattCharacteristic>> FullDeviceList
            = new HashMap<>();
    private Handler mHandler;
    private boolean mScanning;
    private BluetoothGatt bluetoothGatt;
    private int connectionState = STATE_DISCONNECTED;
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
                            UpdateSource(gatt.getDevice(), characteristic.getUuid().toString(), sourceType, sourceInterval);
                            setCharacteristicNotification(characteristic, true);
                            broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                        }
                    } else {
                        Log.w(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                public void UpdateDevice(BluetoothDevice device, String deviceType) {
                    SQLiteOpenHelper SQLiteOpenHelper = new DatabaseHelperInterface(getApplicationContext());
                    SQLiteDatabase db = SQLiteOpenHelper.getWritableDatabase();
                    String type = deviceType;
                    ContentValues values = new ContentValues();
                    String state = "todo";
                    values.put(COLUMN_DEVICE_TYPE, type);
                    db.update("devices", values, "address = ?", new String[]{String.valueOf(device.getAddress())});
                }

                public void UpdateSource(BluetoothDevice device, String sourceUUID, String sourceType, int sourceInterval) {
                    SQLiteOpenHelper SQLiteOpenHelper = new DatabaseHelperInterface(getApplicationContext());
                    SQLiteDatabase db = SQLiteOpenHelper.getWritableDatabase();
                    String uuid = sourceUUID;
                    String address = device.getAddress();
                    String type = sourceType;
                    ContentValues values = new ContentValues();
                    int interval = sourceInterval;
                    values.put(SourceContract.SourceEntry.COLUMN_SOURCE_UUID, uuid);
                    values.put(SourceContract.SourceEntry.COLUMN_DEVICE_UUID, address);
                    values.put(COLUMN_SOURCE_TYPE, type);
                    values.put(COLUMN_SOURCE_INTERVAL, interval);
                    db.insertWithOnConflict("source", null, values, SQLiteDatabase.CONFLICT_IGNORE);
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
     *
     * @param action
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    /**
     * Broadcast updates from data received through specified bluetooth characteristic
     *
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
            SQLiteOpenHelper SQLiteOpenHelper = new DatabaseHelperInterface(getApplicationContext());
            SQLiteDatabase db = SQLiteOpenHelper.getWritableDatabase();
            Calendar cal = Calendar.getInstance();
            String timestamp = cal.getTime().toString();
            String value = String.valueOf(heartRate);
            String name = "HeartRate";
            String uuid = characteristic.getUuid().toString();

            ContentValues values = new ContentValues();
            values.put(COLUMN_TIMESTAMP, timestamp);
            values.put(COLUMN_MEASUREMENT_VALUE, value);
            values.put(COLUMN_CHARACTERISTIC_NAME, name);
            values.put(COLUMN_SOURCE_UUID, uuid);

            db.insertWithOnConflict("measurements", null, values, SQLiteDatabase.CONFLICT_IGNORE);
            Log.d("TABLEWTF", String.valueOf(Home.SQLiteOpenHelper.getReadableDatabase().query(MeasurementsContract.MeasureEntry.TABLE_NAME, null, null, null, null, null, null)));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder;
                stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
                        stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
    }

    /**
     * Enabled automatic data notifications from a specified characteristic
     *
     * @param characteristic
     * @param enabled
     */
    //todo Acidental recursive? name of function = function inside lol
    @Override
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
     * Broadcast a list of all devices scanned to activity
     */
    @Override
    public void SendList() {
        Intent intent = new Intent("IntentFilterSendData");
        intent.putParcelableArrayListExtra("DEVICE_LIST", deviceList);
        LocalBroadcastManager.getInstance(BackgroundService.this).sendBroadcast(intent);
    }

    @Override
    public List<BluetoothDevice> CheckConnectedDevices(BluetoothManager bluetoothManager) {
        List<BluetoothDevice> connected_devices_list = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
        for (BluetoothDevice device : connected_devices_list) {
            Log.d("PREVCONNECTED:", device.getName() + ": " + device.getAddress());
        }
        return connected_devices_list;
    }

    @Override
    public void CheckDeviceConnection(BluetoothManager bluetoothManager, BluetoothDevice device) {
        int a = bluetoothManager.getConnectionState(device, bluetoothGatt.GATT);
        if (a == BluetoothProfile.STATE_CONNECTED) {
            Log.d("PREVCONNECTED:", "Woah, device:" + device + " is currently connected");
        } else if (a == BluetoothProfile.STATE_CONNECTING) {
            Log.d("PREVCONNECTED:", "Woah, device:" + device + " is trying to connect");
        } else {
            Log.d("PREVCONNECTED:", device + " not connected");
        }

    }

    /**
     * Scan Bluetooth Devices
     *
     * @param enable
     */
    @Override
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
     * Tries to connect to device's gatt server
     *
     * @param device
     */
    @Override
    public void getData(BluetoothDevice device) {
        if (device.getName().contains("MI Band 2")) {
            device.connectGatt(getApplicationContext(), true, new MiBand2());
        } else {
            bluetoothGatt = device.connectGatt(getApplicationContext(), true, gattCallback);
        }
    }

    /**
     * Handles all Bluetooth Scanner's needs to execute the scan
     */
    @Override
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

    /**
     * Binder to allow communication between the service and the activity
     */
    public class LocalBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }

}
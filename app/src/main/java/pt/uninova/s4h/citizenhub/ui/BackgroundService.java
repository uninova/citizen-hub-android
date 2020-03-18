package pt.uninova.s4h.citizenhub.ui;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BackgroundService extends Service {

    public ArrayList<BluetoothDevice> deviceList;
    private Handler mHandler;
    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mBluetoothScanner;
    public int mConnectionState = Constants.STATE_DISCONNECTED;
    public Context myContext = null;
    BluetoothAdapter mBluetoothAdapter;

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result.getDevice() != null && result.getDevice().getName() != null && result.getDevice().getAddress() != null) {
                if (!deviceList.contains(result.getDevice())) {
                    deviceList.add(result.getDevice());
                    Log.i("infos",result.getDevice().getName());
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

    public void SendList() {
        Intent intent = new Intent("IntentFilterSendData");
        intent.putParcelableArrayListExtra("DEVICE_LIST", deviceList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void scanDevice(final boolean enable) {
        if (enable) {
            Log.i("infos","got into scanDevice");
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

    public void PreScan (){
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
     * Empty constructor
     */
    public BackgroundService() {
        super();
    }

    public BackgroundService(Context context) {
        myContext = context;
    }
}

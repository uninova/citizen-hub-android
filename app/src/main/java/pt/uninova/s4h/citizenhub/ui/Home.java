package pt.uninova.s4h.citizenhub.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAccessor;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.TimerTask;

import com.google.android.material.snackbar.Snackbar;

import datastorage.DatabaseHelperInterface;
import datastorage.DeviceDbHelper;
import datastorage.DeviceProvider;
import datastorage.MeasurementProvider;
import datastorage.MeasurementsDbHelper;
import datastorage.SourceDbHelper;
import datastorage.SourceProvider;
import kbz.s4h.S4HDCM.BLELibrary;
import pt.uninova.s4h.citizenhub.ui.devices.DevicesFragment;
import pt.uninova.s4h.citizenhub.ui.login.LoginActivity;


public class Home extends AppCompatActivity implements DevicesFragment.OnDataPass {
    private static final int MY_PERMISSION_RESPONSE = 2;
    private static final int REQUEST_ENABLE_BT = 1;
    public static Context homecontext;
    public static DeviceDbHelper deviceDbHelper;
    public static DatabaseHelperInterface databaseHelperInterface;
    public static MeasurementsDbHelper measurementsDbHelper;
    public static SQLiteOpenHelper SQLiteOpenHelper;

    private AppBarConfiguration mAppBarConfiguration;
    int counter = 0;
    public static FloatingActionButton fab;
    public static BluetoothManager mBluetoothManager;
    public static BackgroundService mService;
    public static boolean loggedIn = false;
    public static boolean bypassForTesting = false;
    public static String loggedEmail = "person@uninova.pt";
    public static Boolean foundDevice = false;
    SourceDbHelper sourceDbHelper;
    public static BLELibrary ble;

    private final Handler handler = new Handler(Looper.getMainLooper());
    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public Handler mHandler = new Handler(Looper.myLooper());
    boolean mBound = false;
    // and returned in the Activity's onRequestPermissionsResult()
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BackgroundService.LocalBinder binder = (BackgroundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (loggedIn == false && bypassForTesting == false) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homecontext=getApplicationContext();

        SQLiteOpenHelper = new DatabaseHelperInterface(getApplicationContext());
        measurementsDbHelper = new MeasurementsDbHelper(getApplicationContext());
        sourceDbHelper = new SourceDbHelper(getApplicationContext());
        deviceDbHelper = new DeviceDbHelper(getApplicationContext());

        DeviceProvider deviceProvider = new DeviceProvider();
        MeasurementProvider measurementProvider = new MeasurementProvider();
        SourceProvider sourceProvider = new SourceProvider();
        measurementProvider.openAndQueryDatabase(measurementsDbHelper);
        deviceProvider.openAndQueryDatabase(deviceDbHelper);
        sourceProvider.openAndQueryDatabase(sourceDbHelper);
        Log.d("WRITETEST", "writen to" + this.getFilesDir().getAbsolutePath());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BleCheckPermissions();

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_reports, R.id.nav_profile,
                R.id.nav_devices, R.id.nav_body, R.id.nav_posture, R.id.nav_about, R.id.nav_logout,
                R.id.nav_quit)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        run_service_crono(); // runs service on startup of activity
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth, useless option?
                } else if (!mBluetoothAdapter.isEnabled()) {
                    // Device Bluetooth is not enabled.
                    Snackbar.make(view, "Please enable Bluetooth in your phone settings.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    // Bluetooth is enabled. Search.
                    if (mBound) {
                        mService.CheckConnectedDevices(mBluetoothManager);
                        //search device logic
                        mService.PreScan();
                        mService.scanDevice(true);
                        //set up search window
                        setActionBarTitle("Searching Devices...");
                        fab.hide();
                        //start timer 5 sec. and then show fab
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (foundDevice == true) {
                                            setActionBarTitle("Select a Device");
                                            foundDevice = false; //reset to variable
                                        } else {
                                            setActionBarTitle("Connected Devices");
                                            FragmentManager fragmentManager = getSupportFragmentManager();
                                        }
                                    }
                                });
                                fab.show();
                            }
                        }, 6000);
                    } else {
                    }
                }

            }
        });
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void BleCheckPermissions() {

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.w("BleActivity", "Location access not granted!");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_RESPONSE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_RESPONSE);
                Log.w("ExternalStorage", "External Storage granted!");
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_RESPONSE);
            Log.w("ExternalStorage", "External Storage granted!");
        }
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE NOT SUPPORTED", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onDataPass(BluetoothDevice device) {
        Log.d("OndataPass", device.getName());
        /*
        DeviceDbHelper deviceDbHelper = new DeviceDbHelper(this);
        SQLiteDatabase db= deviceDbHelper.getWritableDatabase();
        int connected = 1;
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_CONNECTED, connected);
        db.update("devices",values,"address = ?",new String[]{String.valueOf(device.getAddress())});
         */
        mService.mBluetoothScanner.stopScan(mService.mLeScanCallback);
        mService.getData(device);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(getApplicationContext(), BackgroundService.class);
        startService(intent);
        if (!mBound) {
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    public void run_service_crono() {
        sendNotification(); //calls methods from NotificationHelper
    }

    public void sendNotification() {
        NotificationHelper helper = new NotificationHelper(Home.this);
        helper.createNotification("Citizen Hub", "Reading Heart Rate...");
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
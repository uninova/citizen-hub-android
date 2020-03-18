package pt.uninova.s4h.citizenhub.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import java.util.TimerTask;

import pt.uninova.s4h.citizenhub.ui.login.LoginActivity;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    int counter = 0;
    public static FloatingActionButton fab;
    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public Handler mHandler = new Handler(Looper.myLooper());
    public static BluetoothManager mBluetoothManager;
    BackgroundService mService;
    boolean mBound = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final int MY_PERMISSION_RESPONSE = 2;
    private static final int REQUEST_ENABLE_BT = 1;
    public static boolean loggedIn = false;
    public static boolean bypassForTesting = false;
    public static String loggedEmail = "person@uninova.pt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (loggedIn == false && bypassForTesting == false)
        {
            Intent intent = new Intent (this, LoginActivity.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
                R.id.nav_devices, R.id.nav_about, R.id.nav_logout,
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
                        //Intent intent = new Intent(Home.this, Temp_MainActivity.class);
                        //startActivity(intent);
                        mService.PreScan();
                        mService.scanDevice(true);
                    } else {
                    }


                }

            }
        });
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public void BleCheckPermissions(){

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.w("BleActivity", "Location access not granted!");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_RESPONSE);
            }
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
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
        }
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent (getApplicationContext(), BackgroundService.class);
        startService(intent);
        if (!mBound)
        {
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

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

    public void run_service_crono()
    {
        //run chronometer
        //((Chronometer) findViewById(R.id.chrono)).start();
        //running task on alarm (reading probably)
        //Timer myTimer = new Timer("MyTimer", true);
        //myTimer.scheduleAtFixedRate(new MyTask(), 0, 60000);
        //Intent mServiceIntent = new Intent(getApplicationContext(), mService.getClass());
        //if (!isMyServiceRunning(mService.getClass())) {
        //    startService(mServiceIntent);
        //}
        sendNotification(); //calls methods from NotificationHelper

    }

    public void runHeartRate (View view)
    {
        //just for testing
        //Log.i("FSL_debug", "got here, running heart rate");
    }

    private class MyTask extends TimerTask {

        public void run(){
            //task to create Toasts, check method
            (new MyAsyncTask()).execute();
            //running service
            //BackgroundService_backup mSensorService = new BackgroundService_backup(getApplicationContext());
            //Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
            //if (!isMyServiceRunning(mSensorService.getClass())) {
            //    startService(mServiceIntent);
            //}
            sendNotification(); //calls methods from NotificationHelper
        }
    }

    public void sendNotification() {
                NotificationHelper helper = new NotificationHelper(Home.this);
                helper.createNotification("Citizen Hub","Reading Heart Rate...");
                //placeholder message
    }

    //check whether the service is running or not, if service already in running mode then no need to start it again
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    class MyAsyncTask extends android.os.AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            runOnUiThread(new Runnable(){
                public void run() {
                    //debug task to show Toasts, periodically
                    counter++;
                    Toast.makeText(getApplicationContext(), "Service runned " + counter + " times.", Toast.LENGTH_LONG).show();
                    //end of debug task
                }
            });
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu); //the three dots on the left of the action bar
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
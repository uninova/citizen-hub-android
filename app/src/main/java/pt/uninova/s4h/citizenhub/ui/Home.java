package pt.uninova.s4h.citizenhub.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    int counter = 0;
    public static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
    }

    public void run_service_crono()
    {
        //run chronometer
        //((Chronometer) findViewById(R.id.chrono)).start();
        //running task on alarm (reading probably)
        Timer myTimer = new Timer("MyTimer", true);
        myTimer.scheduleAtFixedRate(new MyTask(), 0, 60000);
    }

    private class MyTask extends TimerTask {

        public void run(){
            //task to create Toasts, check method
            (new MyAsyncTask()).execute();
            //running service
            BackgroundService mSensorService = new BackgroundService(getApplicationContext());
            Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
            if (!isMyServiceRunning(mSensorService.getClass())) {
                startService(mServiceIntent);
            }
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
package pt.uninova.s4h.citizenhub;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTraining;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBinder;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class MainActivity extends AppCompatActivity implements CitizenHubServiceBound {

    private CitizenHubService citizenHubService;
    private ServiceConnection connection;

    private AppBarConfiguration appBarConfiguration;

    @Override
    public CitizenHubService getService() {
        return citizenHubService;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        NavController navController = ((NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.summary_fragment, R.id.report_master_fragment, R.id.lumbar_extension_training_fragment, R.id.device_list_fragment, R.id.accounts_fragment, R.id.about_fragment)
                        .setOpenableLayout(drawerLayout)
                        .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //testing

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTimeTesting", false)) {
            // <---- run your one time code here

            LumbarExtensionTrainingRepository lumbarRepository = new LumbarExtensionTrainingRepository(getApplication());
            lumbarRepository.add(new LumbarExtensionTraining(LocalDateTime.now(), 93838, (float) 95.9, 90,60));
            MeasurementRepository measurementRepository = new MeasurementRepository(getApplication());
            Date now = Date.from(Instant.now());

            measurementRepository.add(new Measurement(now, MeasurementKind.BAD_POSTURE, 60.0));
            measurementRepository.add(new Measurement(now, MeasurementKind.GOOD_POSTURE, 100.0));
            measurementRepository.add(new Measurement(now, MeasurementKind.CALORIES, 550.0));
            measurementRepository.add(new Measurement(now, MeasurementKind.DISTANCE, 1350.0));
            measurementRepository.add(new Measurement(now, MeasurementKind.HEART_RATE, 73.0));
            measurementRepository.add(new Measurement(now, MeasurementKind.STEPS, 993.5));
            measurementRepository.add(new Measurement(now, MeasurementKind.RESPIRATION_RATE, 14.0));
            measurementRepository.add(new Measurement(now, MeasurementKind.BLOOD_PRESSURE,4.0));
            measurementRepository.add(new Measurement(now, MeasurementKind.BAD_POSTURE, 730.0));
            measurementRepository.add(new Measurement(now, MeasurementKind.SITTING, 700.0));

            // mark first time has ran.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTimeTesting", true);
            editor.commit();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = ((NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();

        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void startService() {
        this.connection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                citizenHubService = ((CitizenHubServiceBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                MainActivity.this.connection = null;
            }
        };

        final Intent intent = new Intent(this, CitizenHubService.class);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        NavController navController = ((NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();

        if (navController.getCurrentBackStackEntry().getDestination().getId() == R.id.summary_fragment) {
            moveTaskToBack(false);
        } else if (navController.getCurrentBackStackEntry().getDestination().getId() == R.id.device_list_fragment) {
            moveTaskToBack(false);
        } else {
            navController.popBackStack();
        }
    }

    private void stopService() {
        unbindService(connection);
    }
}
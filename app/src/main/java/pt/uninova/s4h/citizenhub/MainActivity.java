package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.Random;

import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.summary_fragment, R.id.report_fragment, R.id.user_fragment, R.id.device_list_fragment, R.id.about_fragment)
                        .setOpenableLayout(drawerLayout)
                        .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //
        // SAMPLING CODE
        //
        Handler handler = new Handler(getMainLooper());
        int delay = 10000;
        final Random random = new Random();
        final MeasurementRepository repo = new MeasurementRepository(getApplication());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MeasurementKind kind = MeasurementKind.find(random.nextInt(7));

                Measurement measurement = new Measurement(new Date(), kind, (double) random.nextInt(200));
                System.out.println(measurement.getKind().toString() + ":" + measurement.getTimestamp().toString() + ":" + measurement.getValue());

                repo.add(measurement);

                handler.postDelayed(this, random.nextInt(delay));
            }
        }, random.nextInt(delay));
        //
        // END SAMPLING CODE
        //
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
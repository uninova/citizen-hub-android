package pt.uninova.s4h.citizenhub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

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
                new AppBarConfiguration.Builder(R.id.summary_fragment, R.id.report_master_fragment, R.id.lumbar_extension_training_fragment, R.id.device_list_fragment, R.id.settings_fragment,R.id.smart4health_fragment, R.id.accounts_fragment, R.id.about_fragment)
                        .setOpenableLayout(drawerLayout)
                        .build();



        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = ((NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();

        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        NavController navController = ((NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();

        if (navController.getCurrentBackStackEntry().getDestination().getId() == R.id.summary_fragment) {
            moveTaskToBack(false);
        } else {
            navController.popBackStack();
        }
    }

}
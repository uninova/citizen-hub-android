package pt.uninova.s4h.citizenhub;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.lang.D4LException;
import care.data4life.sdk.listener.ResultListener;

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
                new AppBarConfiguration.Builder(R.id.summary_fragment, R.id.report_master_fragment, R.id.lumbar_extension_training_fragment, R.id.device_list_fragment, R.id.settings_fragment, R.id.smart4health_fragment, R.id.accounts_fragment, R.id.about_fragment)
                        .setOpenableLayout(drawerLayout)
                        .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final NavigationView navView = findViewById(R.id.nav_view);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final Menu nav_Menu = navView.getMenu();

        Data4LifeClient client = Data4LifeClient.getInstance();

        client.isUserLoggedIn(new ResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                nav_Menu.findItem(R.id.smart4health_fragment).setVisible(aBoolean);
            }

            @Override
            public void onError(@NonNull D4LException e) {
                nav_Menu.findItem(R.id.smart4health_fragment).setVisible(false);
            }
        });
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
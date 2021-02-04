package pt.uninova.s4h.citizenhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.lang.D4LException;
import care.data4life.sdk.listener.ResultListener;

import static care.data4life.sdk.Data4LifeClient.D4L_AUTH;

public class LobbyActivity extends AppCompatActivity {

    private Button loginButton;


    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lobby);
        NavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    }
}

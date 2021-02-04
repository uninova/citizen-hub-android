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
    private NavController navController;

//    private void authenticate() {
//        final Data4LifeClient client = Data4LifeClient.getInstance();
//
//        client.isUserLoggedIn(new ResultListener<Boolean>() {
//            @Override
//            public void onSuccess(Boolean value) {
//                if (value) {
//                    final Intent intent = new Intent(LobbyActivity.this, MainActivity.class);
//
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
//                    startActivity(intent);
//                    LobbyActivity.this.finish();
//                } else {
//                    loginButton.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onError(D4LException exception) {
//                loginButton.setVisibility(View.VISIBLE);
//                exception.printStackTrace();
//            }
//        });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == D4L_AUTH) {
//                authenticate();
//            }
//        }
//    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lobby);
        NavigationView navView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

//        loginButton = findViewById(R.id.fragment_authentication_button_login);
//        loginButton.setVisibility(View.GONE);
//        loginButton.setOnClickListener((View v) -> {
//            final Intent loginIntent = Data4LifeClient.getInstance().getLoginIntent(getApplicationContext(), null);
//
//            startActivityForResult(loginIntent, D4L_AUTH);
//        });

//        authenticate();
    }
}

package pt.uninova.s4h.citizenhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.lang.D4LException;
import care.data4life.sdk.listener.ResultListener;

import static care.data4life.sdk.Data4LifeClient.D4L_AUTH;

public class LobbyActivity extends AppCompatActivity {

    private Button loginButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lobby);

        loginButton = findViewById(R.id.activity_lobby_button_login);

        loginButton.setOnClickListener(v -> {
            Intent loginIntent = Data4LifeClient.getInstance().getLoginIntent(getApplicationContext(), null);
            startActivityForResult(loginIntent, D4L_AUTH);
        });

        final Data4LifeClient client = Data4LifeClient.getInstance();

        client.isUserLoggedIn(new ResultListener<Boolean>() {

            @Override
            public void onSuccess(Boolean value) {
                if (value) {
                    startMainActivity();
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(D4LException exception) {
                loginButton.setVisibility(View.VISIBLE);
                exception.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == D4L_AUTH) {
            if (resultCode == RESULT_OK) {
                startMainActivity();
            } else {
                loginButton.setVisibility(View.VISIBLE);
            }
        }

    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}

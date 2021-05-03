package pt.uninova.s4h.citizenhub.ui.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pt.uninova.s4h.citizenhub.R;

public class SplashActivity extends AppCompatActivity {

    private static final int splashTimeOut = 2500;
    private ImageView logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.authentication_fragment_img_s4h_logo_image_id);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LobbyActivity.class);
                startActivity(i);
                finish();
            }
        }, splashTimeOut);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_logo);
        logo.startAnimation(anim);
    }
}
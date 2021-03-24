package pt.uninova.s4h.citizenhub.ui.lobby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.Timer;
import java.util.TimerTask;

import pt.uninova.s4h.citizenhub.R;

public class SlideActivity extends AppCompatActivity implements ViewPagerController {

    public static ViewPager viewPager;
    SlideViewPagerAdapter adapter;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3500;
    int currentPage = 0;
    Timer timer;
    ViewPagerController viewPagerController = this;
    Handler handler = new Handler();
    Runnable update = new Runnable() {
        Button button;
        public void run() {


            if (currentPage == 3) {
                currentPage = 0;
            } else {
                viewPager.setCurrentItem(currentPage++, true);
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        viewPager = findViewById(R.id.viewpager);
        adapter = new SlideViewPagerAdapter(this);
        Button button = findViewById(R.id.btnGetStarted);

        viewPager.setAdapter(adapter);

        if (isOpenAlready()) {
            Intent intent = new Intent(SlideActivity.this, LobbyActivity.class);
            //     intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            SharedPreferences.Editor editor = getSharedPreferences("slide", MODE_PRIVATE).edit();
            editor.putBoolean("slide", true);
            editor.apply();
        }
        StartTimer();
    }

    public void StartTimer() {
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(update);

            }
        }, DELAY_MS, PERIOD_MS);
    }


    public void StopTimer() {
        if (update != null) {
            Log.d("entrou", "entrou");
            timer.cancel();
            handler.removeCallbacksAndMessages(null);
            update = null;
            handler = null;
        }
    }


    private boolean isOpenAlready() {

        SharedPreferences sharedPreferences = getSharedPreferences("slide", MODE_PRIVATE);
        return sharedPreferences.getBoolean("slide", false);

    }

    @Override
    public void stopTimerTask() {
        StopTimer();
    }

}

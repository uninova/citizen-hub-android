package pt.uninova.s4h.citizenhub.ui.lobby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import pt.uninova.s4h.citizenhub.R;

public class SlideActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    SlideViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        viewPager = findViewById(R.id.viewpager);
        adapter = new SlideViewPagerAdapter(this);
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


    }

    private boolean isOpenAlready() {

        SharedPreferences sharedPreferences = getSharedPreferences("slide", MODE_PRIVATE);
        return sharedPreferences.getBoolean("slide", false);

    }
}

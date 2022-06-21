package pt.uninova.s4h.citizenhub.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import pt.uninova.s4h.citizenhub.AboutFragment;
import pt.uninova.s4h.citizenhub.DataFragment;
import pt.uninova.s4h.citizenhub.GreetingFragment;
import pt.uninova.s4h.citizenhub.SettingsFragment;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {

    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GreetingFragment();
            case 1:
                return new SettingsFragment();
            case 2:
                return new DataFragment();
            default:
                return new AboutFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
package pt.uninova.s4h.citizenhub;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    private int numberPages = 4;

    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GreetingFragment();
            case 1:
                return new DataFragment();
            case 2:
                return new SettingsFragment();
            case 3:
                return new AboutFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return numberPages;
    }
}

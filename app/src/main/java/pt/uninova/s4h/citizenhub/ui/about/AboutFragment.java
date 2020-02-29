package pt.uninova.s4h.citizenhub.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import pt.uninova.s4h.citizenhub.ui.R;
import pt.uninova.s4h.citizenhub.ui.home.HomeFragment;

public class AboutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //to keep fragment, to be changed later...
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        ///

        ///show S4H website in a new window, browser. Back returns to home fragment
        Intent i = new Intent (Intent.ACTION_VIEW, Uri.parse("https://www.smart4health.eu"));
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        return null;
    }
}
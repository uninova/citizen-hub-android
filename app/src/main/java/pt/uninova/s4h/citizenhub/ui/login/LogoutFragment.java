package pt.uninova.s4h.citizenhub.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import pt.uninova.s4h.citizenhub.ui.Home;
import pt.uninova.s4h.citizenhub.ui.R;
import pt.uninova.s4h.citizenhub.ui.home.HomeFragment;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.content.DialogInterface.OnClickListener;

public class LogoutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //to keep fragment, to be changed later...
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        //

        //are you sure dialog before exiting
        OnClickListener dialogClickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case BUTTON_POSITIVE:
                        Home.loggedIn = false;
                        Intent intent = new Intent (LogoutFragment.this.getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case BUTTON_NEGATIVE:
                        //Does nothing
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Are you sure you want to logout of your account?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

        return null;
    }
}
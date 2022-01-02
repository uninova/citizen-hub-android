package pt.uninova.s4h.citizenhub.ui.accounts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.lang.D4LException;
import care.data4life.sdk.listener.Callback;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.ui.lobby.LobbyActivity;

public class Smart4HealthAccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.smart4health_account_fragment, container, false);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.smart4health_account_fragment, menu);

        menu.findItem(R.id.smart4health_account_fragment_menu_log_out).setOnMenuItemClickListener((MenuItem item) -> {
            final Data4LifeClient client = Data4LifeClient.getInstance();

            client.logout(new Callback() {
                @Override
                public void onSuccess() {
                    final Activity activity = requireActivity();
                    final Intent intent = new Intent(activity, LobbyActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(intent);
                    activity.finish();
                }

                @Override
                public void onError(@NonNull D4LException exception) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Failure to log out.", Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                    });
                }
            });

            return true;
        });
    }

}

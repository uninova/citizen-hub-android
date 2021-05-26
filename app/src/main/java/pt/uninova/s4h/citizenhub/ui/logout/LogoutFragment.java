package pt.uninova.s4h.citizenhub.ui.logout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.lang.D4LException;
import care.data4life.sdk.listener.Callback;
import pt.uninova.s4h.citizenhub.ui.lobby.LobbyActivity;
import pt.uninova.s4h.citizenhub.R;

public class LogoutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_logout, container, false);

        final Button logOutButton = view.findViewById(R.id.button_logOut);

        logOutButton.setOnClickListener(v -> {
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
                public void onError(D4LException exception) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Failure to log out.", Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                    });
                }
            });

        });

        return view;
    }
}

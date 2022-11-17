package pt.uninova.s4h.citizenhub.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pt.uninova.s4h.citizenhub.R;

public class DeviceConfigurationConnectFragment extends Fragment {
    private DeviceViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_connect, container, false);
        Button connectButton = view.findViewById(R.id.button_connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.addAgent(model.getConfigurationAgent().getValue());
                getActivity().getSupportFragmentManager().beginTransaction().remove(DeviceConfigurationConnectFragment.this).commitNow();

            }
        });

        return view;
    }
}

package pt.uninova.s4h.citizenhub.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;

public class DeviceConfigurationUdiSetterFragment extends Fragment {

    private Button setUdi;
    private Button clearUdi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_unique_identifier_setter, container, false);
        setUdi = view.findViewById(R.id.udi_set_button);
        setUdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(DeviceConfigurationUdiSetterFragment.this.requireView()).navigate(DeviceConfigurationUdiSetterFragmentDirections.actionDeviceConfigurationUdiSetterFragmentToDeviceConfigurationFragment());

            }
        });

        clearUdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(DeviceConfigurationUdiSetterFragment.this.requireView()).navigate(DeviceConfigurationUdiSetterFragmentDirections.actionDeviceConfigurationUdiSetterFragmentToDeviceConfigurationFragment());

            }
        });

        return view;
    }

}

package pt.uninova.s4h.citizenhub.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;

public class DeviceConfigurationUniqueIdentifierFragment extends Fragment {

    private LinearLayout udiButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_unique_identifier_item, container, false);
        udiButton = view.findViewById(R.id.unique_device_identifier_layout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        udiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(DeviceConfigurationUniqueIdentifierFragment.this.requireView()).navigate(DeviceConfigurationFragmentDirections.actionDeviceConfigurationFragmentToDeviceConfigurationUdiSetterFragment());
            }
        });
    }
}

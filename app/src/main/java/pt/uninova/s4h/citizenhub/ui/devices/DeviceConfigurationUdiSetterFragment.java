package pt.uninova.s4h.citizenhub.ui.devices;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;

public class DeviceConfigurationUdiSetterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_unique_identifier_setter, container, false);
        Button setUdi = view.findViewById(R.id.udi_set_button);
        Button clearUdi = view.findViewById(R.id.udi_clear_button);
        EditText udiText = view.findViewById(R.id.fragment_device_configuration_udi_setter_textview);

        udiText.setOnClickListener(view1 -> {
            udiText.requestFocus();

            InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view1, InputMethodManager.SHOW_IMPLICIT);
        });

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

package pt.uninova.s4h.citizenhub.ui.devices;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DeviceConfigurationUdiSetterFragment extends Fragment {
    private DeviceViewModel model;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.device_configuration_udi_setter_menu, menu);
        MenuItem clearUdi = menu.findItem(R.id.device_configuration_udi_clear_item);
        clearUdi.setOnMenuItemClickListener((MenuItem item) -> {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    model.getSelectedDeviceAgent().getSettingsManager().set("udi-serial-number", null);
                    model.getSelectedDeviceAgent().getSettingsManager().set("udi-system", null);
                    model.getSelectedDeviceAgent().getSettingsManager().set("udi-device-identifier", null);

                }
            });
            Navigation.findNavController(DeviceConfigurationUdiSetterFragment.this.requireView()).navigate(DeviceConfigurationUdiSetterFragmentDirections.actionDeviceConfigurationUdiSetterFragmentToDeviceConfigurationFragment());
            return true;
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.fragment_device_configuration_unique_identifier_setter, container, false);
        Button setUdi = view.findViewById(R.id.udi_set_button);
        EditText serialNumber = view.findViewById(R.id.value_serial_number_edit_text);
        EditText deviceIdentifier = view.findViewById(R.id.value_device_identifier_text);
        Spinner systemSpinner = view.findViewById(R.id.spinner_udi_system);

        model.getSelectedDeviceAgent().getSettingsManager().get("udi-serial-number", new Observer<String>() {
            @Override
            public void observe(String value) {
                if (value != null) {
                    serialNumber.setHint(value);
                }
            }
        });

        serialNumber.setOnClickListener(view1 -> {
            serialNumber.requestFocus();

            InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view1, InputMethodManager.SHOW_IMPLICIT);
        });

        deviceIdentifier.setOnClickListener(view1 -> {
            deviceIdentifier.requestFocus();

            InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view1, InputMethodManager.SHOW_IMPLICIT);
        });

        model.getSelectedDeviceAgent().getSettingsManager().get("udi-device-identifier", new Observer<String>() {
            @Override
            public void observe(String value) {
                if (value != null) {
                    deviceIdentifier.setHint(value);
                }
            }
        });


        model.getSelectedDeviceAgent().getSettingsManager().get("udi-system", new Observer<String>() {
            @Override
            public void observe(String value) {
                if (value != null) {
                    systemSpinner.setSelection(Integer.parseInt(value));
                }
            }
        });
        systemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getChildAt(0) != null) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorS4HDarkBlue));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setUdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        model.getSelectedDeviceAgent().getSettingsManager().set("udi-serial-number", serialNumber.getText().toString());
                        model.getSelectedDeviceAgent().getSettingsManager().set("udi-system", String.valueOf(systemSpinner.getSelectedItemPosition()));
                        model.getSelectedDeviceAgent().getSettingsManager().set("udi-device-identifier", deviceIdentifier.getText().toString());

                    }
                });

                Navigation.findNavController(DeviceConfigurationUdiSetterFragment.this.requireView()).navigate(DeviceConfigurationUdiSetterFragmentDirections.actionDeviceConfigurationUdiSetterFragmentToDeviceConfigurationFragment());

            }
        });
        return view;
    }

}

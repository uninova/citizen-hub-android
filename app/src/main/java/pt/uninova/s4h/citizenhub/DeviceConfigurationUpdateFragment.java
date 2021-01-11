package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

public class DeviceConfigurationUpdateFragment extends DeviceConfigurationFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_device_configuration_update, container, false);

        deleteDevice = result.findViewById(R.id.buttonDelete);
        updateDevice = result.findViewById(R.id.buttonConfiguration);

        setupViews(result);

        setupText();

        testingFillFeatures();

        setupFeatures();

        updateDevice.setOnClickListener(view -> {
            //TODO: Update Device Data
            Navigation.findNavController(getView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());
        });

        deleteDevice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                model.delete(changedDevice);
                Navigation.findNavController(getView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());
            }
        });

        return result;
    }
}

package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

public class DeviceConfigurationAddFragment extends DeviceConfigurationFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_device_configuration_add, container, false);

        connectDevice = result.findViewById(R.id.buttonConfiguration);

        setupViews(result);

        setupText();

        testingFillFeatures();

        setupFeatures();

        connectDevice.setOnClickListener(view -> {
            model.apply();

            //TODO: set features, DB?

            Navigation.findNavController(getView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceListFragment());
        });

        return result;
    }
}

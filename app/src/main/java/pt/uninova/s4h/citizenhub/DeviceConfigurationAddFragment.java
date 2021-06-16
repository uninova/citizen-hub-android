package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Objects;

import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceConfigurationAddFragment extends DeviceConfigurationFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_add, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        connectDevice = view.findViewById(R.id.buttonConfiguration);
        setupViews(view);

        setupText();

//        testingFillFeatures();

        setupFeatures(Objects.requireNonNull(((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().getDeviceAgentMap().get(model.getSelectedDevice().getValue())));

        connectDevice.setOnClickListener(v -> {
            model.apply();
            setFeaturesState(model.getSelectedAgent());
            //TODO: set features, DB?

            Navigation.findNavController(requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceListFragment());
        });

        return view;
    }
}

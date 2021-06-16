package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Objects;

import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceConfigurationUpdateFragment extends DeviceConfigurationFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_update, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        deleteDevice = view.findViewById(R.id.buttonDelete);
        updateDevice = view.findViewById(R.id.buttonConfiguration);

        setupViews(view);

        setupText();

//        testingFillFeatures();

        setupFeatures(Objects.requireNonNull(((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().getDeviceAgentMap().get(model.getSelectedDevice().getValue())));

        updateDevice.setOnClickListener(v -> {
            setFeaturesState(model.getSelectedAgent(requireActivity()));

            //TODO: Update Device Data
            Navigation.findNavController(requireView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());
        });

        deleteDevice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                model.delete(model.getSelectedDevice().getValue());
                Navigation.findNavController(getView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());
            }
        });

        return view;
    }
}

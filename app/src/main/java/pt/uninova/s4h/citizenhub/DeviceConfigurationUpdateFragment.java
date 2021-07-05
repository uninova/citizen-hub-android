package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceConfigurationUpdateFragment extends DeviceConfigurationFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_update, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        deleteDevice = view.findViewById(R.id.buttonDelete);
        updateDevice = view.findViewById(R.id.buttonConfiguration);
        AgentOrchestrator agentOrchestrator = ((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator();

        setupViews(view);
        setupText();
        setListViewFeaturesAdapter();
        updateDevice.setOnClickListener(v -> {
            setFeaturesState();
            Navigation.findNavController(requireView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());
        });

        deleteDevice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                model.delete(model.getSelectedDevice().getValue());
                AgentOrchestrator agentOrchestrator = ((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator();
                agentOrchestrator.deleteDeviceFromMap(model.getSelectedDevice().getValue());
                Navigation.findNavController(getView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());
            }
        });

        return view;
    }


}

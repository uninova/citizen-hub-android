package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceConfigurationAddFragment extends DeviceConfigurationFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_add, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        ProgressBar progressBar = view.findViewById(R.id.add_pprogressBar);
        CitizenHubService service = ((CitizenHubServiceBound) requireActivity()).getService();
        AgentOrchestrator agentOrchestrator = service.getAgentOrchestrator();

        connectDevice = view.findViewById(R.id.buttonConfiguration);
        setupViews(view);
        setupText();

        model.createAgent(service, agent -> {
            agentOrchestrator.add(model.getSelectedDevice().getValue(), agent);

            requireActivity().runOnUiThread(() -> {
                loadFeatureState();
                connectDevice.setOnClickListener(v -> {
                    agent.enable();
                    model.apply();

                    saveFeaturesChosen();

                    Navigation.findNavController(requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceListFragment());
                });

            });
        });

        return view;
    }
}


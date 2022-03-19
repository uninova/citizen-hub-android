package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class DeviceConfigurationAddFragment extends DeviceConfigurationFragment {

    private DeviceViewModel model;

    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_add, container, false);

        progressBar = view.findViewById(R.id.add_pprogressBar);

        connectDevice = view.findViewById(R.id.buttonConfiguration);
        setupViews(view);
        setupText();

        progressBar.setVisibility(View.VISIBLE);
        connectDevice.setText(R.string.fragment_device_configuration_add_loading_features_text);

        model.getAgentOrchestrator().observe(getViewLifecycleOwner(), this::onAgentOrchestrator);

        return view;
    }

    private void onAgentOrchestrator(AgentOrchestrator agentOrchestrator) {
        final Observer<Device> deviceObserver = new Observer<Device>() {

            @Override
            public void onChanged(Device device) {
                agentOrchestrator.identify(device, agent -> {
                    requireActivity().runOnUiThread(() -> loadSupportedFeatures());

                    connectDevice.setOnClickListener(view -> {
                        agent.enable();
                        agentOrchestrator.add(agent);

                        saveFeaturesChosen();

                        Navigation.findNavController(DeviceConfigurationAddFragment.this.requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceListFragment());
                    });

                    progressBar.setVisibility(View.INVISIBLE);
                    connectDevice.setText(R.string.fragment_device_configuration_connect_option_text);
                });

                model.getSelectedDevice().removeObserver(this);
            }
        };

        model.getSelectedDevice().observe(getViewLifecycleOwner(), deviceObserver);
    }
}


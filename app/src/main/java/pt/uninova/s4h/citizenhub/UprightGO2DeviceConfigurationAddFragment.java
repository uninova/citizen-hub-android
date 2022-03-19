package pt.uninova.s4h.citizenhub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2CalibrationProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2VibrationProtocol;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class UprightGO2DeviceConfigurationAddFragment extends DeviceConfigurationFragment {
/*
    private ProgressBar progressBar;

    private DeviceViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        model.getAgentOrchestrator().observe(getViewLifecycleOwner(), this::onAgentOrchestrator);
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

        return view;
    }

    private void onAgentOrchestrator(AgentOrchestrator agentOrchestrator) {
        final Observer<Device> deviceObserver = new Observer<Device>() {

            @Override
            public void onChanged(Device device) {
                agentOrchestrator.identify(device, agent -> {

                });

                model.getSelectedDevice().removeObserver(this);
            }
        };

        model.getSelectedDevice().observe(getViewLifecycleOwner(), deviceObserver);


        agentOrchestrator.add(device, value -> requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UprightGO2DeviceConfigurationAddFragment.this.loadSupportedFeatures();

                connectDevice.setOnClickListener(v -> {
                    value.enable();
                    agentOrchestrator.add(device);

                    UprightGO2DeviceConfigurationAddFragment.this.saveFeaturesChosen();

                    if (device.getName().startsWith("UprightGO2")) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.fragment_device_configuration_sensor_calibration_text)
                                .setMessage(getString(R.string.fragment_device_configuration_warning_calibration_text) +
                                        getString(R.string.fragment_device_configuration_warning_calibration_text2))
                                .setPositiveButton(R.string.fragment_device_configuration_dialog_option_calibrate, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //TODO calibrate and animate
                                        dialog = ProgressDialog.show(getContext(), "", getString(R.string.fragment_device_configuration_dialog_calibrating_message), false);

                                        UprightGo2Agent agent = (UprightGo2Agent) value;

                                        //Send Message calibration
                                        agent.enableProtocol(new UprightGo2CalibrationProtocol(agent));

                                        //default - first vibration settings when adding device
                                        boolean vibration = true;
                                        int angle = 1;
                                        int interval = 5;
                                        int pattern = 0;
                                        boolean showPattern = true;
                                        int strength = 1;

                                        //Send Message vibration settings
                                        agent.enableProtocol(new UprightGo2VibrationProtocol(agent, vibration, angle, interval, showPattern, pattern, strength));

                                        handler.sendMessageDelayed(new Message(), 2500);

                                    }
                                })
                                .setIcon(R.drawable.img_citizen_hub_logo_png)
                                .show();
                    }
                    Navigation.findNavController(UprightGO2DeviceConfigurationAddFragment.this.requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceListFragment());
                });

                progressBar.setVisibility(View.INVISIBLE);
                connectDevice.setText(R.string.fragment_device_configuration_connect_option_text);
            }
        }));

    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        final AgentOrchestrator agentOrchestrator = getService().getAgentOrchestrator();

    }
*/
}


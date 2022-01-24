package pt.uninova.s4h.citizenhub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2CalibrationProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2VibrationProtocol;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceConfigurationAddFragment extends DeviceConfigurationFragment {

    private ProgressDialog dialog;
    //TODO just for testing purposes, delete later
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (dialog.isShowing()) {
                dialog.dismiss();
                Toast.makeText(getActivity(),getString(R.string.fragment_device_configuration_add_calibration_completed_toast), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_add, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();

        CitizenHubService service = ((CitizenHubServiceBound) requireActivity()).getService();
        AgentOrchestrator agentOrchestrator = service.getAgentOrchestrator();

        ProgressBar progressBar = view.findViewById(R.id.add_pprogressBar);

        connectDevice = view.findViewById(R.id.buttonConfiguration);
        setupViews(view);
        setupText();

        progressBar.setVisibility(View.VISIBLE);
        connectDevice.setText(R.string.fragment_device_configuration_add_loading_features_text);

        agentOrchestrator.add(device, value -> requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DeviceConfigurationAddFragment.this.loadSupportedFeatures();

                connectDevice.setOnClickListener(v -> {

                    value.enable();
                    model.apply();

                    DeviceConfigurationAddFragment.this.saveFeaturesChosen();

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
                    Navigation.findNavController(DeviceConfigurationAddFragment.this.requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceListFragment());
                });

                progressBar.setVisibility(View.INVISIBLE);
                connectDevice.setText(R.string.fragment_device_configuration_connect_option_text);
            }
        }));

        return view;
    }
}


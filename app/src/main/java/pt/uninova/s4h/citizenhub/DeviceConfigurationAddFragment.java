package pt.uninova.s4h.citizenhub;

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

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2CalibrationProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2VibrationProtocol;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class DeviceConfigurationAddFragment extends DeviceConfigurationFragment {

    private DeviceViewModel model;

    private ProgressBar progressBar;

    private ProgressDialog dialog;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog.isShowing()) {
                dialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.fragment_device_configuration_add_calibration_completed_toast), Toast.LENGTH_LONG).show();
            }
        }
    };


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

        model.identifySelectedDevice(agent -> {
            requireActivity().runOnUiThread(this::loadSupportedFeatures);

            connectDevice.setOnClickListener(v -> {
                model.addAgent(agent);

                saveFeaturesChosen();

                if (agent.getSource().getName().startsWith("UprightGO2")) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.fragment_device_configuration_sensor_calibration_text)
                            .setMessage(getString(R.string.fragment_device_configuration_warning_calibration_text) +
                                    getString(R.string.fragment_device_configuration_warning_calibration_text2))
                            .setPositiveButton(R.string.fragment_device_configuration_dialog_option_calibrate, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog = ProgressDialog.show(getContext(), "", getString(R.string.fragment_device_configuration_dialog_calibrating_message), false);

                                    UprightGo2Agent uprightGo2Agent = (UprightGo2Agent) agent;

                                    //Send Message calibration
                                    uprightGo2Agent.enableProtocol(new UprightGo2CalibrationProtocol(uprightGo2Agent));

                                    //default - first vibration settings when adding device
                                    boolean vibration = true;
                                    int angle = 1;
                                    int interval = 5;
                                    int pattern = 0;
                                    boolean showPattern = true;
                                    int strength = 1;

                                    //Send Message vibration settings
                                    uprightGo2Agent.enableProtocol(new UprightGo2VibrationProtocol(uprightGo2Agent, vibration, angle, interval, showPattern, pattern, strength));

                                    handler.sendMessageDelayed(new Message(), 2500);

                                }
                            })
                            .setIcon(R.drawable.img_citizen_hub_logo_png)
                            .show();
                }

                Navigation.findNavController(DeviceConfigurationAddFragment.this.requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceListFragment());
            });

            requireActivity().runOnUiThread(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                connectDevice.setText(R.string.fragment_device_configuration_connect_option_text);
            });


        });

        return view;
    }
}


package pt.uninova.s4h.citizenhub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2CalibrationProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2PostureProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2VibrationProtocol;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;


public class DeviceConfigurationAdvancedFragment extends DeviceConfigurationFragment {

    private ProgressDialog dialog;
    //TODO just for testing purposes, delete later
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (dialog.isShowing()) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Calibration Completed!", Toast.LENGTH_LONG).show();
            }
        }
    };
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_advanced, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        nameDevice = view.findViewById(R.id.textConfigurationDeviceName);
        addressDevice = view.findViewById(R.id.textConfigurationDeviceAddress);
        advancedOKDevice = view.findViewById(R.id.buttonConfigurationAdvancedOK);
        deviceAdvancedSettings = view.findViewById(R.id.layoutStubConfigurationAdvancedSettings);
        setupText();

        enableAdvancedConfigurations(view, model);

        advancedOKDevice.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(DeviceConfigurationAdvancedFragmentDirections.actionDeviceConfigurationAdvancedFragmentToDeviceConfigurationUpdateFragment());
        });

        return view;
    }

    //TODO change from device name to proper detection of the sensor with Advanced Settings
    protected void enableAdvancedConfigurations(View view, DeviceViewModel model) {
        final Device device = model.getSelectedDevice().getValue();
        if (device.getName().equals("UprightGO2")) { //miband just for testing, todo change this back to UprightGo2
            /*
            - Posture Correction Vibration ON/OFF
            - Vibration Angle (1 (strict) to 6 (relaxed))
            - Vibration Interval (5, 15, 30 or 60 seconds)
            - Vibration Show Pattern (ON/OFF)
            - Vibration Pattern (0 (long), 1 (medium), 2 (short), 3 (ramp up), 4 (knock knock),
            5 (heartbeat), 6 (tuk tuk), 7 (ecstatic), 8 (muzzle))
            - Vibration Strength (1 (gentle), 2 (medium), 3 (strong))
            - Perform Calibration (Trigger)
             */
            deviceAdvancedSettings.setLayoutResource(R.layout.fragment_device_configuration_advanced_uprightgo2);
            deviceAdvancedSettingsInflated = deviceAdvancedSettings.inflate();
            loadAdvancedConfigurationUprightGo2(deviceAdvancedSettingsInflated);
            setupAdvancedConfigurationsUprightGo2(deviceAdvancedSettingsInflated, model, device);
        }
    }

    protected void loadAdvancedConfigurationUprightGo2(View view) {
        //TODO Where to get them?
    }

    protected void setupAdvancedConfigurationsUprightGo2(View view, DeviceViewModel model, Device device) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ///TODO the listeners ARE NOT SET yet, missing where to save/get them
        //Posture Correction Vibration ON/OFF
        Switch postureCorrectionVibration = view.findViewById(R.id.switchPostureCorrection);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        postureCorrectionVibration.setChecked(sharedPreferences.getBoolean("Posture Correction Vibration",
                false));
        postureCorrectionVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("Posture Correction Vibration", true);
                    editor.apply();
                } else {
                    editor.putBoolean("Posture Correction Vibration", false);
                    editor.apply();
                }
            }
        });
        //Vibration Angle (1 (strict) to 6 (relaxed))
        SeekBar correctionAngle = view.findViewById(R.id.seekbarVibrationAngle);
        correctionAngle.setMax(5);
        correctionAngle.incrementProgressBy(1);
        correctionAngle.setProgress(0);
        correctionAngle.setProgress(sharedPreferences.
                getInt("Vibration Angle", -1));
        correctionAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                editor.putInt("Vibration Angle", correctionAngle.getProgress());
                //TODO add + 1 when sending config. message
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //Vibration Interval (5, 15, 30 or 60 seconds)
        Spinner spinnerInterval = view.findViewById(R.id.spinnerVibrationInterval);
        spinnerInterval.setSelection(sharedPreferences.getInt("Vibration Interval", -1));
        spinnerInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putInt("Vibration Interval", spinnerInterval.getSelectedItemPosition());
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do a default thingy
            }
        });
        //Vibration Pattern (0 (long), 1 (medium), 2 (short), 3 (ramp up), 4 (knock knock),
        // 5 (heartbeat), 6 (tuk tuk), 7 (ecstatic), 8 (muzzle))
        Spinner spinnerPattern = view.findViewById(R.id.spinnerVibrationPattern);
        spinnerPattern.setSelection(sharedPreferences.getInt("Vibration Pattern", -1));
        spinnerPattern.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putInt("Vibration Pattern", spinnerPattern.getSelectedItemPosition());
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do a default thingy
            }
        });
        //Vibration Show Pattern (ON/OFF)
        //TODO this can be implemented later, but not a setting available to
        // the user on the Upright App
        //Vibration Strength (1 (gentle), 2 (medium), 3 (strong))
        SeekBar correctionStrength = view.findViewById(R.id.seekbarVibrationStrength);
        correctionStrength.setMax(2);
        correctionStrength.incrementProgressBy(1);
        correctionStrength.setProgress(0);
        correctionStrength.setProgress(sharedPreferences.
                getInt("Vibration Strength", -1));
        correctionStrength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                editor.putInt("Vibration Strength", correctionStrength.getProgress());
                //TODO add + 1 when sending config. message
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        // Perform Calibration (Trigger)
        Button buttonCalibration = view.findViewById(R.id.buttonCalibration);

        CitizenHubService service = ((CitizenHubServiceBound) requireActivity()).getService();
        AgentOrchestrator agentOrchestrator = service.getAgentOrchestrator();

        buttonCalibration.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Sensor Calibration")
                        .setMessage("ATTENTION: Before pressing Calibrate, please ensure you are sitting and have" +
                                " your back straight.")
                        .setPositiveButton("Calibrate", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //testar disable do measurmente e protocolo
                                agentOrchestrator.getDeviceAgentMap().get(device).getProtocol(UprightGo2CalibrationProtocol.ID).enable();


                                //TODO calibrate and animate
                                dialog = ProgressDialog.show(getContext(), "", "Calibrating, Please wait...", false);

                                handler.sendMessageDelayed(new Message(), 2500);
                            }
                        })
                        .setIcon(R.drawable.img_citizen_hub_logo_png)
                        .show();
            }
        });
    }
}

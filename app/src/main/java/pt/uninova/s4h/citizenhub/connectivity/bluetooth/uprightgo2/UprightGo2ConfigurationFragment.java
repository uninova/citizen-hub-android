package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

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
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class UprightGo2ConfigurationFragment extends Fragment {

    protected ViewStub deviceAdvancedSettings;
    protected View deviceAdvancedSettingsInflated;

    private ProgressDialog dialog;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (dialog.isShowing()) {
                dialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.fragment_device_configuration_advanced_warning_calibration_completed_text), Toast.LENGTH_LONG).show();
            }
        }
    };
    private SharedPreferences sharedPreferences;
private boolean test = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_advanced, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        deviceAdvancedSettings = view.findViewById(R.id.layoutStubConfigurationAdvancedSettings);

        if(test==false) {
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle(R.string.fragment_device_configuration_sensor_calibration_text)
                    .setMessage(this.getString(R.string.fragment_device_configuration_warning_calibration_text) +
                            this.getString(R.string.fragment_device_configuration_warning_calibration_text2))
                    .setPositiveButton(R.string.fragment_device_configuration_dialog_option_calibrate, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog = ProgressDialog.show(getContext(), "", getString(R.string.fragment_device_configuration_dialog_calibrating_message), false);

                            UprightGo2Agent uprightGo2Agent = (UprightGo2Agent) model.getSelectedDeviceAgent();

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
            test=true;
        }
        enableAdvancedConfigurations(view, model);
        return view;
    }

    //TODO change from device name to proper detection of the sensor with Advanced Settings
    protected void enableAdvancedConfigurations(View view, DeviceViewModel model) {
        final Device device = model.getSelectedDevice().getValue();
        if (device.getName().equals("UprightGO2")) {
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
            setupAdvancedConfigurationsUprightGo2(deviceAdvancedSettingsInflated, model, device);
        }
    }


    protected void setupAdvancedConfigurationsUprightGo2(View view, DeviceViewModel model, Device device) {
        //Posture Correction Vibration ON/OFF
        SwitchCompat postureCorrectionVibration = view.findViewById(R.id.switchPostureCorrection);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        postureCorrectionVibration.setChecked(sharedPreferences.getBoolean("Posture Correction Vibration",
                true));
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

        Spinner spinnerAngle = view.findViewById(R.id.spinnerVibrationAngle);
        spinnerAngle.setSelection(sharedPreferences.getInt("Vibration Angle", -1));
        spinnerAngle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putInt("Vibration Angle", (spinnerAngle.getSelectedItemPosition()));
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do a default thingy
            }
        });
//        SeekBar correctionAngle = view.findViewById(R.id.seekbarVibrationAngle);
//        correctionAngle.setMax(5);
//        correctionAngle.incrementProgressBy(1);
//        correctionAngle.setProgress(0);
//        correctionAngle.setProgress(sharedPreferences.
//                getInt("Vibration Angle", -1));
//        correctionAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
//                editor.putInt("Vibration Angle", correctionAngle.getProgress());
//                editor.apply();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
        //Vibration Interval (5, 15, 30 or 60 seconds)
        Spinner spinnerInterval = view.findViewById(R.id.spinnerVibrationInterval);
        spinnerInterval.setSelection(sharedPreferences.getInt("Vibration Interval", -1));
        spinnerInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putInt("Vibration Interval", (spinnerInterval.getSelectedItemPosition()));
                System.out.println("Spinner item "+ spinnerInterval.getSelectedItem() + "interval:"  +spinnerInterval.getSelectedItemPosition() + "spinner i & L"
                        + i + " " + l);
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
                editor.putInt("Vibration Pattern", (spinnerPattern.getSelectedItemPosition()));
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do a default thingy
            }
        });
//        SeekBar correctionStrength = view.findViewById(R.id.spinnerVibrationStrength);
//        correctionStrength.setMax(2);
//        correctionStrength.incrementProgressBy(1);
//        correctionStrength.setProgress(0);
//        correctionStrength.setProgress(sharedPreferences.
//                getInt("Vibration Strength", -1));
//        correctionStrength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
//                editor.putInt("Vibration Strength", correctionStrength.getProgress());
//                editor.apply();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });

        Spinner correctionStrength = view.findViewById(R.id.spinnerVibrationStrength);
        correctionStrength.setSelection(sharedPreferences.getInt("Vibration Strength", -1));
        correctionStrength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putInt("Vibration Strength", (correctionStrength.getSelectedItemPosition()));
                editor.apply();
                System.out.println("Strength:" + correctionStrength.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Perform Calibration (Trigger)
        Button buttonCalibration = view.findViewById(R.id.buttonCalibration);


        buttonCalibration.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.fragment_device_configuration_advanced_calibration_dialog_title)
                        .setMessage(getString(R.string.fragment_device_configuration_advanced_warning_calibration_message_text) +
                                getString(R.string.fragment_device_configuration_advanced_warning_calibration_message_text2))
                        .setPositiveButton(R.string.fragment_device_configuration_advanced_calibration_dialog_calibrate_option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//
                                UprightGo2Agent agent = (UprightGo2Agent) model.getSelectedDeviceAgent();
//                                agent.enableProtocol(new UprightGo2CalibrationProtocol(agent));
//                                //Send Message vibration settings
//                                UprightGo2Agent uprightGo2Agent = (UprightGo2Agent) model.getSelectedDeviceAgent();

                                //Send Message calibration
                                agent.enableProtocol(new UprightGo2CalibrationProtocol(agent));
                                System.out.println("ONCLICKKK" + agent + agent.getName());

                                //default - first vibration settings when adding device
                                boolean vibration = true;
                                int angle = 1;
                                int interval = 5;
                                int pattern = 0;
                                boolean showPattern = true;
                                int strength = 1;
                                //TODO IR BUSCAR AOS LISTENERS
                                //Send Message vibration settings
                                agent.enableProtocol(new UprightGo2VibrationProtocol(agent, vibration, angle, interval, showPattern, pattern, strength));

                                dialog = ProgressDialog.show(getContext(), "", getString(R.string.fragment_device_configuration_advanced_calibration_dialog_calibrating_text), false);

                                handler.sendMessageDelayed(new Message(), 2500);
                            }
                        })
                        .setIcon(R.drawable.img_citizen_hub_logo_png)
                        .show();
            }
        });
    }

    public static Fragment newInstance()
    {
        return new UprightGo2ConfigurationFragment();
    }


}

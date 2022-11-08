package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.DeviceConfigurationUpdateFragmentDirections;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class UprightGo2ConfigurationFragment extends Fragment {

    public static String uprightGo2MenuItem = "calibration";

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

    private MenuItem.OnMenuItemClickListener menuItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_advanced, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        deviceAdvancedSettings = view.findViewById(R.id.layoutStubConfigurationAdvancedSettings);
        deviceAdvancedSettings.setLayoutResource(R.layout.fragment_device_configuration_advanced_uprightgo2);
        deviceAdvancedSettingsInflated = deviceAdvancedSettings.inflate();
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

            setupAdvancedConfigurationsUprightGo2(deviceAdvancedSettingsInflated, model, device);
        }
    }


    protected void setupAdvancedConfigurationsUprightGo2(View view, DeviceViewModel model, Device device) {
        //Posture Correction Vibration ON/OFF
        SwitchCompat postureCorrectionVibration = view.findViewById(R.id.switchPostureCorrection);
        if(!postureCorrectionVibration.isEnabled()){
                postureCorrectionVibration.setAlpha(0.5f);
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        postureCorrectionVibration.setChecked(sharedPreferences.getBoolean("Posture Correction Vibration", true));
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
                if(adapterView.getChildAt(0)!=null)
                ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(),R.color.colorS4HDarkBlue));
                editor.putInt("Vibration Angle", (spinnerAngle.getSelectedItemPosition()));
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do a default thingy
            }
        });
        Spinner spinnerInterval = view.findViewById(R.id.spinnerVibrationInterval);
        spinnerInterval.setSelection(sharedPreferences.getInt("Vibration Interval", -1));
        spinnerInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getChildAt(0)!=null)
                    ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(),R.color.colorS4HDarkBlue));
                editor.putInt("Vibration Interval", (spinnerInterval.getSelectedItemPosition()));
                System.out.println("Spinner item " + spinnerInterval.getSelectedItem() + "interval:" + spinnerInterval.getSelectedItemPosition() + "spinner i & L" + i + " " + l);
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
                if(adapterView.getChildAt(0)!=null)
                    ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(),R.color.colorS4HDarkBlue));
                editor.putInt("Vibration Pattern", (spinnerPattern.getSelectedItemPosition()));
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do a default thingy
            }
        });

        Spinner correctionStrength = view.findViewById(R.id.spinnerVibrationStrength);
        correctionStrength.setSelection(sharedPreferences.getInt("Vibration Strength", -1));
        correctionStrength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getChildAt(0)!=null)
                    ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(),R.color.colorS4HDarkBlue));
                editor.putInt("Vibration Strength", (correctionStrength.getSelectedItemPosition()));
                editor.apply();
                System.out.println("Strength:" + correctionStrength.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Perform Calibration (Trigger)
        LinearLayout buttonCalibration = view.findViewById(R.id.calibrationLayout);

        buttonCalibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToUprightGo2CalibrationFragment());
            }
        });


    }

    public static Fragment newInstance() {
        return new UprightGo2ConfigurationFragment();
    }

}

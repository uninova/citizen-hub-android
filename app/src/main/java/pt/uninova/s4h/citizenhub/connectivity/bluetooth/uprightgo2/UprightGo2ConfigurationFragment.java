package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import java.util.Objects;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AbstractConfigurationFragment;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class UprightGo2ConfigurationFragment extends AbstractConfigurationFragment {

    public static String uprightGo2MenuItem = "calibration";
    private Agent agent;
    protected ViewStub deviceAdvancedSettings;
    protected View deviceAdvancedSettingsInflated;
    private boolean vibration;
    private int angle;
    private int interval;
    private int pattern;
    private boolean showPattern;
    private int strength;

    public UprightGo2ConfigurationFragment(Agent agent) {
        super(agent);
        this.agent = agent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_advanced, container, false);

        deviceAdvancedSettings = view.findViewById(R.id.layoutStubConfigurationAdvancedSettings);
        deviceAdvancedSettings.setLayoutResource(R.layout.fragment_device_configuration_uprightgo2);
        deviceAdvancedSettingsInflated = deviceAdvancedSettings.inflate();

        LinearLayout buttonCalibration = deviceAdvancedSettingsInflated.findViewById(R.id.calibrationLayout);

        buttonCalibration.setOnClickListener(view1 -> Navigation.findNavController(requireView()).navigate(pt.uninova.s4h.citizenhub.ui.devices.DeviceConfigurationFragmentDirections.actionDeviceConfigurationStreamsFragmentToUprightGo2CalibrationFragment()));

        agent.getSettingsManager().get("First Time", new Observer<String>() {
            @Override
            public void observe(String value) {
                if (value == null) {
                    agent.getSettingsManager().set("posture-correction-vibration", "true");
                    agent.getSettingsManager().set("vibration-angle", "0");
                    agent.getSettingsManager().set("vibration-interval", "0");
                    agent.getSettingsManager().set("vibration-pattern", "0");
                    agent.getSettingsManager().set("show-vibration-pattern", "true");
                    agent.getSettingsManager().set("vibration-strength", "0");

                    agent.getSettingsManager().set("First Time", "1");
                    setupAdvancedConfigurationsUprightGo2(deviceAdvancedSettingsInflated);

                } else {
                    setupAdvancedConfigurationsUprightGo2(deviceAdvancedSettingsInflated);

                }
            }
        });

        return view;
    }


    protected void setupAdvancedConfigurationsUprightGo2(View view) {
        //posture-correction-vibration ON/OFF
        SwitchCompat postureCorrectionVibration = view.findViewById(R.id.switchPostureCorrection);
        if (agent.getState() != Agent.AGENT_STATE_ENABLED) {
            postureCorrectionVibration.setAlpha(0.5f);
        }
        agent.getSettingsManager().get("posture-correction-vibration", new Observer<String>() {
            @Override
            public void observe(String value) {
                if (Objects.equals(value, "true")) {
                    vibration = true;
                    postureCorrectionVibration.setChecked(true);
                } else {
                    if (Objects.equals(value, "false")) {
                        postureCorrectionVibration.setChecked(false);
                        vibration = false;
                    }
                }
            }
        });
        postureCorrectionVibration.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                agent.getSettingsManager().set("posture-correction-vibration", "true");
                vibration = true;
            } else {
                agent.getSettingsManager().set("posture-correction-vibration", "false");
                vibration = false;
            }
            setSetting(agent);
        });
        //vibration-angle (1 (strict) to 6 (relaxed))

        Spinner spinnerAngle = view.findViewById(R.id.spinnerVibrationAngle);

        agent.getSettingsManager().get("vibration-angle", new Observer<String>() {
            @Override
            public void observe(String value) {
                spinnerAngle.setSelection(Integer.parseInt(value));
                angle = Integer.parseInt(value);
            }
        });
        setSpinnerAlpha(spinnerAngle);
        spinnerAngle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getChildAt(0) != null) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorS4HDarkBlue));
                }
                agent.getSettingsManager().set("vibration-angle", String.valueOf(spinnerAngle.getSelectedItemPosition()));
                angle = spinnerAngle.getSelectedItemPosition();

                setSetting(agent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do a default thingy
            }
        });
        Spinner spinnerInterval = view.findViewById(R.id.spinnerVibrationInterval);

        agent.getSettingsManager().get("vibration-interval", new Observer<String>() {
            @Override
            public void observe(String value) {
                spinnerInterval.setSelection(Integer.parseInt(value));
                interval = Integer.parseInt(value);
            }
        });
        setSpinnerAlpha(spinnerInterval);
        spinnerInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getChildAt(0) != null)
                    ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorS4HDarkBlue));
                agent.getSettingsManager().set("vibration-interval", String.valueOf(spinnerInterval.getSelectedItemPosition()));
                interval = spinnerInterval.getSelectedItemPosition();
                setSetting(agent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do a default thingy
            }
        });
        //vibration-pattern (0 (long), 1 (medium), 2 (short), 3 (ramp up), 4 (knock knock),
        // 5 (heartbeat), 6 (tuk tuk), 7 (ecstatic), 8 (muzzle))
        Spinner spinnerPattern = view.findViewById(R.id.spinnerVibrationPattern);

        agent.getSettingsManager().get("vibration-pattern", new Observer<String>() {
            @Override
            public void observe(String value) {
                spinnerPattern.setSelection(Integer.parseInt(value));
                pattern = Integer.parseInt(value);
            }
        });
        setSpinnerAlpha(spinnerPattern);
        spinnerPattern.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getChildAt(0) != null)
                    ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorS4HDarkBlue));

                agent.getSettingsManager().set("vibration-pattern", String.valueOf(spinnerPattern.getSelectedItemPosition()));
                pattern = spinnerPattern.getSelectedItemPosition();
                setSetting(agent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do a default thingy
            }
        });

        Spinner correctionStrength = view.findViewById(R.id.spinnerVibrationStrength);

        agent.getSettingsManager().get("vibration-strength", new Observer<String>() {
            @Override
            public void observe(String value) {
                if (value == null) {
                    correctionStrength.setSelection(0);
                    strength = 0;
                } else {
                    correctionStrength.setSelection(Integer.parseInt(value));
                    strength = Integer.parseInt(value);
                }
            }
        });

        setSpinnerAlpha(correctionStrength);
        correctionStrength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getChildAt(0) != null)
                    ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorS4HDarkBlue));
                agent.getSettingsManager().set("vibration-strength", String.valueOf((correctionStrength.getSelectedItemPosition())));
                strength = (correctionStrength.getSelectedItemPosition());
                setSetting(agent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Perform Calibration (Trigger)


    }

    private void setSetting(Agent agent) {
        //some value adaptation
        int time = 5;
        if (interval == 0)
            time = 5;
        else if (interval == 1)
            time = 15;
        else if (interval == 2)
            time = 30;
        else if (interval == 3)
            time = 60;

        //Send Message vibration settings
        if (agent.getState() == Agent.AGENT_STATE_ENABLED) {

            UprightGo2VibrationProtocol vibrationProtocol;
            vibrationProtocol = new UprightGo2VibrationProtocol((UprightGo2Agent) agent, vibration, angle, interval, false, pattern, strength);
            vibrationProtocol.saveSettings();
        }
    }

    private void setSpinnerAlpha(Spinner spinner) {
        if (agent.getState() != Agent.AGENT_STATE_ENABLED) {
            spinner.setAlpha(0.5f);
        }
    }

}

package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class UprightGo2CalibrationFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_uprightgo2_calibration, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        Button calibrationButton = view.findViewById(R.id.buttonCalibrate);

        calibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UprightGo2Agent agent = (UprightGo2Agent) model.getSelectedDeviceAgent();
//                                //Send Message vibration settings

                //Send Message calibration
                agent.enableProtocol(new UprightGo2CalibrationProtocol(agent));

                //default - first vibration settings when adding device
                boolean vibration = sharedPreferences.getBoolean("Posture Correction Vibration", true);
                int angle = sharedPreferences.getInt("Vibration Angle", 1);
                int interval = sharedPreferences.getInt("Vibration Interval", 5);
                int pattern = sharedPreferences.getInt("Vibration Pattern", 0);
                boolean showPattern = sharedPreferences.getBoolean("Show Vibration Pattern", true);
                int strength = sharedPreferences.getInt("Vibration Strength", 0);

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
                agent.enableProtocol(new UprightGo2VibrationProtocol(agent, vibration, angle, interval, showPattern, pattern, strength));
            }
        });
        return view;
    }

}

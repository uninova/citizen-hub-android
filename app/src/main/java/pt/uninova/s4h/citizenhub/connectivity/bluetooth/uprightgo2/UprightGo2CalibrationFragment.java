package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import static pt.uninova.s4h.citizenhub.connectivity.Protocol.STATE_COMPLETED;

import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class UprightGo2CalibrationFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    UprightGo2Agent agent;
    ImageView checkMarkImageView;
    private final Observer<StateChangedMessage<Integer, ? extends Protocol>> protocolStateObserver = new Observer<StateChangedMessage<Integer, ? extends Protocol>>() {
        @Override
        public void observe(StateChangedMessage<Integer, ? extends Protocol> value) {
            if (value.getNewState() == STATE_COMPLETED) {
                checkMarkImageView.setVisibility(View.VISIBLE);
                ((Animatable) checkMarkImageView.getDrawable()).start();
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_uprightgo2_calibration, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Button calibrationButton = view.findViewById(R.id.buttonCalibrate);
        checkMarkImageView = view.findViewById(R.id.calibration_checkmark_imageView);

        calibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                                //Send Message vibration settings

                //Send Message calibration
                agent = (UprightGo2Agent) model.getSelectedDeviceAgent();
                UprightGo2CalibrationProtocol uprightGo2CalibrationProtocol = new UprightGo2CalibrationProtocol(agent);
                uprightGo2CalibrationProtocol.addStateObserver(protocolStateObserver);
                agent.enableProtocol(uprightGo2CalibrationProtocol);

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

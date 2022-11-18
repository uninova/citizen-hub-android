package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import static pt.uninova.s4h.citizenhub.connectivity.Protocol.STATE_COMPLETED;

import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class UprightGo2CalibrationFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    UprightGo2Agent agent;
    ImageView checkMarkImageView;
    LinearLayout animationsLayout;
    ProgressBar progressBar;
    TextView calibrationMessage;

    private final Observer<StateChangedMessage<Integer, ? extends Protocol>> protocolStateObserver = new Observer<StateChangedMessage<Integer, ? extends Protocol>>() {
        @Override
        public void observe(StateChangedMessage<Integer, ? extends Protocol> value) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (value.getNewState() == STATE_COMPLETED) {
                        progressBar.setVisibility(View.GONE);
                        calibrationMessage.setVisibility(View.GONE);
                        checkMarkImageView.setVisibility(View.VISIBLE);
                        ((Animatable) checkMarkImageView.getDrawable()).start();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Navigation.findNavController(requireView()).navigate(UprightGo2CalibrationFragmentDirections.actionUprightGo2CalibrationFragmentToDeviceConfigurationStreamsFragment());
                            }
                        }, 1000);
                    }

                }
            });
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_uprightgo2_calibration, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Button calibrationButton = view.findViewById(R.id.buttonCalibrate);
        calibrationMessage = view.findViewById(R.id.device_configuration_loading_textview);
        LinearLayout topLayout = view.findViewById(R.id.calibrationTopLayout);
        LinearLayout buttonLayout = view.findViewById(R.id.button_layout);
        animationsLayout = view.findViewById(R.id.animationsLayout);
        progressBar = view.findViewById(R.id.calibration_pprogressBar);
        checkMarkImageView = view.findViewById(R.id.calibration_checkmark_imageView);

        calibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                //Send Message calibration
                topLayout.setVisibility(View.GONE);
                buttonLayout.setVisibility(View.GONE);
                animationsLayout.setVisibility(View.VISIBLE);
                agent = (UprightGo2Agent) model.getSelectedDeviceAgent();
                UprightGo2CalibrationProtocol uprightGo2CalibrationProtocol = new UprightGo2CalibrationProtocol(agent);
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        uprightGo2CalibrationProtocol.addStateObserver(protocolStateObserver);
                        agent.enableProtocol(uprightGo2CalibrationProtocol);
                    }
                }, 1000);
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

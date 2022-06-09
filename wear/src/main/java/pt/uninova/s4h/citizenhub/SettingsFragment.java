package pt.uninova.s4h.citizenhub;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import persistence.MeasurementKind;

public class SettingsFragment extends Fragment {

    private Switch switchHeartRate, switchSteps;
    private TextView textPhoneConnected;
    private CompoundButton.OnCheckedChangeListener heartRateListener, stepsListener;
    private final String citizenHubPath = "/citizenhub_";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        switchHeartRate = view.findViewById(R.id.switchHeartRate);
        switchSteps = view.findViewById(R.id.switchSteps);
        textPhoneConnected = view.findViewById(R.id.textInfoPhone);

        enableObservers(view);

        heartRateListener = (compoundButton, isChecked) -> {
            if(isChecked)
                MainActivity.protocolHeartRate.setValue(true);
            else
                MainActivity.protocolHeartRate.setValue(false);
            Date now = new Date();
            MeasurementKind kind = MeasurementKind.HEART_RATE;
            String msg = checkedToCommunicationValue(isChecked) + "," + now.getTime() + "," + kind.getId();
            String dataPath = citizenHubPath + MainActivity.nodeIdString;
            new SendMessage(dataPath, msg).start();
        };

        stepsListener = (compoundButton, isChecked) -> {
            if(isChecked)
                MainActivity.protocolSteps.setValue(true);
            else
                MainActivity.protocolSteps.setValue(false);
            Date now = new Date();
            MeasurementKind kind = MeasurementKind.STEPS;
            String msg = checkedToCommunicationValue(isChecked) + "," + now.getTime() + "," + kind.getId();
            String dataPath = citizenHubPath + MainActivity.nodeIdString;
            new SendMessage(dataPath, msg).start();
        };

        enabledCheckedListeners(true);

        return view;
    }

    private void enableObservers(View view){
        MainActivity.protocolPhoneConnected.observe((LifecycleOwner) view.getContext(), aBoolean -> {
            if(aBoolean)
                textPhoneConnected.setText(R.string.show_data_phone_connected);
            else
                textPhoneConnected.setText(R.string.show_data_phone_not_connected);
        });

        MainActivity.protocolSteps.observe((LifecycleOwner) view.getContext(), aBoolean -> {
            enabledCheckedListeners(false);
            if(aBoolean) {
                textPhoneConnected.setText(R.string.show_data_phone_connected);
                switchSteps.setChecked(true);
                enableStepsSensor(true);
            }
            else {
                switchSteps.setChecked(false);
                enableStepsSensor(false);
            }
            enabledCheckedListeners(true);
        });

        MainActivity.protocolHeartRate.observe((LifecycleOwner) view.getContext(), aBoolean -> {
            enabledCheckedListeners(false);
            if(aBoolean) {
                textPhoneConnected.setText(R.string.show_data_phone_connected);
                switchHeartRate.setChecked(true);
                enableHeartRateSensor(true);
            }
            else {
                switchHeartRate.setChecked(false);
                enableHeartRateSensor(false);
            }
            enabledCheckedListeners(true);
        });
    }

    private void enableHeartRateSensor(Boolean enabled){
        if(MainActivity.sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null){
            if (enabled){
                MainActivity.sensorManager.registerListener(MainActivity.heartRateListener, MainActivity.heartSensor,SensorManager.SENSOR_DELAY_NORMAL);
            }
            else{
                MainActivity.sensorManager.unregisterListener(MainActivity.heartRateListener, MainActivity.heartSensor);
            }
        }
    }

    private void enableStepsSensor(Boolean enabled){
        if (MainActivity.sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null) {
            if(enabled){
                MainActivity.sensorManager.registerListener(MainActivity.stepsListener, MainActivity.stepsSensor,SensorManager.SENSOR_DELAY_NORMAL);
            }
            else{
                MainActivity.sensorManager.unregisterListener(MainActivity.stepsListener, MainActivity.stepsSensor);
            }
        }
    }

    private int checkedToCommunicationValue(boolean isChecked){
        if (isChecked)
            return 100001;
        else
            return 100000;
    }

    private void enabledCheckedListeners(Boolean enabled){
        if(enabled){
            switchHeartRate.setOnCheckedChangeListener(heartRateListener);
            switchSteps.setOnCheckedChangeListener(stepsListener);
        }
        else{
        switchHeartRate.setOnCheckedChangeListener(null);
        switchSteps.setOnCheckedChangeListener(null);
        }
    }

    class SendMessage extends Thread {
        String path;
        String message;
        SendMessage(String p, String msg) {
            path = p;
            message = msg;
        }
        public void run() {
            Task<List<Node>> nodeListTask = Wearable.getNodeClient(requireContext()).getConnectedNodes();
            try {
                List<Node> nodes = Tasks.await(nodeListTask);
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(requireActivity()).sendMessage(node.getId(), path, message.getBytes());
                    try {
                        Integer result = Tasks.await(sendMessageTask);
                    } catch (ExecutionException | InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            } catch (ExecutionException | InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}
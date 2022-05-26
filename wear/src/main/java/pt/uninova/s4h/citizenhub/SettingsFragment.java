package pt.uninova.s4h.citizenhub;

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
    private String citizenHubPath = "/citizenhub_";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        switchHeartRate = view.findViewById(R.id.switchHeartRate);
        switchSteps = view.findViewById(R.id.switchSteps);
        textPhoneConnected = view.findViewById(R.id.textInfo);

        MainActivity.protocolPhoneConnected.observe((LifecycleOwner) view.getContext(), aBoolean -> {
            if(aBoolean)
                textPhoneConnected.setText(R.string.show_data_phone_connected);
            else
                textPhoneConnected.setText(R.string.show_data_phone_not_connected);
        });

        MainActivity.protocolSteps.observe((LifecycleOwner) view.getContext(), aBoolean -> {
            removeListeners();
            if(aBoolean) {
                textPhoneConnected.setText(R.string.show_data_phone_connected);
                switchSteps.setChecked(true);
            }
            else {
                switchSteps.setChecked(false);
            }
            enableListeners();
        });

        MainActivity.protocolHeartRate.observe((LifecycleOwner) view.getContext(), aBoolean -> {
            removeListeners();
            if(aBoolean) {
                textPhoneConnected.setText(R.string.show_data_phone_connected);
                switchHeartRate.setChecked(true);
            }
            else {
                switchHeartRate.setChecked(false);
            }
            enableListeners();
        });

        heartRateListener = (compoundButton, isChecked) -> {
            Date now = new Date();
            MeasurementKind kind = MeasurementKind.HEART_RATE;
            String msg = checkedToCommunicationValue(isChecked) + "," + now.getTime() + "," + kind.getId();
            String dataPath = citizenHubPath + MainActivity.nodeIdString;
            new SendMessage(dataPath, msg).start();
        };

        stepsListener = (compoundButton, isChecked) -> {
            Date now = new Date();
            MeasurementKind kind = MeasurementKind.STEPS;
            String msg = checkedToCommunicationValue(isChecked) + "," + now.getTime() + "," + kind.getId();
            String dataPath = citizenHubPath + MainActivity.nodeIdString;
            new SendMessage(dataPath, msg).start();
        };

        enableListeners();

        return view;
    }

    private int checkedToCommunicationValue(boolean isChecked){
        if (isChecked)
            return 1001;
        else
            return 1000;
    }

    private void removeListeners(){
        switchHeartRate.setOnCheckedChangeListener(null);
        switchSteps.setOnCheckedChangeListener(null);
    }

    private void enableListeners(){
        switchHeartRate.setOnCheckedChangeListener(heartRateListener);
        switchSteps.setOnCheckedChangeListener(stepsListener);
    }

    class SendMessage extends Thread {
        String path;
        String message;
        SendMessage(String p, String msg) {
            path = p;
            message = msg;
        }
        public void run() {
            Task<List<Node>> nodeListTask = Wearable.getNodeClient(getContext()).getConnectedNodes();
            try {
                Task<Node> t = Wearable.getNodeClient(getContext()).getLocalNode();
                Node n = Tasks.await(t);
                List<Node> nodes = Tasks.await(nodeListTask);
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(getActivity()).sendMessage(node.getId(), path, message.getBytes());
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
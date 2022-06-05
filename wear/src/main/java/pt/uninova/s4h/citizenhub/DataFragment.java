package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

public class DataFragment extends Fragment {

    public TextView textDataSteps, textHeartRate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_data, container, false);

        textDataSteps = view.findViewById(R.id.textDataSteps);
        textHeartRate = view.findViewById(R.id.textDataHearRate);

        textDataSteps.setText(getString(R.string.show_data_steps, MainActivity.stepsTotal));
        textHeartRate.setText(getString(R.string.show_data_heartrate, MainActivity.heartRate));

        //TODO: not tested, still missing disabling sensor
        MainActivity.listenHeartRate.observe((LifecycleOwner) view.getContext(), s -> {
            if(Boolean.TRUE.equals(MainActivity.protocolHeartRate.getValue()))
                textHeartRate.setText(s);
            else
                textHeartRate.setText(R.string.fragment_data_protocol_disabled);
        });

        //TODO: not tested, still missing disabling sensor
        MainActivity.listenSteps.observe((LifecycleOwner) view.getContext(), s -> {
            if(Boolean.TRUE.equals(MainActivity.protocolSteps.getValue()))
                textDataSteps.setText(s);
            else
                textDataSteps.setText(R.string.fragment_data_protocol_disabled);
        });

        return view;
    }
}
package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

public class DataFragment extends Fragment {

    public TextView textDataSteps, textDataHeartRate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_data, container, false);

        textDataSteps = view.findViewById(R.id.textDataSteps);
        textDataHeartRate = view.findViewById(R.id.textDataHearRate);

        textDataSteps.setText(getString(R.string.show_data_steps, MainActivity.stepsTotal));
        textDataHeartRate.setText(getString(R.string.show_data_heartrate, MainActivity.heartRate));

        MainActivity.listenHeartRate.observe((LifecycleOwner) view.getContext(), s -> textDataHeartRate.setText(s));

        MainActivity.listenSteps.observe((LifecycleOwner) view.getContext(), s -> textDataSteps.setText(s));

        return view;
    }
}
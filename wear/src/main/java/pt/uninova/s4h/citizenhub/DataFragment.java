package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.BreakIterator;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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

        MainActivity.listenHeartRate.observe((LifecycleOwner) view.getContext(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textHeartRate.setText(s);
            }
        });

        MainActivity.listenSteps.observe((LifecycleOwner) view.getContext(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textDataSteps.setText(s);
            }
        });

        return view;
    }
}
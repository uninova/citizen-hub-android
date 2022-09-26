package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

public class DataFragment extends Fragment {

    public TextView textDataSteps, textDataHeartRate, textDataHeartRateAverage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_data, container, false);

        textDataSteps = view.findViewById(R.id.textDataSteps);
        textDataHeartRate = view.findViewById(R.id.textDataHearRate);
        textDataHeartRateAverage = view.findViewById(R.id.textDataHearRateAverage);

        enableObservers(view);

        return view;
    }

    private void enableObservers(View view){
        MainActivity.listenHeartRate.observe((LifecycleOwner) view.getContext(), s -> textDataHeartRate.setText(s));
        MainActivity.listenHeartRateAverage.observe((LifecycleOwner) view.getContext(), s -> textDataHeartRateAverage.setText(s));
        MainActivity.listenSteps.observe((LifecycleOwner) view.getContext(), s -> textDataSteps.setText(s));
    }
}
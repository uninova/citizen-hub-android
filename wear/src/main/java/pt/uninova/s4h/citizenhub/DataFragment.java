package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

public class DataFragment extends Fragment {

    public TextView textDataSteps, textDataHeartRate, textDataHeartRateAverage;
    private Handler handler = new Handler(Looper.getMainLooper());

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

        /* TODO: do not delete this after merging
        MainActivity.protocolSteps.observe((LifecycleOwner) view.getContext(), aBoolean -> {
            if(aBoolean)
            {
                final LocalDate now = LocalDate.now();
                MainActivity.stepsSnapshotMeasurementRepository.readMaximumObserved(now, value -> textDataSteps.setText(getString(R.string.show_data_steps, value.intValue())));
            }
            else
                textDataSteps.setText(R.string.fragment_data_steps_protocol_disabled);
            textDataSteps.setGravity(Gravity.CENTER);
        });

        MainActivity.protocolHeartRate.observe((LifecycleOwner) view.getContext(), aBoolean -> {
            if(aBoolean)
            {
                final LocalDate now = LocalDate.now();
                MainActivity.heartRateMeasurementRepository.readAverageObserved(now, value -> textDataHeartRateAverage.setText(getString(R.string.show_data_heartrate_average, value)));
            }
            else
                textDataHeartRate.setText(R.string.fragment_data_heartrate_protocol_disabled);
            textDataHeartRate.setGravity(Gravity.CENTER);
        });*/
    }
}
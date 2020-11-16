package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.util.Map;

public class SummaryFragment extends Fragment {

    private SummaryViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);

        model.getDailySummary().observe(getViewLifecycleOwner(), this::onDailySummaryUpdate);
    }

    private void onDailySummaryUpdate(Map<MeasurementKind, MeasurementAggregate> dailySummary) {
        final LinearLayout caloriesLayout = getView().findViewById(R.id.fragment_summary_layout_calories);
        final LinearLayout distanceLayout = getView().findViewById(R.id.fragment_summary_layout_distance);
        final LinearLayout heartRateLayout = getView().findViewById(R.id.fragment_summary_layout_heart_rate);
        final LinearLayout postureLayout = getView().findViewById(R.id.fragment_summary_layout_posture);
        final LinearLayout stepsLayout = getView().findViewById(R.id.fragment_summary_layout_steps);

        final TextView caloriesTextView = getView().findViewById(R.id.fragment_summary_text_view_calories);
        final TextView distanceTextView = getView().findViewById(R.id.fragment_summary_text_view_distance);
        final TextView heartRateTextView = getView().findViewById(R.id.fragment_summary_text_view_heart_rate);
        final TextView postureTextView = getView().findViewById(R.id.fragment_summary_text_view_posture);
        final TextView stepsTextView = getView().findViewById(R.id.fragment_summary_text_view_steps);

        if (dailySummary != null) {
            final MeasurementAggregate calories = dailySummary.get(MeasurementKind.CALORIES);
            final MeasurementAggregate distance = dailySummary.get(MeasurementKind.DISTANCE);
            final MeasurementAggregate heartRate = dailySummary.get(MeasurementKind.HEART_RATE);
            final MeasurementAggregate badPosture = dailySummary.get(MeasurementKind.BAD_POSTURE);
            final MeasurementAggregate goodPosture = dailySummary.get(MeasurementKind.GOOD_POSTURE);
            final MeasurementAggregate steps = dailySummary.get(MeasurementKind.STEPS);

            if (calories != null) {
                caloriesTextView.setText(getString(R.string.fragment_summary_text_view_calories_text, calories.getSum()));
                caloriesLayout.setVisibility(View.VISIBLE);
            } else {
                caloriesLayout.setVisibility(View.GONE);
            }

            if (distance != null) {
                distanceTextView.setText(getString(R.string.fragment_summary_text_view_distance_text, distance.getSum()));
                distanceLayout.setVisibility(View.VISIBLE);
            } else {
                distanceLayout.setVisibility(View.GONE);
            }

            if (heartRate != null) {
                heartRateTextView.setText(getString(R.string.fragment_summary_text_view_heart_rate_text, heartRate.getAverage()));
                heartRateLayout.setVisibility(View.VISIBLE);
            } else {
                heartRateLayout.setVisibility(View.GONE);
            }
            postureLayout.setVisibility(View.VISIBLE);

            if (badPosture != null || goodPosture != null) {
                postureTextView.setText(getString(R.string.fragment_summary_text_view_posture_text, badPosture == null ? 0 : badPosture.getSum(), goodPosture == null ? 0 : goodPosture.getSum()));
                postureLayout.setVisibility(View.VISIBLE);
            } else {
                postureLayout.setVisibility(View.GONE);
            }

            if (steps != null) {
                stepsTextView.setText(getString(R.string.fragment_summary_text_view_steps_text, steps.getSum()));
                stepsLayout.setVisibility(View.VISIBLE);
            } else {
                stepsLayout.setVisibility(View.GONE);
            }

            if (calories == null && goodPosture == null && distance == null && steps == null && calories == null && heartRate == null) {
                heartRateTextView.setText("No activity data for today.");
                heartRateLayout.setVisibility(View.VISIBLE);
            } else {
                heartRateLayout.setVisibility(View.GONE);
            }
        }
    }
}
package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import pt.uninova.s4h.citizenhub.persistence.DailySummary;

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

    private void onDailySummaryUpdate(DailySummary dailySummary) {
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
            if (dailySummary.getSumCalories() != null) {
                caloriesTextView.setText(getString(R.string.fragment_summary_text_view_calories_text, dailySummary.getSumCalories()));
                caloriesLayout.setVisibility(View.VISIBLE);
            } else {
                caloriesLayout.setVisibility(View.GONE);
            }

            if (dailySummary.getSumDistance() != null) {
                distanceTextView.setText(getString(R.string.fragment_summary_text_view_distance_text, dailySummary.getSumDistance()));
                distanceLayout.setVisibility(View.VISIBLE);
            } else {
                distanceLayout.setVisibility(View.GONE);
            }

            if (dailySummary.getAverageHeartRate() != null) {
                heartRateTextView.setText(getString(R.string.fragment_summary_text_view_heart_rate_text, dailySummary.getAverageHeartRate()));
                heartRateLayout.setVisibility(View.VISIBLE);
            } else {
                heartRateLayout.setVisibility(View.GONE);
            }

            if (dailySummary.getSumBadPosture() != null || dailySummary.getSumGoodPosture() != null) {
                postureTextView.setText(getString(R.string.fragment_summary_text_view_posture_text, dailySummary.getSumBadPosture(), dailySummary.getSumGoodPosture()));
                postureLayout.setVisibility(View.VISIBLE);
            } else {
                postureLayout.setVisibility(View.GONE);
            }

            if (dailySummary.getSumSteps() != null) {
                stepsTextView.setText(getString(R.string.fragment_summary_text_view_steps_text, dailySummary.getSumSteps()));
                stepsLayout.setVisibility(View.VISIBLE);
            } else {
                stepsLayout.setVisibility(View.GONE);
            }
        }

    }
}
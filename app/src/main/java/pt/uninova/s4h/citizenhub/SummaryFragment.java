package pt.uninova.s4h.citizenhub;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTraining;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class SummaryFragment extends ServiceFragment {

    private SummaryViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_summary_2, container, false);

        if (getService() != null) {
            onServiceConnected();
        }

        return view;
    }

    @Override
    public void onServiceConnected() {
        model.getDailySummary().observe(getViewLifecycleOwner(), this::onDailySummaryUpdate);
        //model.getMostRecentLumbar().observe(getViewLifecycleOwner(), this::onLumbarExtensionTrainingUpdate);
    }

    private void onLumbarExtensionTrainingUpdate(LumbarExtensionTraining lumbarExtensionTraining) {
        final LinearLayout lumbarGroup = requireView().findViewById(R.id.fragment_summary_layout_lumbar_training_extension);
        final TextView lumbarTextView = requireView().findViewById(R.id.fragment_summary_text_view_lumbar_text);
        final TextView lumbarTitle = requireView().findViewById(R.id.lumbarTextView);
        final TextView noDataTextView = requireView().findViewById(R.id.fragment_summary_text_view_no_data);

        if (lumbarExtensionTraining != null) {

            final int lumbarTrainingLength = lumbarExtensionTraining.getTrainingLength();
            final float lumbarScore = lumbarExtensionTraining.getScore();
            final int lumbarRepetitions = lumbarExtensionTraining.getRepetitions();
            noDataTextView.setVisibility(View.GONE);
            lumbarTextView.setText(getString(R.string.fragment_summary_text_view_lumbar_text, secondsToString(lumbarTrainingLength), lumbarScore, String.valueOf(lumbarRepetitions), lumbarExtensionTraining.getWeight()));

            lumbarGroup.setVisibility(View.VISIBLE);
            lumbarTitle.setVisibility(View.VISIBLE);
            lumbarTextView.setVisibility(View.VISIBLE);
        } else {
            lumbarGroup.setVisibility(View.GONE);
            lumbarTitle.setVisibility(View.GONE);
            lumbarTextView.setVisibility(View.GONE);
        }
    }

    private String secondsToString(int value) {
        int seconds = value;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        if (minutes > 0)
            seconds = seconds % 60;

        if (hours > 0) {
            minutes = minutes % 60;
        }

        String result = ((hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m " : "") + (seconds > 0 ? seconds + "s" : "")).trim();

        return result.equals("") ? "0s" : result;
    }

    private void onDailySummaryUpdate(Map<MeasurementKind, MeasurementAggregate> dailySummary) {
        final View view = requireView();

        if (dailySummary != null) {
            final MeasurementAggregate heartRate = dailySummary.get(MeasurementKind.HEART_RATE);
            final MeasurementAggregate postureCorrect = dailySummary.get(MeasurementKind.POSTURE_CORRECT);
            final MeasurementAggregate postureIncorrect = dailySummary.get(MeasurementKind.POSTURE_INCORRECT);
            final MeasurementAggregate steps = dailySummary.get(MeasurementKind.STEPS);
            final MeasurementAggregate distance = dailySummary.get(MeasurementKind.DISTANCE);
            final MeasurementAggregate calories = dailySummary.get(MeasurementKind.CALORIES);
            final MeasurementAggregate bloodPressureSystolic = dailySummary.get(MeasurementKind.BLOOD_PRESSURE_SYSTOLIC);
            final MeasurementAggregate bloodPressureDiastolic = dailySummary.get(MeasurementKind.BLOOD_PRESSURE_DIASTOLIC);
            //final MeasurementAggregate respiration = dailySummary.get(MeasurementKind.RESPIRATION_RATE);

            boolean hasHeartRate = heartRate != null;

            if (hasHeartRate) {
                TextView heartRateTextView = view.findViewById(R.id.heartRateValueTextView);

                heartRateTextView.setText(getString(R.string.heart_rate_value, heartRate.getAverage()));
            }

            CardView heartRateCardView = view.findViewById(R.id.heartRateCardView);
            heartRateCardView.setVisibility(hasHeartRate ? View.VISIBLE : View.GONE);


            boolean hasPosture = postureIncorrect != null || postureCorrect != null;

            if (hasPosture) {
                int pc = postureCorrect == null ? 0 : postureCorrect.getSum().intValue();
                int pi = postureIncorrect == null ? 0 : postureIncorrect.getSum().intValue();

                TextView postureCorrectTextView = view.findViewById(R.id.postureCorrectValueTextView);
                TextView postureIncorrectTextView = view.findViewById(R.id.postureIncorrectValueTextView);

                postureCorrectTextView.setText(getString(R.string.posture_correct_value, secondsToString(pc)));
                postureIncorrectTextView.setText(getString(R.string.posture_incorrect_value, secondsToString(pi)));
            }

            CardView postureCardView = view.findViewById(R.id.postureCardView);
            postureCardView.setVisibility(hasPosture ? View.VISIBLE : View.GONE);


            boolean hasBloodPressure = bloodPressureSystolic != null && bloodPressureDiastolic != null;

            if (hasBloodPressure) {
                TextView bloodPressureSystolicTextView = view.findViewById(R.id.bloodPressureSystolicValueTextView);
                TextView bloodPressureDiastolicTextView = view.findViewById(R.id.bloodPressureDiastolicValueTextView);

                bloodPressureSystolicTextView.setText(getString(R.string.blood_pressure_systolic_value, bloodPressureSystolic.getAverage()));
                bloodPressureDiastolicTextView.setText(getString(R.string.blood_pressure_diastolic_value, bloodPressureDiastolic.getAverage()));
            }

            CardView bloodPressureCardView = view.findViewById(R.id.bloodPressureCardView);
            bloodPressureCardView.setVisibility(hasBloodPressure ? View.VISIBLE : View.GONE);


            boolean hasActivity = steps != null || calories != null || distance != null;

            if (hasActivity) {
                double s = steps == null ? 0 : steps.getSum();
                double c = calories == null ? 0 : calories.getSum();
                double d = distance == null ? 0 : distance.getSum();

                TextView activityStepsTextView = view.findViewById(R.id.activityStepsValueTextView);
                TextView activityCaloriesTextView = view.findViewById(R.id.activityCaloriesValueTextView);
                TextView activityDistanceTextView = view.findViewById(R.id.activityDistanceValueTextView);

                activityStepsTextView.setText(getString(R.string.activity_steps_value, s));
                activityCaloriesTextView.setText(getString(R.string.activity_calories_value, c));
                activityDistanceTextView.setText(getString(R.string.activity_distance_value, d));
            }

            CardView activityCardView = view.findViewById(R.id.activityCardView);
            activityCardView.setVisibility(hasActivity ? View.VISIBLE : View.GONE);

            TextView noDataTextView = view.findViewById(R.id.fragment_summary_text_view_no_data);

            if (!hasActivity && !hasBloodPressure && !hasPosture && !hasHeartRate) {
                AgentOrchestrator agentOrchestrator = getService().getAgentOrchestrator();

                if (agentOrchestrator.getDevices().isEmpty())
                    noDataTextView.setText(getString(R.string.fragment_report_text_view_no_data_summary_nodevices));
                else
                    noDataTextView.setText(getString(R.string.fragment_report_text_view_no_data_summary));

                noDataTextView.setVisibility(View.VISIBLE);
            } else {
                noDataTextView.setVisibility(View.GONE);
            }
        }
    }
}

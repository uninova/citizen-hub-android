package pt.uninova.s4h.citizenhub.ui.summary;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.ServiceFragment;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.data.PostureValue;
import pt.uninova.s4h.citizenhub.persistence.entity.BloodPressureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.LumbarExtensionTrainingMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.LumbarExtensionTrainingSummary;
import pt.uninova.s4h.citizenhub.persistence.entity.util.PostureClassificationSum;
import pt.uninova.s4h.citizenhub.persistence.entity.util.WalkingInformation;

public class SummaryFragment extends ServiceFragment {

    private SummaryViewModel model;

    private String millisToString(long value) {
        return secondsToString(value / 1000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getService() != null) {
            onServiceConnected();
        }
    }

    private void onDailyBloodPressureMeasurementUpdate(List<BloodPressureMeasurementRecord> bloodPressureMeasurementRecords) {
        final View view = requireView();

        if (!bloodPressureMeasurementRecords.isEmpty()) {
            final BloodPressureMeasurementRecord record = bloodPressureMeasurementRecords.get(bloodPressureMeasurementRecords.size() - 1);

            TextView bloodPressureSystolicTextView = view.findViewById(R.id.bloodPressureSystolicValueTextView);
            TextView bloodPressureDiastolicTextView = view.findViewById(R.id.bloodPressureDiastolicValueTextView);

            bloodPressureSystolicTextView.setText(getString(R.string.blood_pressure_systolic_value, record.getSystolic()));
            bloodPressureDiastolicTextView.setText(getString(R.string.blood_pressure_diastolic_value, record.getDiastolic()));
        }

        final CardView bloodPressureCardView = view.findViewById(R.id.bloodPressureCardView);

        bloodPressureCardView.setVisibility(bloodPressureMeasurementRecords.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void onDailyDataExistenceUpdate(Boolean exists) {
        final View view = requireView();
        final TextView noDataTextView = view.findViewById(R.id.fragment_summary_text_view_no_data);

        if (exists) {
            noDataTextView.setVisibility(View.GONE);
        } else {
            AgentOrchestrator agentOrchestrator = getService().getAgentOrchestrator();

            if (agentOrchestrator.getDevices().isEmpty())
                noDataTextView.setText(getString(R.string.fragment_report_text_view_no_data_summary_nodevices));
            else
                noDataTextView.setText(getString(R.string.fragment_report_text_view_no_data_summary));

            noDataTextView.setVisibility(View.VISIBLE);
        }
    }

    private void onDailyHeartRateUpdate(Double value) {
        final View view = requireView();

        boolean hasHeartRate = value != null;

        if (hasHeartRate) {
            TextView heartRateTextView = view.findViewById(R.id.heartRateValueTextView);

            heartRateTextView.setText(getString(R.string.heart_rate_value, value));
        }

        final CardView heartRateCardView = view.findViewById(R.id.heartRateCardView);

        heartRateCardView.setVisibility(hasHeartRate ? View.VISIBLE : View.GONE);
    }

    private void onDailyLumbarExtensionTrainingUpdate(LumbarExtensionTrainingSummary record) {
        final View view = requireView();
        final CardView lumbarExtensionTrainingCardView = view.findViewById(R.id.lumbarTrainingCardView);

        if (record == null) {
            lumbarExtensionTrainingCardView.setVisibility(View.GONE);
        } else {
            final TextView lumbarExtensionTrainingCaloriesTextView = view.findViewById(R.id.lumbarTrainingCaloriesValueTextView);
            final TextView lumbarExtensionTrainingDurationTextView = view.findViewById(R.id.lumbarTrainingDurationValueTextView);
            final TextView lumbarExtensionTrainingRepetitionsTextView = view.findViewById(R.id.lumbarTrainingRepetitionsValueTextView);
            final TextView lumbarExtensionTrainingWeightTextView = view.findViewById(R.id.lumbarTrainingWeightValueTextView);

            if (record.getCalories() == null) {
                lumbarExtensionTrainingCaloriesTextView.setVisibility(View.GONE);
            } else {
                lumbarExtensionTrainingCaloriesTextView.setText(getString(R.string.lumbar_training_calories_value, record.getCalories()));
                lumbarExtensionTrainingCaloriesTextView.setVisibility(View.VISIBLE);
            }

            lumbarExtensionTrainingDurationTextView.setText(getString(R.string.lumbar_training_duration_value, millisToString(record.getDuration().toMillis())));
            lumbarExtensionTrainingRepetitionsTextView.setText(getString(R.string.lumbar_training_repetitions_value, record.getRepetitions()));
            lumbarExtensionTrainingWeightTextView.setText(getString(R.string.lumbar_training_weight_value, record.getWeight()));

            lumbarExtensionTrainingCardView.setVisibility(View.VISIBLE);
        }
    }

    private void onDailyPostureMeasurementUpdate(List<PostureClassificationSum> records) {
        final View view = requireView();
        final Map<Integer, Double> values = new HashMap<>();

        for (PostureClassificationSum i : records) {
            if (i.getClassification() == PostureValue.CLASSIFICATION_CORRECT || i.getClassification() == PostureValue.CLASSIFICATION_INCORRECT) {
                values.put(i.getClassification(), i.getDuration());
            }
        }

        boolean hasPosture = !values.isEmpty();

        if (hasPosture) {
            int pc = values.containsKey(PostureValue.CLASSIFICATION_CORRECT) ? values.get(PostureValue.CLASSIFICATION_CORRECT).intValue() : 0;
            int pi = values.containsKey(PostureValue.CLASSIFICATION_INCORRECT) ? values.get(PostureValue.CLASSIFICATION_INCORRECT).intValue() : 0;

            TextView postureCorrectTextView = view.findViewById(R.id.postureCorrectValueTextView);
            TextView postureIncorrectTextView = view.findViewById(R.id.postureIncorrectValueTextView);

            postureCorrectTextView.setText(getString(R.string.posture_correct_value, secondsToString(pc)));
            postureIncorrectTextView.setText(getString(R.string.posture_incorrect_value, secondsToString(pi)));
        }

        final CardView postureCardView = view.findViewById(R.id.postureCardView);
        postureCardView.setVisibility(hasPosture ? View.VISIBLE : View.GONE);
    }

    public void onDailyWalkingInformationUpdate(WalkingInformation record) {
        if (record != null) {
            final View view = requireView();

            final boolean hasSteps = record.getSteps() != null;
            final boolean hasDistance = record.getDistance() != null;
            final boolean hasCalories = record.getCalories() != null;

            final boolean hasActivity = hasSteps || hasDistance || hasCalories;

            final TextView activityStepsTextView = view.findViewById(R.id.activityStepsValueTextView);
            final TextView activityCaloriesTextView = view.findViewById(R.id.activityCaloriesValueTextView);
            final TextView activityDistanceTextView = view.findViewById(R.id.activityDistanceValueTextView);

            if (hasSteps) {
                activityStepsTextView.setText(getString(R.string.activity_steps_value, record.getSteps()));
            }

            if (hasDistance) {
                activityCaloriesTextView.setText(getString(R.string.activity_calories_value, record.getCalories()));
            }

            if (hasCalories) {
                activityDistanceTextView.setText(getString(R.string.activity_distance_value, record.getDistance()));
            }

            final CardView activityCardView = view.findViewById(R.id.activityCardView);

            activityStepsTextView.setVisibility(hasSteps ? View.VISIBLE : View.GONE);
            activityDistanceTextView.setVisibility(hasDistance ? View.VISIBLE : View.GONE);
            activityCaloriesTextView.setVisibility(hasCalories ? View.VISIBLE : View.GONE);
            activityCardView.setVisibility(hasActivity ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void onServiceConnected() {
        model.getDailyLumbarExtensionTraining().observe(getViewLifecycleOwner(), this::onDailyLumbarExtensionTrainingUpdate);
        model.getDailyBloodPressureMeasurement().observe(getViewLifecycleOwner(), this::onDailyBloodPressureMeasurementUpdate);
        model.getDailyDataExistence().observe(getViewLifecycleOwner(), this::onDailyDataExistenceUpdate);
        model.getDailyHeartRate().observe(getViewLifecycleOwner(), this::onDailyHeartRateUpdate);
        model.getDailyPostureMeasurement().observe(getViewLifecycleOwner(), this::onDailyPostureMeasurementUpdate);
        model.getDailyWalkingInformation().observe(getViewLifecycleOwner(), this::onDailyWalkingInformationUpdate);
    }

    private String secondsToString(long value) {
        long seconds = value;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        if (minutes > 0)
            seconds = seconds % 60;

        if (hours > 0) {
            minutes = minutes % 60;
        }

        String result = ((hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m " : "") + (seconds > 0 ? seconds + "s" : "")).trim();

        return result.equals("") ? "0s" : result;
    }
}
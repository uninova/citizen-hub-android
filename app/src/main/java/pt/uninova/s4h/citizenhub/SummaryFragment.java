package pt.uninova.s4h.citizenhub;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTraining;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class SummaryFragment extends Fragment {

    private SummaryViewModel model;
    private boolean lumbar = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);

        model.getDailySummary().observe(getViewLifecycleOwner(), this::onDailySummaryUpdate);
        model.getLumbarExtensionTraining().observe(getViewLifecycleOwner(), this::onLumbarExtensionTrainingUpdate);
        model.getMostRecentLumbar().observe(getViewLifecycleOwner(), this::onLumbarExtensionTrainingUpdate);
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
            noDataTextView.setVisibility(GONE);
            lumbarTextView.setText(getString(R.string.fragment_summary_text_view_lumbar_text, secondsToString(lumbarTrainingLength), lumbarScore, String.valueOf(lumbarRepetitions), lumbarExtensionTraining.getWeight()));

            lumbarGroup.setVisibility(View.VISIBLE);
            lumbarTitle.setVisibility(View.VISIBLE);
            lumbarTextView.setVisibility(View.VISIBLE);
            lumbar = true;
        } else {
            lumbar = false;

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

    public String calculateTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) -
                TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) -
                TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds));
        long second = TimeUnit.SECONDS.toSeconds(seconds) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds));
        return (String.format("%d Hours %d Minutes %d Seconds", hours, minute, second));

    }


    private void onDailySummaryUpdate(Map<MeasurementKind, MeasurementAggregate> dailySummary) {
        final LinearLayout caloriesGroup = requireView().findViewById(R.id.fragment_summary_layout_calories);
        final LinearLayout distanceGroup = requireView().findViewById(R.id.fragment_summary_layout_distance);
        final LinearLayout heartrateGroup = requireView().findViewById(R.id.fragment_summary_layout_heart_rate);
        final LinearLayout postureGroup = requireView().findViewById(R.id.fragment_summary_layout_posture);
        final LinearLayout stepsGroup = requireView().findViewById(R.id.fragment_summary_layout_activity);
        final LinearLayout respirationGroup = requireView().findViewById(R.id.fragment_summary_layout_respiration);
        final LinearLayout bloodPressureGroup = requireView().findViewById(R.id.fragment_summary_layout_blood_pressure);

        final TextView caloriesTextView = requireView().findViewById(R.id.fragment_summary_text_view_calories);
        final TextView distanceTextView = requireView().findViewById(R.id.fragment_summary_text_view_distance);
        final TextView heartRateTextView = requireView().findViewById(R.id.fragment_summary_text_view_heart_rate);
        final TextView postureTextView = requireView().findViewById(R.id.fragment_summary_text_view_posture);
        final TextView stepsTextView = requireView().findViewById(R.id.fragment_summary_text_view_steps);
        final TextView respirationTextView = requireView().findViewById(R.id.fragment_summary_text_view_respiration);
        final TextView bloodPressureTextView = requireView().findViewById(R.id.fragment_summary_text_view_blood_pressure);


        final TextView caloriesTitle = requireView().findViewById(R.id.caloriesTextView);
        final TextView distanceTitle = requireView().findViewById(R.id.distanceWalkedTextView);
        final TextView heartRateTitle = requireView().findViewById(R.id.heartrateTextView);
        final TextView postureTitle = requireView().findViewById(R.id.sittingTextView);
        final TextView stepsTitle = requireView().findViewById(R.id.stepsTakenTextView);
        final TextView respirationTitle = requireView().findViewById(R.id.respirationTextView);
        final TextView bloodPressureTitle = requireView().findViewById(R.id.bloodPressureTextView);
        final TextView noDataTextView = requireView().findViewById(R.id.fragment_summary_text_view_no_data);


        if (dailySummary != null) {

            final MeasurementAggregate calories = dailySummary.get(MeasurementKind.CALORIES);
            final MeasurementAggregate distance = dailySummary.get(MeasurementKind.DISTANCE);
            final MeasurementAggregate heartRate = dailySummary.get(MeasurementKind.HEART_RATE);
            final MeasurementAggregate badPosture = dailySummary.get(MeasurementKind.BAD_POSTURE);
            final MeasurementAggregate goodPosture = dailySummary.get(MeasurementKind.GOOD_POSTURE);
            final MeasurementAggregate steps = dailySummary.get(MeasurementKind.STEPS);
            final MeasurementAggregate respiration = dailySummary.get(MeasurementKind.RESPIRATION_RATE);
            final MeasurementAggregate bloodPressureSBP = dailySummary.get(MeasurementKind.BLOOD_PRESSURE_SBP);
            final MeasurementAggregate bloodPressureDBP = dailySummary.get(MeasurementKind.BLOOD_PRESSURE_DBP);
            final MeasurementAggregate bloodPressureMeanAP = dailySummary.get(MeasurementKind.BLOOD_PRESSURE_MEAN_AP);

            if (bloodPressureSBP != null && bloodPressureDBP != null && bloodPressureMeanAP != null) {
                bloodPressureTextView.setText(getString(R.string.fragment_summary_text_view_blood_pressure_text, bloodPressureSBP.getAverage(), bloodPressureDBP.getAverage()));
                bloodPressureGroup.setVisibility(VISIBLE);
                bloodPressureTitle.setVisibility(VISIBLE);
                bloodPressureTextView.setVisibility(VISIBLE);
            } else {
                bloodPressureGroup.setVisibility(View.GONE);
                bloodPressureTitle.setVisibility(View.GONE);
                bloodPressureTextView.setVisibility(View.GONE);
            }

            if (respiration != null) {
                respirationTextView.setText(getString(R.string.fragment_summary_text_view_respiration_text, respiration.getSum()));
                respirationGroup.setVisibility(VISIBLE);
                respirationTitle.setVisibility(VISIBLE);
                respirationTextView.setVisibility(VISIBLE);
            } else {
                respirationGroup.setVisibility(View.GONE);
                respirationTitle.setVisibility(View.GONE);
                respirationTextView.setVisibility(View.GONE);
            }

            if (calories != null) {
                caloriesTextView.setText(getString(R.string.fragment_summary_text_view_calories_text, calories.getSum()));
                caloriesGroup.setVisibility(VISIBLE);
                caloriesTitle.setVisibility(VISIBLE);
                caloriesTextView.setVisibility(VISIBLE);
            } else {
                caloriesGroup.setVisibility(View.GONE);
                caloriesTitle.setVisibility(View.GONE);
                caloriesTextView.setVisibility(View.GONE);
            }

            if (distance != null) {
                distanceTextView.setText(getString(R.string.fragment_summary_text_view_distance_text, distance.getSum()));
                distanceGroup.setVisibility(VISIBLE);
                distanceTitle.setVisibility(VISIBLE);
                distanceTextView.setVisibility(VISIBLE);

            } else {
                distanceGroup.setVisibility(View.GONE);
                distanceTitle.setVisibility(View.GONE);
                distanceTextView.setVisibility(View.GONE);

            }

            if (heartRate != null) {
                heartRateTextView.setText(getString(R.string.fragment_summary_text_view_heart_rate_text, heartRate.getAverage()));
                heartrateGroup.setVisibility(VISIBLE);
                heartRateTitle.setVisibility(VISIBLE);
                heartRateTextView.setVisibility(VISIBLE);

            } else {
                heartrateGroup.setVisibility(View.GONE);
                heartRateTitle.setVisibility(View.GONE);
                heartRateTextView.setVisibility(View.GONE);

            }


            if (badPosture != null || goodPosture != null) {
                int gp = goodPosture == null ? 0 : goodPosture.getSum().intValue();
                int bp = badPosture == null ? 0 : badPosture.getSum().intValue();

                postureTextView.setText(getString(R.string.fragment_summary_text_view_posture_text, secondsToString(gp), secondsToString(bp)));
                postureGroup.setVisibility(VISIBLE);
                postureTitle.setVisibility(VISIBLE);
                postureTextView.setVisibility(VISIBLE);

            } else {
                postureGroup.setVisibility(View.GONE);
                postureTitle.setVisibility(View.GONE);
                postureTextView.setVisibility(View.GONE);

            }

            if (steps != null) {
                stepsTextView.setText(getString(R.string.fragment_summary_text_view_activity_text, steps.getSum()));
                stepsGroup.setVisibility(VISIBLE);
                stepsTitle.setVisibility(VISIBLE);
                stepsTextView.setVisibility(VISIBLE);

            } else {
                stepsGroup.setVisibility(View.GONE);
                stepsTitle.setVisibility(View.GONE);
                stepsTextView.setVisibility(View.GONE);
            }

            if (badPosture == null && goodPosture == null && distance == null && steps == null && calories == null && heartRate == null && !lumbar && respiration == null && bloodPressureSBP == null && bloodPressureDBP == null && bloodPressureMeanAP == null) {

                CitizenHubService service = ((CitizenHubServiceBound) requireActivity()).getService();
                AgentOrchestrator agentOrchestrator = service.getAgentOrchestrator();
                if (agentOrchestrator.getDevices().isEmpty())
                    noDataTextView.setText(getString(R.string.fragment_report_text_view_no_data_summary_nodevices));
                else
                    noDataTextView.setText(getString(R.string.fragment_report_text_view_no_data_summary));

                noDataTextView.setVisibility(VISIBLE); //TODO make own card
            } else {
                noDataTextView.setVisibility(View.GONE); //TODO all other gones
            }
        }
    }
}
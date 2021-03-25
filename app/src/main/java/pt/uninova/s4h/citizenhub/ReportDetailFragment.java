package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import care.data4life.fhir.r4.model.DocumentReference;
import care.data4life.sdk.call.Callback;
import care.data4life.sdk.call.Fhir4Record;
import care.data4life.sdk.lang.D4LException;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class ReportDetailFragment extends Fragment {

    private ReportViewModel model;
    private TextView infoTextView_year, infoTextView_day, getInfoTextView_noData;
    private TextView heartRateAvg, heartRateMax, heartRateMin, distanceTotal, caloriesTotal, stepsTotal;
    private TextView heartRateTitle, caloriesTitle, distanceTitle, stepsTitle;
    private Group heartRateGroup, caloriesGroup, distanceGroup, stepsGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_detail, container, false);

        Button uploadPdfButton = view.findViewById(R.id.button);
        uploadPdfButton.setOnClickListener(v -> {
            try {
                model.sendDetail(new Callback<Fhir4Record<DocumentReference>>() {
                    @Override
                    public void onSuccess(Fhir4Record<DocumentReference> recAord) {
                        requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "File uploaded successfully.", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onError(D4LException exception) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "File failed to upload.", Toast.LENGTH_SHORT).show();
                            exception.printStackTrace();
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return view;
    }

    private void onSummaryChanged(Map<MeasurementKind, MeasurementAggregate> value) {
        final MeasurementAggregate calories = value.get(MeasurementKind.CALORIES);
        final MeasurementAggregate distance = value.get(MeasurementKind.DISTANCE);
        final MeasurementAggregate heartRate = value.get(MeasurementKind.HEART_RATE);
        final MeasurementAggregate badPosture = value.get(MeasurementKind.BAD_POSTURE);
        final MeasurementAggregate goodPosture = value.get(MeasurementKind.GOOD_POSTURE);
        final MeasurementAggregate steps = value.get(MeasurementKind.STEPS);

        requireActivity().runOnUiThread(() -> {
            final int titleResId = (calories == null || distance == null || heartRate == null || badPosture == null || goodPosture == null || steps == null
                    ? R.string.fragment_report_text_view_title_no_data
                    : R.string.fragment_report_text_view_title);


            String day = String.valueOf(model.getDetailDate().getDayOfMonth());
            String month = model.getDetailDate().getMonth().toString();
            String year = String.valueOf(model.getDetailDate().getYear());
            String dayMonth = day + " " + month;
            infoTextView_day.setText(dayMonth);
            infoTextView_year.setText(year);
//
//            if (badPosture != null && goodPosture != null) {
//                infoTextView_timeSitting.setText(getString(R.string.fragment_report_text_view_time_sitting_text, badPosture.getSum()));
//                infoTextView_timePosture.setText(getString(R.string.fragment_report_text_view_time_posture_text, goodPosture.getSum()));
//            } else {
//                infoTextView_timeSitting.setVisibility(View.GONE);
//                infoTextView_timePosture.setVisibility(View.GONE);
//            }

            if (distance == null && steps == null && heartRate == null && calories == null) {
                getInfoTextView_noData.setVisibility(View.VISIBLE);
            } else {
                getInfoTextView_noData.setVisibility(View.GONE);
            }

            if (distance != null) {
//                infoTextView_distance.setText(getString(R.string.fragment_report_text_view_distance_text, distance.getSum()));
//                distanceTitle.setVisibility(View.VISIBLE);
//            distanceTotal.setVisibility(View.VISIBLE);
                if (distanceGroup != null) {
                    distanceGroup.setVisibility(View.VISIBLE);
                }
                distanceTotal.setText(String.valueOf(distance.getSum()));
            } else {
//                distanceTitle.setVisibility(View.GONE);
//                distanceTotal.setVisibility(View.GONE);
                if (distanceGroup != null) {
                    distanceGroup.setVisibility(View.GONE);
                }
            }
            if (steps != null) {
                if (stepsGroup != null) {
                    stepsGroup.setVisibility(View.VISIBLE);
                }
                stepsTotal.setText(String.valueOf(steps.getSum()));
//                stepsTitle.setVisibility(View.VISIBLE);
//                stepsTotal.setVisibility(View.VISIBLE);
            }
//                infoTextView_steps.setText(getString(R.string.fragment_report_text_view_steps_text, steps.getSum()));
            else {
//                infoTextView_steps.setVisibility(View.GONE);
                if (stepsGroup != null) {

                    stepsGroup.setVisibility(View.GONE);

                }
//                stepsTitle.setVisibility(View.GONE);
//                stepsTotal.setVisibility(View.GONE);
            }
            if (calories != null) {
                if (caloriesGroup != null) {
                    caloriesGroup.setVisibility(View.VISIBLE);
                }
                caloriesTotal.setText(String.valueOf(calories.getSum()));
//                caloriesTitle.setVisibility(View.VISIBLE);
//                caloriesTitle.setVisibility(View.VISIBLE);
            }
//                infoTextView_calories.setText(getString(R.string.fragment_report_text_view_calories_text, calories.getSum()));
            else {
                if (caloriesGroup != null) {

                    caloriesGroup.setVisibility(View.GONE);
                }

//                caloriesTitle.setVisibility(View.GONE);
//                caloriesTotal.setVisibility(View.GONE);
            }
//                infoTextView_calories.setVisibility(View.GONE);

            if (heartRate != null) {
                if (heartRateGroup != null) {
                    heartRateGroup.setVisibility(View.VISIBLE);
                }
                heartRateAvg.setText(String.valueOf(heartRate.getAverage()));
                heartRateMax.setText(String.valueOf(heartRate.getMax()));
                heartRateMin.setText(String.valueOf(heartRate.getMin()));
//                heartRateTitle.setVisibility(View.VISIBLE);
//                heartRateMax.setVisibility(View.VISIBLE);
//                heartRateMin.setVisibility(View.VISIBLE);
//                heartRateAvg.setVisibility(View.VISIBLE);
            }
//                infoTextView_heartrate.setText(getString(R.string.fragment_report_text_view_heartrate_text, heartRate.getAverage()));
            else {
                if (heartRateGroup != null) {
                    heartRateGroup.setVisibility(View.GONE);
                }
//                heartRateTitle.setVisibility(View.GONE);
//                heartRateMax.setVisibility(View.GONE);
//                heartRateMin.setVisibility(View.GONE);
//                heartRateAvg.setVisibility(View.GONE);
            }
//                infoTextView_heartrate.setVisibility(View.GONE);

            /*
            infoTextView_timeSitting.setText(getString(R.string.fragment_report_text_view_time_sitting_text, 100.0));
            infoTextView_timePosture.setText(getString(R.string.fragment_report_text_view_time_posture_text, 200.0));
            infoTextView_distance.setText(getString(R.string.fragment_report_text_view_distance_text, 200.0));
            infoTextView_steps.setText(getString(R.string.fragment_report_text_view_steps_text, 40.0));
            infoTextView_calories.setText(getString(R.string.fragment_report_text_view_calories_text, 100.0));
            infoTextView_heartrate.setText(getString(R.string.fragment_report_text_view_heartrate_text, 200.0));
            */
        });
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);

        infoTextView_day = view.findViewById(R.id.fragment_report_detail_text_view_day);
        infoTextView_year = view.findViewById(R.id.fragment_report_detail_text_view_year);
//        infoTextView_timePosture = view.findViewById(R.id.fragment_report_detail_text_view_time_posture);
//        infoTextView_timeSitting = view.findViewById(R.id.fragment_report_detail_text_view_time_sitting);
//        infoTextView_distance = view.findViewById(R.id.fragment_report_detail_text_view_distance);
//        infoTextView_steps = view.findViewById(R.id.fragment_report_detail_text_view_steps);
//        infoTextView_calories = view.findViewById(R.id.fragment_report_detail_text_view_calories);
//        infoTextView_heartrate = view.findViewById(R.id.fragment_report_detail_text_view_heartrate);
        heartRateTitle = view.findViewById(R.id.HeartRateTitle);
        caloriesTitle = view.findViewById(R.id.caloriesTitle);
        stepsTitle = view.findViewById(R.id.StepsTitle);
        distanceTitle = view.findViewById(R.id.DistanceTitle);
        heartRateAvg = view.findViewById(R.id.fragment_report_detail_heart_rate_average);
        heartRateMax = view.findViewById(R.id.fragment_report_detail_heart_rate_max);
        heartRateMin = view.findViewById(R.id.fragment_report_detail_heart_rate_min);
        distanceTotal = view.findViewById(R.id.fragment_report_detail_distance_total);
        caloriesTotal = view.findViewById(R.id.fragment_report_detail_calories_total);
        stepsTotal = view.findViewById(R.id.fragment_report_detail_steps_total);
        heartRateGroup = view.findViewById(R.id.hearRateGroup);
        caloriesGroup = view.findViewById(R.id.caloriesGroup);
        stepsGroup = view.findViewById(R.id.stepsGroup);
        distanceGroup = view.findViewById(R.id.distanceGroup);
        getInfoTextView_noData = view.findViewById(R.id.fragment_report_detail_view_no_data);
        model.obtainSummary(this::onSummaryChanged);
        model = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
    }
}
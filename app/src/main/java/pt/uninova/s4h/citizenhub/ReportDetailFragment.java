package pt.uninova.s4h.citizenhub;


import android.content.Intent;
import android.net.Uri;
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
import java.time.LocalDate;
import java.util.Map;

import care.data4life.fhir.r4.model.DocumentReference;
import care.data4life.sdk.call.Callback;
import care.data4life.sdk.call.Fhir4Record;
import care.data4life.sdk.lang.D4LException;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTraining;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class ReportDetailFragment extends Fragment {

    private ReportViewModel model;
    private TextView infoTextView_year, infoTextView_day, getInfoTextView_noData;
    private TextView heartRateAvg, heartRateMax, heartRateMin, distanceTotal, caloriesTotal, stepsTotal, okPostureTotal, notOkPostureTotal,
            lumbarRepetitions, lumbarTrainingLength, lumbarScore, lumbarWeight, respirationRate, bloodPressureSBP, bloodPressureDBP, bloodPressureMeanAP;
    private LumbarExtensionTraining lumbarExtensionTraining;
    private Group heartRateGroup, caloriesGroup, distanceGroup, stepsGroup, postureGroup, lumbarExtensionTrainingGroup, respirationGroup, bloodPressureGroup;
    private boolean isSucess;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_detail, container, false);
        Button uploadPdfButton = view.findViewById(R.id.uploadButton);
        Button viewPdfButton = view.findViewById(R.id.viewPdfButton);


        viewPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://app.smart4health.eu";
                //String url = "https://app-staging.smart4health.eu";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
//                uploadPdfButton.setVisibility(View.VISIBLE);
//                viewPdfButton.setVisibility(View.GONE);
            }
        });


        uploadPdfButton.setOnClickListener(v -> {

            try {
                model.sendDetail(new Callback<Fhir4Record<DocumentReference>>() {
                    @Override
                    public void onSuccess(Fhir4Record<DocumentReference> recAord) {
                        requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "File uploaded successfully.", Toast.LENGTH_SHORT).show();
                                    viewPdfButton.setVisibility(View.VISIBLE);
                                    uploadPdfButton.setVisibility(View.GONE);
                                }
                        );
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

    private void onSummaryChanged(Map<MeasurementKind, MeasurementAggregate> value) {
        final MeasurementAggregate calories = value.get(MeasurementKind.CALORIES);
        final MeasurementAggregate distance = value.get(MeasurementKind.DISTANCE);
        final MeasurementAggregate heartRate = value.get(MeasurementKind.HEART_RATE);
        final MeasurementAggregate badPosture = value.get(MeasurementKind.BAD_POSTURE);
        final MeasurementAggregate goodPosture = value.get(MeasurementKind.GOOD_POSTURE);
        final MeasurementAggregate steps = value.get(MeasurementKind.STEPS);
        final MeasurementAggregate bloodPressure = value.get(MeasurementKind.BLOOD_PRESSURE);
        final MeasurementAggregate respiration = value.get(MeasurementKind.RESPIRATION_RATE);

        requireActivity().runOnUiThread(() -> {
            final int titleResId = (calories == null || distance == null || heartRate == null || badPosture == null || goodPosture == null || steps == null || lumbarExtensionTraining == null
                    ? R.string.fragment_report_text_view_title_no_data
                    : R.string.fragment_report_text_view_title);


            String day = String.valueOf(model.getDetailDate().getDayOfMonth());
            int monthInt = model.getDetailDate().getMonthValue();
            String month;
            switch (monthInt) {
                case 1:
                    month = getString(R.string.date_month_01);
                    break;
                case 2:
                    month = getString(R.string.date_month_02);
                    break;
                case 3:
                    month = getString(R.string.date_month_03);
                    break;
                case 4:
                    month = getString(R.string.date_month_04);
                    break;
                case 5:
                    month = getString(R.string.date_month_05);
                    break;
                case 6:
                    month = getString(R.string.date_month_06);
                    break;
                case 7:
                    month = getString(R.string.date_month_07);
                    break;
                case 8:
                    month = getString(R.string.date_month_08);
                    break;
                case 9:
                    month = getString(R.string.date_month_09);
                    break;
                case 10:
                    month = getString(R.string.date_month_10);
                    break;
                case 11:
                    month = getString(R.string.date_month_11);
                    break;
                case 12:
                    month = getString(R.string.date_month_12);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + monthInt);
            }


            String year = String.valueOf(model.getDetailDate().getYear());
            String dayMonth = day + " " + month;
            infoTextView_day.setText(dayMonth);
            infoTextView_year.setText(year);

            if (distance == null && steps == null && heartRate == null && calories == null && goodPosture == null && badPosture == null && lumbarExtensionTraining == null) {
                getInfoTextView_noData.setVisibility(View.VISIBLE);
            } else {
                getInfoTextView_noData.setVisibility(View.GONE);
            }

            if (distance != null) {
                if (distanceGroup != null) {
                    distanceGroup.setVisibility(View.VISIBLE);
                }
                distanceTotal.setText(String.valueOf(distance.getSum()));
            } else {
                if (distanceGroup != null) {
                    distanceGroup.setVisibility(View.GONE);
                }
            }
            if (steps != null) {
                if (stepsGroup != null) {
                    stepsGroup.setVisibility(View.VISIBLE);
                }
                stepsTotal.setText(String.valueOf(steps.getSum()));
            } else {
                if (stepsGroup != null) {

                    stepsGroup.setVisibility(View.GONE);

                }
            }
            if (calories != null) {
                if (caloriesGroup != null) {
                    caloriesGroup.setVisibility(View.VISIBLE);
                }
                caloriesTotal.setText(String.valueOf(calories.getSum()));
            } else {
                if (caloriesGroup != null) {

                    caloriesGroup.setVisibility(View.GONE);
                }

            }

            if (heartRate != null) {
                if (heartRateGroup != null) {
                    heartRateGroup.setVisibility(View.VISIBLE);
                }
                heartRateAvg.setText(String.format("%.1f", heartRate.getAverage()));
                heartRateMax.setText(String.valueOf(heartRate.getMax()));
                heartRateMin.setText(String.valueOf(heartRate.getMin()));
            } else if (heartRateGroup != null) {
                heartRateGroup.setVisibility(View.GONE);
            }


            if (lumbarExtensionTraining != null) {
                if (lumbarExtensionTrainingGroup != null) {
                    lumbarExtensionTrainingGroup.setVisibility(View.VISIBLE);
                }
                lumbarTrainingLength.setText(String.valueOf(lumbarExtensionTraining.getTrainingLength()));
                lumbarScore.setText(String.valueOf(lumbarExtensionTraining.getScore()));
                lumbarRepetitions.setText(String.valueOf(lumbarExtensionTraining.getRepetitions()));
                lumbarWeight.setText(String.valueOf(lumbarExtensionTraining.getWeight()));
            } else {
                lumbarExtensionTrainingGroup.setVisibility(View.GONE);
            }


            if (badPosture != null || goodPosture != null) {
                if (postureGroup != null) {
                    postureGroup.setVisibility(View.VISIBLE);
                }

                int gp = 0;

                if (goodPosture != null) {
                    gp = goodPosture.getSum().intValue();
                }

                okPostureTotal.setText(secondsToString(gp));

                int bp = 0;

                if (badPosture != null) {
                    bp = badPosture.getSum().intValue();
                }

                notOkPostureTotal.setText(secondsToString(bp));
            } else {
                postureGroup.setVisibility(View.GONE);
            }


        });
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);

        infoTextView_day = view.findViewById(R.id.fragment_report_detail_text_view_day);
        infoTextView_year = view.findViewById(R.id.fragment_report_detail_text_view_year);
        heartRateAvg = view.findViewById(R.id.fragment_report_detail_heart_rate_average);
        heartRateMax = view.findViewById(R.id.fragment_report_detail_heart_rate_max);
        heartRateMin = view.findViewById(R.id.fragment_report_detail_heart_rate_min);
        bloodPressureSBP = view.findViewById(R.id.fragment_report_blood_pressure_sbp_value);
        bloodPressureDBP = view.findViewById(R.id.fragment_report_blood_pressure_dbp_value);
        bloodPressureMeanAP = view.findViewById(R.id.fragment_report_blood_pressure_meanAP_value);
        respirationRate = bloodPressureSBP = view.findViewById(R.id.fragment_report_respiration_breathing_rate_value);
        distanceTotal = view.findViewById(R.id.fragment_report_detail_distance_total);
        caloriesTotal = view.findViewById(R.id.fragment_report_detail_calories_total);
        stepsTotal = view.findViewById(R.id.fragment_report_detail_steps_total);
        okPostureTotal = view.findViewById(R.id.fragment_report_total_time_posture_ok);
        notOkPostureTotal = view.findViewById(R.id.fragment_report_total_time_posture_not_ok);
        lumbarTrainingLength = view.findViewById(R.id.fragment_report_lumbar_extension_training_length_total);
        lumbarRepetitions = view.findViewById(R.id.fragment_report_lumbar_extension_training_repetitions_total);
        lumbarScore = view.findViewById(R.id.fragment_report_lumbar_extension_training_score_total);
        lumbarWeight = view.findViewById(R.id.fragment_report_lumbar_extension_training_weight_value);
        postureGroup = view.findViewById(R.id.postureGroup);
        heartRateGroup = view.findViewById(R.id.hearRateGroup);
        caloriesGroup = view.findViewById(R.id.caloriesGroup);
        stepsGroup = view.findViewById(R.id.stepsGroup);
        distanceGroup = view.findViewById(R.id.distanceGroup);
        bloodPressureGroup = view.findViewById(R.id.bloodPressureGroup);
        respirationGroup = view.findViewById(R.id.respirationGroup);
        lumbarExtensionTrainingGroup = view.findViewById(R.id.lumbarExtensionTrainingGroup);
        getInfoTextView_noData = view.findViewById(R.id.fragment_report_detail_view_no_data);
        model.obtainSummary(this::onSummaryChanged);
        model = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
        LumbarExtensionTrainingRepository lumbarRepository = new LumbarExtensionTrainingRepository(requireActivity().getApplication());

        try {
            lumbarExtensionTraining = lumbarRepository.getLumbarTraining(LocalDate.now()).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
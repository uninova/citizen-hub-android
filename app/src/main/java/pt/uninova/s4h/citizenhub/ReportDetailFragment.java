package pt.uninova.s4h.citizenhub;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import care.data4life.sdk.Data4LifeClient;
import org.jetbrains.annotations.NotNull;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;

public class ReportDetailFragment extends Fragment {

    private ReportViewModel model;
    private TextView infoTextView_timeSitting, infoTextView_timePosture, infoTextView_distance, infoTextView_steps,
            infoTextView_calories, infoTextView_heartrate, infoTextView_day;
    private Button uploadPdfButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_detail, container, false);

        uploadPdfButton = view.findViewById(R.id.button);
        uploadPdfButton.setOnClickListener(v -> {
            Data4LifeClient client = Data4LifeClient.getInstance();

            DocumentReference documentReference = DocumentReferenceBuilder.buildWith(
                    "Daily Report " + model.getDetailDate().toString(),
                    );
            client.createRecord();
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

        infoTextView_day.setText("Daily Activity for " + model.getDetailDate().getYear()
                + "-0" + model.getDetailDate().getMonthValue() + "-" +
                model.getDetailDate().getDayOfMonth());

        if (badPosture != null && goodPosture != null) {
            infoTextView_timeSitting.setText(getString(R.string.fragment_report_text_view_time_sitted_text,
                    badPosture.getSum() + goodPosture.getSum()));
            infoTextView_timePosture.setText(getString(R.string.fragment_report_text_view_time_posture_text, goodPosture.getSum()));
        } else {
            infoTextView_timeSitting.setVisibility(View.GONE);
            infoTextView_timePosture.setVisibility(View.GONE);
        }

        if (distance != null)
            infoTextView_distance.setText(getString(R.string.fragment_report_text_view_distance_text, distance.getSum()));
        else
            infoTextView_distance.setVisibility(View.GONE);

        if (steps != null)
            infoTextView_steps.setText(getString(R.string.fragment_report_text_view_steps_text, steps.getSum()));
        else
            infoTextView_steps.setVisibility(View.GONE);

        if (calories != null)
            infoTextView_calories.setText(getString(R.string.fragment_report_text_view_calories_text, calories.getSum()));
        else
            infoTextView_calories.setVisibility(View.GONE);

        if (heartRate != null)
            infoTextView_heartrate.setText(getString(R.string.fragment_report_text_view_heartrate_text, heartRate.getAverage()));
        else
            infoTextView_heartrate.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);

        infoTextView_day = view.findViewById(R.id.fragment_report_detail_text_view_day);
        infoTextView_timePosture = view.findViewById(R.id.fragment_report_detail_text_view_time_posture);
        infoTextView_timeSitting = view.findViewById(R.id.fragment_report_detail_text_view_time_sitting);
        infoTextView_distance = view.findViewById(R.id.fragment_report_detail_text_view_distance);
        infoTextView_steps = view.findViewById(R.id.fragment_report_detail_text_view_steps);
        infoTextView_calories = view.findViewById(R.id.fragment_report_detail_text_view_calories);
        infoTextView_heartrate = view.findViewById(R.id.fragment_report_detail_text_view_heartrate);

        model.obtainSummary(this::onSummaryChanged);

        model = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
    }
}
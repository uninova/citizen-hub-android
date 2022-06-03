package pt.uninova.s4h.citizenhub.ui.report;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uninova.s4h.citizenhub.BuildConfig;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.data.PostureValue;
import pt.uninova.s4h.citizenhub.persistence.entity.util.AggregateSummary;
import pt.uninova.s4h.citizenhub.persistence.entity.util.PostureClassificationSum;
import pt.uninova.s4h.citizenhub.ui.accounts.AccountsViewModel;

public class ReportDetailFragment extends Fragment {

    private ReportViewModel model;

    private String monthToString(int month) {
        switch (month) {
            case 1:
                return getString(R.string.date_month_01);
            case 2:
                return getString(R.string.date_month_02);
            case 3:
                return getString(R.string.date_month_03);
            case 4:
                return getString(R.string.date_month_04);
            case 5:
                return getString(R.string.date_month_05);
            case 6:
                return getString(R.string.date_month_06);
            case 7:
                return getString(R.string.date_month_07);
            case 8:
                return getString(R.string.date_month_08);
            case 9:
                return getString(R.string.date_month_09);
            case 10:
                return getString(R.string.date_month_10);
            case 11:
                return getString(R.string.date_month_11);
            case 12:
                return getString(R.string.date_month_12);
        }

        return "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_detail, container, false);

        Button uploadPdfButton = view.findViewById(R.id.uploadButton);
        Button viewPdfButton = view.findViewById(R.id.viewPdfButton);
        AccountsViewModel viewModel = new AccountsViewModel(requireActivity().getApplication());

        if (viewModel.hasSmart4HealthAccount()) {

            viewPdfButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = BuildConfig.SMART4HEALTH_APP_URL;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        } else {
            viewPdfButton.setVisibility(View.GONE);
            uploadPdfButton.setVisibility(View.GONE);
        }

        return view;
    }

    private void onDailyDataExistenceUpdate(Integer count) {
        final View view = requireView();
        final TextView noDataTextView = view.findViewById(R.id.fragment_report_detail_view_no_data);

        if (count > 0) {
            noDataTextView.setVisibility(View.GONE);
        } else {
            noDataTextView.setVisibility(View.VISIBLE);
        }
    }

    private void onDailyHeartRateUpdate(AggregateSummary value) {
        final View view = requireView();

        boolean hasHeartRate = value != null && value.getAverage() != null && value.getMaximum() != null && value.getMinimum() != null;

        if (hasHeartRate) {
            TextView heartRateAvg = view.findViewById(R.id.fragment_report_detail_heart_rate_average);
            TextView heartRateMax = view.findViewById(R.id.fragment_report_detail_heart_rate_max);
            TextView heartRateMin = view.findViewById(R.id.fragment_report_detail_heart_rate_min);

            heartRateAvg.setText(getString(R.string.heart_rate_value, value.getAverage()));
            heartRateMax.setText(getString(R.string.heart_rate_value, value.getMaximum()));
            heartRateMin.setText(getString(R.string.heart_rate_value, value.getMinimum()));
        }

        final Group heartRateGroup = view.findViewById(R.id.hearRateGroup);

        heartRateGroup.setVisibility(hasHeartRate ? View.VISIBLE : View.GONE);
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

            TextView postureCorrectTextView = view.findViewById(R.id.fragment_report_total_time_posture_ok);
            TextView postureIncorrectTextView = view.findViewById(R.id.fragment_report_total_time_posture_not_ok);

            postureCorrectTextView.setText(getString(R.string.posture_correct_value, secondsToString(pc)));
            postureIncorrectTextView.setText(getString(R.string.posture_incorrect_value, secondsToString(pi)));
        }

        final View postureGroup = view.findViewById(R.id.postureGroup);

        postureGroup.setVisibility(hasPosture ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model.getDailyDataExistence().observe(getViewLifecycleOwner(), this::onDailyDataExistenceUpdate);
        model.getDailyHeartRateSummary().observe(getViewLifecycleOwner(), this::onDailyHeartRateUpdate);
        model.getDailyPostureMeasurement().observe(getViewLifecycleOwner(), this::onDailyPostureMeasurementUpdate);

        LocalDate currentDate = model.getCurrentDate();

        String year = String.valueOf(currentDate.getYear());
        String month = monthToString(currentDate.getMonthValue());
        String day = String.valueOf(currentDate.getDayOfMonth());

        final TextView infoTextView_day = view.findViewById(R.id.fragment_report_detail_text_view_day);
        final TextView infoTextView_year = view.findViewById(R.id.fragment_report_detail_text_view_year);

        infoTextView_day.setText(String.format("%s %s", day, month));
        infoTextView_year.setText(year);
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
}

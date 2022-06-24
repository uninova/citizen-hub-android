package pt.uninova.s4h.citizenhub.ui.report;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyCallback;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;
import pt.uninova.s4h.citizenhub.report.Group;
import pt.uninova.s4h.citizenhub.report.Item;
import pt.uninova.s4h.citizenhub.report.Report;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class ReportDetailFragment extends Fragment {

    private ReportViewModel model;

    private MeasurementKindLocalization measurementKindLocalization;

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

        measurementKindLocalization = new MeasurementKindLocalization(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_detail, container, false);

        /*Button uploadPdfButton = view.findViewById(R.id.uploadButton);
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
        }*/

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LocalDate currentDate = model.getCurrentDate();

        String year = String.valueOf(currentDate.getYear());
        String month = monthToString(currentDate.getMonthValue());
        String day = String.valueOf(currentDate.getDayOfMonth());

        final TextView infoTextView_day = view.findViewById(R.id.fragment_report_detail_text_view_day);
        final TextView infoTextView_year = view.findViewById(R.id.fragment_report_detail_text_view_year);

        infoTextView_day.setText(String.format("%s %s", day, month));
        infoTextView_year.setText(year);

        Observer<Report> observerWorkTimeReport = workTimeData -> {

            Observer<Report> observerNotWorkTimeReport = notWorkTimeData -> {

                List<Group> groupsWorkTimeData = workTimeData.getGroups();
                List<Group> groupsNotWorkTimeData = notWorkTimeData.getGroups();

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                TableLayout tableLayout = view.findViewById(R.id.reportTableLayout);

                requireActivity().runOnUiThread(() -> {

                    for (Group groupNotWorkTime : groupsNotWorkTimeData) {

                        String labelNotWorkTime = groupNotWorkTime.getLabel().getLocalizedString();

                        displayTitle(tableLayout, labelNotWorkTime);

                        if (labelNotWorkTime.equals(measurementKindLocalization.localize(Measurement.TYPE_BLOOD_PRESSURE)) ||
                                labelNotWorkTime.equals(measurementKindLocalization.localize(Measurement.TYPE_LUMBAR_EXTENSION_TRAINING))) {
                            for (Group group : groupNotWorkTime.getGroupList()) {
                                String timestamp = group.getLabel().getLocalizedString();
                                displayTimestamp(tableLayout, timestamp.substring(timestamp.indexOf("T") + 1, timestamp.indexOf("Z")) + " - MyTime");
                                for (Item item : group.getItemList()) {
                                    addNewRow(tableLayout,
                                            item.getLabel().getLocalizedString(),
                                            "-",
                                            decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())),
                                            item.getUnits().getLocalizedString());
                                }
                            }
                            for(Group groupWorkTime : groupsWorkTimeData){
                                String labelWorkTime = groupWorkTime.getLabel().getLocalizedString();
                                if(labelWorkTime.equals(labelNotWorkTime)){
                                    for(Group group : groupsWorkTimeData){
                                        String timestamp = group.getLabel().getLocalizedString();
                                        displayTimestamp(tableLayout, timestamp.substring(timestamp.indexOf("T") + 1, timestamp.indexOf("Z")) + " - MyWork");
                                        for(Item item : group.getItemList()){
                                            addNewRow(tableLayout,
                                                    item.getLabel().getLocalizedString(),
                                                    "-",
                                                    decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())),
                                                    item.getUnits().getLocalizedString());
                                        }
                                    }
                                }
                            }
                        } else {
                            boolean hasGroup = false;
                            for (Group groupWorkTime : groupsWorkTimeData){
                                String labelWorkTime = groupWorkTime.getLabel().getLocalizedString();
                                if(labelNotWorkTime.equals(labelWorkTime)){
                                    hasGroup = true;
                                    for (Item itemNotWorkTime : groupNotWorkTime.getItemList()){
                                        String itemLabel = itemNotWorkTime.getLabel().getLocalizedString();
                                        for(Item itemWorkTime : groupWorkTime.getItemList()){
                                            if(itemLabel.equals(itemWorkTime.getLabel().getLocalizedString())){
                                                addNewRow(tableLayout,
                                                        itemNotWorkTime.getLabel().getLocalizedString(),
                                                        decimalFormat.format(Double.valueOf(itemNotWorkTime.getValue().getLocalizedString())),
                                                        decimalFormat.format(Double.valueOf(itemWorkTime.getValue().getLocalizedString())),
                                                        itemNotWorkTime.getUnits().getLocalizedString());
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if(!hasGroup){
                                for (Item item : groupNotWorkTime.getItemList()) {
                                    addNewRow(tableLayout,
                                            item.getLabel().getLocalizedString(),
                                            decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())),
                                            "0",
                                            item.getUnits().getLocalizedString());
                                }
                            }
                        }
                    }

                    for (Group groupWorkTime : groupsWorkTimeData){
                        String labelWorkTime = groupWorkTime.getLabel().getLocalizedString();
                        boolean hasGroup = false;
                        for (Group groupNotWorkTime : groupsNotWorkTimeData) {
                            if (labelWorkTime.equals(groupNotWorkTime.getLabel().getLocalizedString())) {
                                hasGroup = true;
                            }
                        }
                        if(!hasGroup){
                            if(labelWorkTime.equals(measurementKindLocalization.localize(Measurement.TYPE_BLOOD_PRESSURE)) ||
                                    labelWorkTime.equals(measurementKindLocalization.localize(Measurement.TYPE_LUMBAR_EXTENSION_TRAINING))){
                                for (Group group : groupWorkTime.getGroupList()) {
                                    String timestamp = group.getLabel().getLocalizedString();
                                    displayTimestamp(tableLayout, timestamp.substring(timestamp.indexOf("T") + 1, timestamp.indexOf("Z")) + " - MyWork");
                                    for (Item item : group.getItemList()) {
                                        addNewRow(tableLayout,
                                                item.getLabel().getLocalizedString(),
                                                "-",
                                                decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())),
                                                item.getUnits().getLocalizedString());
                                    }
                                }
                            }
                            else{
                                for (Item item : groupWorkTime.getItemList()) {
                                    addNewRow(tableLayout,
                                            item.getLabel().getLocalizedString(),
                                            "0",
                                            decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())),
                                            item.getUnits().getLocalizedString());
                                }
                            }
                        }
                    }
                });
            };

            model.getNotWorkTimeReport(getActivity().getApplication(), observerNotWorkTimeReport);
        };

        model.getWorkTimeReport(getActivity().getApplication(), observerWorkTimeReport);

    }

    private void displayTitle(TableLayout tableLayout, String title){
        View vTitle = LayoutInflater.from(getContext()).inflate(R.layout.fragment_report_title, null);
        TextView tvTitle = vTitle.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        tableLayout.addView(vTitle);
    }

    private void addNewRow(TableLayout tableLayout, String label, String valueMyTime, String valueWorkTime, String units){
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_report_rows, null);
        TextView tvLabel = v.findViewById(R.id.tvLabel);
        TextView tvValueMyTime = v.findViewById(R.id.tvValueMyTime);
        TextView tvValueWorkTime = v.findViewById(R.id.tvValueWorkTime);
        TextView tvUnits = v.findViewById(R.id.tvUnits);

        tvLabel.setText(label);

        if(valueMyTime.equals("-")) {
            tvValueMyTime.setVisibility(View.INVISIBLE);
        }
        else {
            tvValueMyTime.setText(valueMyTime);
        }

        tvValueWorkTime.setText(valueWorkTime);

        if(units.equals("-")) {
            tvUnits.setVisibility(View.INVISIBLE);
        }
        else {
            tvUnits.setText(units);
        }

        tableLayout.addView(v);
    }

    private void displayTimestamp(TableLayout tableLayout, String timestamp){
        View vTimestamp = (View) LayoutInflater.from(getContext()).inflate(R.layout.fragment_report_timestamp, null);
        TextView tvTimestamp = vTimestamp.findViewById(R.id.tvTimestamp);
        tvTimestamp.setText(timestamp);
        tableLayout.addView(vTimestamp);
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

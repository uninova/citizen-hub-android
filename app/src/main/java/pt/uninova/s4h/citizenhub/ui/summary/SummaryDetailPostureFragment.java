package pt.uninova.s4h.citizenhub.ui.summary;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.PostureMeasurementRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SummaryDetailPostureFragment extends Fragment {

    private SummaryViewModel model;
    private BarChart barChart;
    private LineChart lineChart;
    private PieChart pieChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_detail_posture, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationViewTime = requireView().findViewById(R.id.nav_view_time);
        bottomNavigationViewTime.setOnNavigationItemSelectedListener(this::onNavigationItemSelectedTime);

        barChart = requireView().findViewById(R.id.bar_chart);
        lineChart = requireView().findViewById(R.id.line_chart);
        pieChart = requireView().findViewById(R.id.pie_chart);

        TabLayout tabLayout = requireView().findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                if(pos == 0) {
                    System.out.println("Day");
                    lineChart.highlightValue(null);
                    //lineChart.setMarker(new MyMarkerView(getContext(), R.layout.fragment_summary_detail_line_chart_marker));
                    pieChart.setVisibility(View.VISIBLE);
                    dailyPosture();
                } else if(pos == 1) {
                    System.out.println("Week");
                    lineChart.highlightValue(null);
                    pieChart.setVisibility(View.INVISIBLE);
                    weeklyPosture();
                } else if(pos == 2) {
                    System.out.println("Month");
                    lineChart.highlightValue(null);
                    pieChart.setVisibility(View.INVISIBLE);
                    monthlyPosture();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        model.setupBarChart(barChart);
        model.setupLineChart(lineChart);
        model.setupPieChart(pieChart);
        // Specific to this fragment
        lineChart.getAxisLeft().setAxisMaximum(100);

        dailyPosture();
    }

    /*
     *
     * */
    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationItemSelectedTime(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_day:
                System.out.println("Day");
                pieChart.setVisibility(View.VISIBLE);
                dailyPosture();
                break;
            case R.id.nav_week:
                System.out.println("Week");
                pieChart.setVisibility(View.INVISIBLE);
                weeklyPosture();
                break;
            case R.id.nav_month:
                System.out.println("Month");
                pieChart.setVisibility(View.INVISIBLE);
                monthlyPosture();
                break;
        }
        return true;
    }

    private void dailyPosture() {
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(getContext());

        Observer<List<SummaryDetailUtil>> observerCorrect = correct -> {

            Observer<List<SummaryDetailUtil>> observerIncorrect = incorrect -> {

                //setStackedBar(correct, incorrect, 24);
                setAreaChart(correct, incorrect, 24);

                int correctPostureTime = 0;
                int incorrectPostureTime = 0;
                for (SummaryDetailUtil data : correct)
                    correctPostureTime += data.getValue();
                for (SummaryDetailUtil data : incorrect)
                    incorrectPostureTime += data.getValue();

                List<PieEntry> pieEntries = new ArrayList<>();
                pieEntries.add(new PieEntry(correctPostureTime, secondsToString(correctPostureTime / 1000)));
                pieEntries.add(new PieEntry(incorrectPostureTime, secondsToString(incorrectPostureTime / 1000)));
                PieDataSet dataSet = new PieDataSet(pieEntries, "");
                dataSet.setDrawValues(false);
                dataSet.setColors(ContextCompat.getColor(requireContext(), R.color.colorS4HLightBlue), ContextCompat.getColor(requireContext(), R.color.colorS4HOrange));
                PieData data = new PieData(dataSet);
                pieChart.setData(data);
                pieChart.invalidate();

            };
            postureMeasurementRepository.readLastDayIncorrectPosture(LocalDate.now(), observerIncorrect);
        };

        postureMeasurementRepository.readLastDayCorrectPosture(LocalDate.now(), observerCorrect);
    }

    private void weeklyPosture() {
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerCorrect = correct -> {
            //Observer<List<SummaryDetailUtil>> observerIncorrect = incorrect -> setStackedBar(correct, incorrect, 7);
            Observer<List<SummaryDetailUtil>> observerIncorrect = incorrect -> setAreaChart(correct, incorrect, 7);
            postureMeasurementRepository.readLastSevenDaysIncorrectPosture(LocalDate.now(), observerIncorrect);
        };
        postureMeasurementRepository.readLastSevenDaysCorrectPosture(LocalDate.now(), observerCorrect);
    }

    private void monthlyPosture() {
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerCorrect = correct -> {
            //Observer<List<SummaryDetailUtil>> observerIncorrect = incorrect -> setStackedBar(correct, incorrect, 30);
            Observer<List<SummaryDetailUtil>> observerIncorrect = incorrect -> setAreaChart(correct, incorrect, 30);
            postureMeasurementRepository.readLastThirtyDaysIncorrectPosture(LocalDate.now(), observerIncorrect);
        };
        postureMeasurementRepository.readLastThirtyDaysCorrectPosture(LocalDate.now(), observerCorrect);
    }

    private void setStackedBar(List<SummaryDetailUtil> correctPosture, List<SummaryDetailUtil> incorrectPosture, int max){
        float[] yValsCorrectPosture = new float[max];
        float[] yValsIncorrectPosture = new float[max];

        List<BarEntry> entries = new ArrayList<>();

        for (SummaryDetailUtil data : correctPosture) {
            System.out.println(data.getTime());
            yValsCorrectPosture[Math.round(data.getTime())] = data.getValue();
        }

        for (SummaryDetailUtil data : incorrectPosture) {
            yValsIncorrectPosture[Math.round(data.getTime())] = data.getValue();
        }

        for (int i = 0; i < max; i++){
            System.out.println(i);
            entries.add(new BarEntry(i, new float[]{yValsCorrectPosture[i], yValsIncorrectPosture[i]}));
        }

        BarDataSet barDataSet = new BarDataSet(entries, null);
        barDataSet.setColors(Color.parseColor("#42c5f5"), Color.parseColor("#82e4ff"));
        barDataSet.setStackLabels(new String[]{getString(R.string.summary_detail_posture_correct), getString(R.string.summary_detail_posture_incorrect)});

        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        dataSet.add(barDataSet);

        BarData barData = new BarData(dataSet);
        barData.setValueFormatter(new MyValueFormatter());

        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(model.setLabels(max)));
        //barChart.groupBars(0.2f, 0.25f, 0.05f);
        barChart.invalidate();
    }

    /*private void setBarChartDataTwoColumns(List<SummaryDetailUtil> correctPosture, List<SummaryDetailUtil> incorrectPosture, int max){
        List<BarEntry> entriesCorrectPosture = new ArrayList<>();
        List<BarEntry> entriesIncorrectPosture = new ArrayList<>();

        int currentTime = 0;

        for (SummaryDetailUtil data : correctPosture) {

            while (currentTime < data.getTime() + 1) {
                entriesCorrectPosture.add(new BarEntry(currentTime, 0));
                currentTime++;
            }
            entriesCorrectPosture.add(new BarEntry(data.getTime() + 1, data.getValue()));
            currentTime++;
        }

        while (currentTime < max) {
            entriesIncorrectPosture.add(new BarEntry(currentTime, 0));
            currentTime++;
        }

        currentTime = 0;

        for (SummaryDetailUtil data : incorrectPosture) {
            while (currentTime < data.getTime() + 1) {
                entriesIncorrectPosture.add(new BarEntry(currentTime, 0));
                currentTime++;
            }
            entriesIncorrectPosture.add(new BarEntry(data.getTime() + 1, data.getValue()));
            currentTime++;
        }

        while (currentTime < max) {
            entriesIncorrectPosture.add(new BarEntry(currentTime, 0));
            currentTime++;
        }

        BarDataSet barDataSetCorrectPosture = new BarDataSet(entriesCorrectPosture, getString(R.string.summary_detail_posture_correct));
        barDataSetCorrectPosture.setColor(Color.parseColor("#42c5f5"));
        BarDataSet barDataSetIncorrectPosture = new BarDataSet(entriesIncorrectPosture, getString(R.string.summary_detail_posture_incorrect));
        barDataSetIncorrectPosture.setColor(Color.parseColor("#82e4ff"));
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        dataSet.add(barDataSetCorrectPosture);
        dataSet.add(barDataSetIncorrectPosture);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        //barChart.groupBars(0.2f, 0.25f, 0.05f);
        barChart.invalidate();
    }*/

    private void setAreaChart(List<SummaryDetailUtil> correctPosture, List<SummaryDetailUtil> incorrectPosture, int max){
        float[] valuesCorrectPosture = new float[max];
        float[] valuesIncorrectPosture = new float[max];
        float[] offset = new float[max];

        for (SummaryDetailUtil data : incorrectPosture) {
            valuesIncorrectPosture[Math.round(data.getTime())] = data.getValue();
            offset[Math.round(data.getTime())] = data.getValue();
        }

        for (SummaryDetailUtil data : correctPosture) {
            valuesCorrectPosture[Math.round(data.getTime())] = data.getValue() + offset[Math.round(data.getTime())];
        }

        int currentTime = 0;
        float total;
        List<Entry> entriesCorrectPosture = new ArrayList<>();
        List<Entry> entriesIncorrectPosture = new ArrayList<>();

        while(currentTime < max){
            total = valuesCorrectPosture[currentTime] + valuesIncorrectPosture[currentTime];
            if(total > 3600000){
                valuesCorrectPosture[currentTime] = valuesCorrectPosture[currentTime] * 3600000 / total;
                valuesIncorrectPosture[currentTime] = valuesIncorrectPosture[currentTime] * 3600000 / total;
            }
            System.out.println(valuesCorrectPosture[currentTime]);
            System.out.println(valuesIncorrectPosture[currentTime]);
            entriesCorrectPosture.add(new BarEntry(currentTime, valuesCorrectPosture[currentTime] * 100 / 3600000));
            entriesIncorrectPosture.add(new BarEntry(currentTime, valuesIncorrectPosture[currentTime] * 100 / 3600000));
            currentTime++;
        }

        /*for (SummaryDetailUtil data : correctPosture) {
            while (currentTime < data.getTime()) {
                entriesCorrectPosture.add(new BarEntry(currentTime, 0));
                offset[currentTime] = 0;
                currentTime++;
            }
            entriesCorrectPosture.add(new BarEntry(data.getTime(), data.getValue() * 100 / 3600000));
            offset[currentTime] = data.getValue();
            currentTime++;
        }

        while (currentTime < max) {
            entriesCorrectPosture.add(new BarEntry(currentTime, 0));
            offset[currentTime] = 0;
            currentTime++;
        }

        currentTime = 0;

        for (SummaryDetailUtil data : incorrectPosture) {
            while (currentTime < data.getTime()) {
                entriesIncorrectPosture.add(new BarEntry(currentTime, offset[currentTime]));
                currentTime++;
            }
            entriesIncorrectPosture.add(new BarEntry(data.getTime(), data.getValue() + offset[currentTime]));
            currentTime++;
        }

        while (currentTime < max) {
            entriesIncorrectPosture.add(new BarEntry(currentTime, offset[currentTime]));
            currentTime++;
        }*/

        LineDataSet lineDataSetCorrectPosture = setLineDataSet(entriesCorrectPosture, getString(R.string.summary_detail_posture_correct), ContextCompat.getColor(requireContext(), R.color.colorS4HLightBlue));
        LineDataSet lineDataSetIncorrectPosture = setLineDataSet(entriesIncorrectPosture, getString(R.string.summary_detail_posture_incorrect), ContextCompat.getColor(requireContext(), R.color.colorS4HOrange));

        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
        dataSet.add(lineDataSetCorrectPosture);
        dataSet.add(lineDataSetIncorrectPosture);

        LineData lineData = new LineData(dataSet);
        lineData.setValueFormatter(new MyValueFormatter());

        lineChart.setData(lineData);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(model.setLabels(max)));
        //barChart.groupBars(0.2f, 0.25f, 0.05f);
        lineChart.invalidate();
    }

    private LineDataSet setLineDataSet(List<Entry> entries, String label, int color){
        LineDataSet lineDataSet = new LineDataSet(entries, label);
        lineDataSet.setColor(color);
        lineDataSet.setFillColor(color);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(255);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return lineDataSet;
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

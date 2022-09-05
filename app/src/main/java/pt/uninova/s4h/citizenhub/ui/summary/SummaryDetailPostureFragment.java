package pt.uninova.s4h.citizenhub.ui.summary;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private BottomNavigationView bottomNavigationViewPosture;

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

        barChart = requireView().findViewById(R.id.bar_chart);
        lineChart = requireView().findViewById(R.id.line_chart);
        pieChart = requireView().findViewById(R.id.pie_chart);

        BottomNavigationView bottomNavigationViewTime = requireView().findViewById(R.id.nav_view_time);
        bottomNavigationViewTime.setOnNavigationItemSelectedListener(this::onNavigationItemSelectedTime);

        bottomNavigationViewPosture = requireView().findViewById(R.id.nav_view_posture);
        bottomNavigationViewPosture.setOnNavigationItemSelectedListener(this::onNavigationItemSelectedPosture);
        bottomNavigationViewPosture.setVisibility(View.INVISIBLE);

        setupBarChart();
        setupLineChart();
        dailyPosture();
    }

    private void setupBarChart() {
        barChart.setDrawGridBackground(false);
        barChart.setScaleEnabled(true);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setDrawGridLines(false);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    private void setupLineChart() {
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setText("Steps");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setDrawGridLines(false);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
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
                bottomNavigationViewPosture.setVisibility(View.INVISIBLE);
                barChart.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.INVISIBLE);
                pieChart.setVisibility(View.VISIBLE);
                dailyPosture();
                break;
            case R.id.nav_week:
                System.out.println("Week");
                bottomNavigationViewPosture.setVisibility(View.INVISIBLE);
                barChart.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.INVISIBLE);
                pieChart.setVisibility(View.INVISIBLE);
                weeklyPosture();
                break;
            case R.id.nav_month:
                System.out.println("Month");
                bottomNavigationViewPosture.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.INVISIBLE);
                pieChart.setVisibility(View.INVISIBLE);
                monthlyAverageCorrectPostureTime();
                break;
        }
        return true;
    }

    /*
     *
     * */
    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationItemSelectedPosture(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_average:
                System.out.println("Main");
                barChart.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.INVISIBLE);
                monthlyAverageCorrectPostureTime();
                break;
            case R.id.nav_correct_posture:
                System.out.println("Correct");
                barChart.setVisibility(View.INVISIBLE);
                lineChart.setVisibility(View.VISIBLE);
                monthlyCorrectPosture();
                break;
            case R.id.nav_incorrect_posture:
                System.out.println("Incorrect");
                barChart.setVisibility(View.INVISIBLE);
                lineChart.setVisibility(View.VISIBLE);
                monthlyIncorrectPosture();
                break;
        }
        return true;
    }

    private void dailyPosture() {
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(getContext());

        Observer<List<SummaryDetailUtil>> observerCorrect = correct -> {

            Observer<List<SummaryDetailUtil>> observerIncorrect = incorrect -> {

                setBarChartDataTwoColumns(correct, incorrect, 24);

                int correctPostureTime = 0;
                int incorrectPostureTime = 0;
                for (SummaryDetailUtil data : correct)
                    correctPostureTime += data.getValue();
                for (SummaryDetailUtil data : incorrect)
                    incorrectPostureTime += data.getValue();

                List<PieEntry> pieEntries = new ArrayList<>();
                pieEntries.add(new PieEntry(correctPostureTime, 0));
                pieEntries.add(new PieEntry(incorrectPostureTime, 1));
                PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                pieDataSet.setColors(Color.BLUE, Color.RED);
                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();

            };
            postureMeasurementRepository.readLastDayIncorrectPosture(LocalDate.now(), observerIncorrect);
        };

        postureMeasurementRepository.readLastDayCorrectPosture(LocalDate.now(), observerCorrect);
    }

    private void weeklyPosture() {
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(getContext());

        Observer<List<SummaryDetailUtil>> observerCorrect = correctPosture -> {
            Observer<List<SummaryDetailUtil>> observerIncorrect = incorrectPosture -> setBarChartDataTwoColumns(correctPosture, incorrectPosture, 7);
            postureMeasurementRepository.readLastSevenDaysIncorrectPosture(LocalDate.now(), observerIncorrect);
        };

        postureMeasurementRepository.readLastSevenDaysCorrectPosture(LocalDate.now(), observerCorrect);
    }

    private void monthlyAverageCorrectPostureTime() {

    }

    private void monthlyCorrectPosture() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart,getString(R.string.summary_detail_posture_correct), 30);
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(getContext());
        postureMeasurementRepository.readLastThirtyDaysCorrectPosture(LocalDate.now(), observer);
    }

    private void monthlyIncorrectPosture() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_posture_incorrect), 30);
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(getContext());
        postureMeasurementRepository.readLastThirtyDaysIncorrectPosture(LocalDate.now(), observer);
    }

    private void setBarChartDataTwoColumns(List<SummaryDetailUtil> correctPosture, List<SummaryDetailUtil> incorrectPosture, int max){
        List<BarEntry> entriesCorrectPosture = new ArrayList<>();
        List<BarEntry> entriesIncorrectPosture = new ArrayList<>();

        int currentTime = 0;

        for (SummaryDetailUtil data : correctPosture) {
            if (currentTime != data.getTime()) {
                while (currentTime != data.getTime()) {
                    currentTime++;
                    entriesCorrectPosture.add(new BarEntry(currentTime, 0));
                }
            }
            entriesCorrectPosture.add(new BarEntry(data.getTime() + 1, data.getValue()));
            currentTime++;
        }

        if (currentTime != max) {
            while (currentTime != max) {
                entriesIncorrectPosture.add(new BarEntry(currentTime, 0));
                currentTime++;
            }
        }

        currentTime = 0;

        for (SummaryDetailUtil data : incorrectPosture) {
            if (currentTime != data.getTime()) {
                while (currentTime != data.getTime()) {
                    currentTime++;
                    entriesIncorrectPosture.add(new BarEntry(currentTime, 0));
                }
            }
            entriesIncorrectPosture.add(new BarEntry(data.getTime() + 1, data.getValue()));
            currentTime++;
        }

        if (currentTime != max) {
            while (currentTime != max) {
                entriesIncorrectPosture.add(new BarEntry(currentTime, 0));
                currentTime++;
            }
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
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

}

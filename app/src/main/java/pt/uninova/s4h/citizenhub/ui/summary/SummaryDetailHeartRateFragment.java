package pt.uninova.s4h.citizenhub.ui.summary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.HeartRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SummaryDetailHeartRateFragment extends Fragment {

    private SummaryViewModel model;
    private LineChart lineChart;
    private BottomNavigationView bottomNavigationViewHeartRate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_detail_heart_rate, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationViewTime = requireView().findViewById(R.id.nav_view_time);
        bottomNavigationViewTime.setOnNavigationItemSelectedListener(this::onNavigationItemSelectedTime);

        bottomNavigationViewHeartRate = requireView().findViewById(R.id.nav_view_heart_rate);
        bottomNavigationViewHeartRate.setOnNavigationItemSelectedListener(this::onNavigationItemSelectedHeartRate);
        lineChart = requireView().findViewById(R.id.line_chart);

        setupLineChart();
        dailyHeartRate();

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
                bottomNavigationViewHeartRate.setVisibility(View.INVISIBLE);
                dailyHeartRate();
                break;
            case R.id.nav_week:
                System.out.println("Week");
                bottomNavigationViewHeartRate.setVisibility(View.INVISIBLE);
                weeklyHeartRate();
                break;
            case R.id.nav_month:
                System.out.println("Month");
                bottomNavigationViewHeartRate.setVisibility(View.VISIBLE);
                monthlyAverageHeartRate();
                break;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationItemSelectedHeartRate(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_average:
                System.out.println("Average");
                monthlyAverageHeartRate();
                break;
            case R.id.nav_max:
                System.out.println("Max");
                monthlyMaxHeartRate();
                break;
            case R.id.nav_min:
                System.out.println("Min");
                monthlyMinHeartRate();
                break;
        }
        return true;
    }

    private void dailyHeartRate(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, "Heart Rate", 24);
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        heartRateMeasurementRepository.readLastDay(LocalDate.now(), observer);
    }

    private void weeklyHeartRate(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, "Heart Rate", 7);
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        heartRateMeasurementRepository.readLastSevenDays(LocalDate.now(), observer);
    }

    private void monthlyAverageHeartRate(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_heart_rate_average), 30);
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        heartRateMeasurementRepository.readAverageLastThirtyDays(LocalDate.now(), observer);
    }

    private void monthlyMaxHeartRate(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_heart_rate_maximum), 30);
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        heartRateMeasurementRepository.readMaxLastThirtyDays(LocalDate.now(), observer);
    }

    private void monthlyMinHeartRate(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_heart_rate_minimum), 30);
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        heartRateMeasurementRepository.readMinLastThirtyDays(LocalDate.now(), observer);
    }

}

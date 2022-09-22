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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.BloodPressureMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.PulseRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SummaryDetailBloodPressureFragment extends Fragment {

    private SummaryViewModel model;
    private LineChart lineChart;
    private BarChart barChart;
    private BottomNavigationView bottomNavigationViewTime;
    private BottomNavigationView bottomNavigationViewBloodPressure;
    private TabLayout tabLayoutPrimary;
    private TabLayout tabLayoutSecondary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_detail_blood_pressure, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        barChart = view.findViewById(R.id.bar_chart);
        lineChart = view.findViewById(R.id.line_chart);

        bottomNavigationViewTime = requireView().findViewById(R.id.nav_view_time);
        bottomNavigationViewTime.setOnNavigationItemSelectedListener(this::onNavigationItemSelectedTime);

        bottomNavigationViewBloodPressure = requireView().findViewById(R.id.nav_view_blood_pressure);
        bottomNavigationViewBloodPressure.setOnNavigationItemSelectedListener(this::onNavigationItemSelectedBloodPressure);

        tabLayoutPrimary = requireView().findViewById(R.id.tab_layout_primary);
        tabLayoutSecondary = requireView().findViewById((R.id.tab_layout_secondary));

        tabLayoutPrimary.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                if(pos == 0) {
                    System.out.println("Day");
                    barChart.highlightValue(null);
                    switch(tabLayoutSecondary.getSelectedTabPosition()) {
                        case 0: dailySystolic(); break;
                        case 1: dailyDiastolic(); break;
                        case 2: dailyMean(); break;
                    }
                } else if(pos == 1) {
                    System.out.println("Week");
                    barChart.highlightValue(null);
                    switch(tabLayoutSecondary.getSelectedTabPosition()) {
                        case 0: weeklySystolic(); break;
                        case 1: weeklyDiastolic(); break;
                        case 2: weeklyMean(); break;
                    }
                } else if(pos == 2) {
                    System.out.println("Month");
                    barChart.highlightValue(null);
                    switch(tabLayoutSecondary.getSelectedTabPosition()) {
                        case 0: monthlySystolic(); break;
                        case 1: monthlyDiastolic(); break;
                        case 2: monthlyMean(); break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayoutSecondary.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                if(pos == 0) {
                    System.out.println("Day");
                    barChart.highlightValue(null);
                    switch(tabLayoutPrimary.getSelectedTabPosition()) {
                        case 0: dailySystolic(); break;
                        case 1: weeklySystolic(); break;
                        case 2: monthlySystolic(); break;
                    }
                } else if(pos == 1) {
                    System.out.println("Week");
                    barChart.highlightValue(null);
                    switch(tabLayoutPrimary.getSelectedTabPosition()) {
                        case 0: dailyDiastolic(); break;
                        case 1: weeklyDiastolic(); break;
                        case 2: monthlyDiastolic(); break;
                    }
                } else if(pos == 2) {
                    System.out.println("Month");
                    barChart.highlightValue(null);
                    switch(tabLayoutPrimary.getSelectedTabPosition()) {
                        case 0: dailyMean(); break;
                        case 1: weeklyMean(); break;
                        case 2: monthlyMean(); break;
                    }
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

        dailySystolic();
        dailyPulseRate();
    }

    /*
     *
     * */
    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationItemSelectedTime(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_day:
                switch (bottomNavigationViewBloodPressure.getSelectedItemId()) {
                    case R.id.nav_systolic: dailySystolic(); break;
                    case R.id.nav_diastolic: dailyDiastolic(); break;
                    case R.id.nav_mean: dailyMean(); break;
                }
                dailyPulseRate();
                break;
            case R.id.nav_week:
                switch (bottomNavigationViewBloodPressure.getSelectedItemId()) {
                    case R.id.nav_systolic: weeklySystolic(); break;
                    case R.id.nav_diastolic: weeklyDiastolic(); break;
                    case R.id.nav_mean: weeklyMean(); break;
                }
                weeklyPulseRate();
                break;
            case R.id.nav_month:
                switch (bottomNavigationViewBloodPressure.getSelectedItemId()) {
                    case R.id.nav_systolic: monthlySystolic(); break;
                    case R.id.nav_diastolic: monthlyDiastolic(); break;
                    case R.id.nav_mean: monthlyMean(); break;
                }
                monthPulseRate();
                break;
        }
        return true;
    }

    /*
     *
     * */
    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationItemSelectedBloodPressure(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_systolic:
                System.out.println("Systolic");
                switch (bottomNavigationViewTime.getSelectedItemId()){
                    case R.id.nav_day: dailySystolic(); break;
                    case R.id.nav_week: weeklySystolic(); break;
                    case R.id.nav_month: monthlySystolic(); break;
                }
                break;
            case R.id.nav_diastolic:
                System.out.println("Diastolic");
                switch (bottomNavigationViewTime.getSelectedItemId()){
                    case R.id.nav_day: dailyDiastolic(); System.out.println("Aqyu"); break;
                    case R.id.nav_week: weeklyDiastolic(); break;
                    case R.id.nav_month: monthlyDiastolic(); break;
                }
                break;
            case R.id.nav_mean:
                System.out.println("Mean");
                switch (bottomNavigationViewTime.getSelectedItemId()){
                    case R.id.nav_day: dailyMean(); break;
                    case R.id.nav_week: weeklyMean(); break;
                    case R.id.nav_month: monthlyMean(); break;
                }
                break;
        }
        return true;
    }

    private void dailySystolic() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setBarChartData(data, barChart, getString(R.string.summary_detail_blood_pressure_systolic), 24);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.readLastDaySystolic(LocalDate.now(), observer);
    }

    private void weeklySystolic() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setBarChartData(data, barChart, getString(R.string.summary_detail_blood_pressure_systolic), 7);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.readLastSevenDaysSystolic(LocalDate.now(), observer);
    }

    private void monthlySystolic() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setBarChartData(data, barChart, getString(R.string.summary_detail_blood_pressure_systolic), 30);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.readLastThirtyDaysSystolic(LocalDate.now(), observer);
    }

    private void dailyDiastolic() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setBarChartData(data, barChart, getString(R.string.summary_detail_blood_pressure_diastolic), 24);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.readLastDayDiastolic(LocalDate.now(), observer);
    }

    private void weeklyDiastolic() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setBarChartData(data, barChart, getString(R.string.summary_detail_blood_pressure_diastolic), 7);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.readLastSevenDaysDiastolic(LocalDate.now(), observer);
    }

    private void monthlyDiastolic(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setBarChartData(data, barChart, getString(R.string.summary_detail_blood_pressure_diastolic), 30);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.readLastThirtyDaysDiastolic(LocalDate.now(), observer);
    }

    private void dailyMean(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setBarChartData(data, barChart, getString(R.string.summary_detail_blood_pressure_mean), 24);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.readLastDayMean(LocalDate.now(), observer);
    }

    private void weeklyMean(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setBarChartData(data, barChart, getString(R.string.summary_detail_blood_pressure_mean), 7);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.readLastSevenDaysMean(LocalDate.now(), observer);
    }

    private void monthlyMean(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setBarChartData(data, barChart, getString(R.string.summary_detail_blood_pressure_mean), 30);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.readLastThirtyDaysMean(LocalDate.now(), observer);
    }

    private void dailyPulseRate(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(Arrays.asList(data), lineChart, new String[]{getString(R.string.summary_detail_blood_pressure_pulse_rate)}, 24);
        PulseRateMeasurementRepository pulseRateMeasurementRepository = new PulseRateMeasurementRepository(getContext());
        pulseRateMeasurementRepository.readLastDay(LocalDate.now(), observer);
    }

    private void weeklyPulseRate(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(Arrays.asList(data), lineChart, new String[]{getString(R.string.summary_detail_blood_pressure_pulse_rate)}, 7);
        PulseRateMeasurementRepository pulseRateMeasurementRepository = new PulseRateMeasurementRepository(getContext());
        pulseRateMeasurementRepository.readLastSevenDays(LocalDate.now(), observer);
    }

    private void monthPulseRate(){
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(Arrays.asList(data), lineChart, new String[]{getString(R.string.summary_detail_blood_pressure_pulse_rate)}, 30);
        PulseRateMeasurementRepository pulseRateMeasurementRepository = new PulseRateMeasurementRepository(getContext());
        pulseRateMeasurementRepository.readLastThirtyDays(LocalDate.now(), observer);
    }

}

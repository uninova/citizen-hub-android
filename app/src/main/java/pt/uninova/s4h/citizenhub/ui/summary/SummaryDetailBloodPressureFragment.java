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
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailBloodPressureUtil;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.BloodPressureMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.HeartRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.PulseRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SummaryDetailBloodPressureFragment extends Fragment {

    private SummaryViewModel model;
    private LineChart lineChart;
    private ChartFunctions chartFunctions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
        chartFunctions = new ChartFunctions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_detail_blood_pressure, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        lineChart = view.findViewById(R.id.line_chart);

        TabLayout tabLayout = requireView().findViewById(R.id.tab_layout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                if(pos == 0) {
                    lineChart.highlightValue(null);
                    lineChart.getXAxis().setAxisMaximum(23);
                    dailyBloodPressure();
                } else if(pos == 1) {
                    lineChart.highlightValue(null);
                    lineChart.getXAxis().resetAxisMaximum();
                    weeklyBloodPressure();
                } else if(pos == 2) {
                    lineChart.highlightValue(null);
                    lineChart.getXAxis().resetAxisMaximum();
                    monthlyBloodPressure();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        model.setupLineChart(lineChart);
        lineChart.getXAxis().setAxisMaximum(23);
        dailyBloodPressure();
    }

    private void dailyBloodPressure() {
        Observer<List<SummaryDetailBloodPressureUtil>> observer = data -> model.setLineChartDataTest(chartFunctions.parseBloodPressureUtil(data), lineChart, new String[]{getString(R.string.summary_detail_blood_pressure_systolic), getString(R.string.summary_detail_blood_pressure_diastolic), getString(R.string.summary_detail_blood_pressure_mean)}, 24);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.selectLastDay(LocalDate.now(), observer);
        /*BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerSystolic = dataSystolic -> {
            Observer<List<SummaryDetailUtil>> observerDiastolic = dataDiastolic -> {
                Observer<List<SummaryDetailUtil>> observerMean = dataMean -> model.setLineChartData(Arrays.asList(dataSystolic, dataDiastolic, dataMean), lineChart, new String[]{getString(R.string.summary_detail_blood_pressure_systolic), getString(R.string.summary_detail_blood_pressure_diastolic), getString(R.string.summary_detail_blood_pressure_mean)}, 24);
                bloodPressureMeasurementRepository.readLastDayMean(LocalDate.now(), observerMean);
            };
            bloodPressureMeasurementRepository.readLastDayDiastolic(LocalDate.now(), observerDiastolic);
        };
        bloodPressureMeasurementRepository.readLastDaySystolic(LocalDate.now(), observerSystolic);*/
    }

    private void weeklyBloodPressure() {
        Observer<List<SummaryDetailBloodPressureUtil>> observer = data -> model.setLineChartDataTest(chartFunctions.parseBloodPressureUtil(data), lineChart, new String[]{getString(R.string.summary_detail_blood_pressure_systolic), getString(R.string.summary_detail_blood_pressure_diastolic), getString(R.string.summary_detail_blood_pressure_mean)}, 7);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.selectSeveralDays(LocalDate.now(), 7, observer);
        /*BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerSystolic = dataSystolic -> {
            Observer<List<SummaryDetailUtil>> observerDiastolic = dataDiastolic -> {
                Observer<List<SummaryDetailUtil>> observerMean = dataMean -> model.setLineChartData(Arrays.asList(dataSystolic, dataDiastolic, dataMean), lineChart, new String[]{getString(R.string.summary_detail_blood_pressure_systolic), getString(R.string.summary_detail_blood_pressure_diastolic), getString(R.string.summary_detail_blood_pressure_mean)}, 7);
                bloodPressureMeasurementRepository.readLastSevenDaysMean(LocalDate.now(), observerMean);
            };
            bloodPressureMeasurementRepository.readLastSevenDaysDiastolic(LocalDate.now(), observerDiastolic);
        };
        bloodPressureMeasurementRepository.readLastSevenDaysSystolic(LocalDate.now(), observerSystolic);*/
    }

    private void monthlyBloodPressure() {
        Observer<List<SummaryDetailBloodPressureUtil>> observer = data -> model.setLineChartDataTest(chartFunctions.parseBloodPressureUtil(data), lineChart, new String[]{getString(R.string.summary_detail_blood_pressure_systolic), getString(R.string.summary_detail_blood_pressure_diastolic), getString(R.string.summary_detail_blood_pressure_mean)}, 30);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        bloodPressureMeasurementRepository.selectSeveralDays(LocalDate.now(), 30, observer);
       /* BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerSystolic = dataSystolic -> {
            Observer<List<SummaryDetailUtil>> observerDiastolic = dataDiastolic -> {
                Observer<List<SummaryDetailUtil>> observerMean = dataMean -> model.setLineChartData(Arrays.asList(dataSystolic, dataDiastolic, dataMean), lineChart, new String[]{getString(R.string.summary_detail_blood_pressure_systolic), getString(R.string.summary_detail_blood_pressure_diastolic), getString(R.string.summary_detail_blood_pressure_mean)}, 30);
                bloodPressureMeasurementRepository.readLastThirtyDaysMean(LocalDate.now(), observerMean);
            };
            bloodPressureMeasurementRepository.readLastThirtyDaysDiastolic(LocalDate.now(), observerDiastolic);
        };
        bloodPressureMeasurementRepository.readLastThirtyDaysSystolic(LocalDate.now(), observerSystolic);*/
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

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
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.HeartRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SummaryDetailHeartRateFragment extends Fragment {

    private SummaryViewModel model;
    private LineChart lineChart;

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

        TabLayout tabLayout = requireView().findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                if(pos == 0) {
                    System.out.println("Day");
                    lineChart.highlightValue(null);
                    lineChart.getXAxis().setAxisMaximum(23);
                    dailyHeartRate();
                } else if(pos == 1) {
                    System.out.println("Week");
                    lineChart.highlightValue(null);
                    lineChart.getXAxis().resetAxisMaximum();
                    weeklyHeartRate();
                } else if(pos == 2) {
                    System.out.println("Month");
                    lineChart.highlightValue(null);
                    lineChart.getXAxis().resetAxisMaximum();
                    monthlyHeartRate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        lineChart = requireView().findViewById(R.id.line_chart);
        model.setupLineChart(lineChart);
        // Specific to this fragment
        lineChart.getXAxis().setAxisMaximum(23);

        dailyHeartRate();
    }

    private void dailyHeartRate(){
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerAvg = dataAvg -> {
            Observer<List<SummaryDetailUtil>> observerMax = dataMax -> {
                Observer<List<SummaryDetailUtil>> observerMin = dataMin -> model.setLineChartData(Arrays.asList(dataAvg, dataMax, dataMin), lineChart, new String[]{getString(R.string.summary_detail_heart_rate_average), getString(R.string.summary_detail_heart_rate_maximum), getString(R.string.summary_detail_heart_rate_minimum)}, 24);
                heartRateMeasurementRepository.readMinLastDay(observerMin, LocalDate.now());
            };
            heartRateMeasurementRepository.readMaxLastDay(observerMax, LocalDate.now());
        };
        heartRateMeasurementRepository.readAvgLastDay(observerAvg, LocalDate.now());
    }

    private void weeklyHeartRate(){
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerAvg = dataAvg -> {
            Observer<List<SummaryDetailUtil>> observerMax = dataMax -> {
                Observer<List<SummaryDetailUtil>> observerMin = dataMin -> model.setLineChartData(Arrays.asList(dataAvg, dataMax, dataMin), lineChart, new String[]{getString(R.string.summary_detail_heart_rate_average), getString(R.string.summary_detail_heart_rate_maximum), getString(R.string.summary_detail_heart_rate_minimum)}, 7);
                heartRateMeasurementRepository.readMinLastSevenDays(observerMin, LocalDate.now());
            };
            heartRateMeasurementRepository.readMaxLastSevenDays(observerMax, LocalDate.now());
        };
        heartRateMeasurementRepository.readAvgLastSevenDays(observerAvg, LocalDate.now());
    }

    private void monthlyHeartRate(){
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerAvg = dataAvg -> {
            Observer<List<SummaryDetailUtil>> observerMax = dataMax -> {
                Observer<List<SummaryDetailUtil>> observerMin = dataMin -> model.setLineChartData(Arrays.asList(dataAvg, dataMax, dataMin), lineChart, new String[]{getString(R.string.summary_detail_heart_rate_average), getString(R.string.summary_detail_heart_rate_maximum), getString(R.string.summary_detail_heart_rate_minimum)}, 30 );
                heartRateMeasurementRepository.readMinLastThirtyDays(observerMin, LocalDate.now());
            };
            heartRateMeasurementRepository.readMaxLastThirtyDays(observerMax, LocalDate.now());
        };
        heartRateMeasurementRepository.readAvgLastThirtyDays(observerAvg, LocalDate.now());
    }

}

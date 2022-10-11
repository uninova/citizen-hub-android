package pt.uninova.s4h.citizenhub.ui.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailHeartRateUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.HeartRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SummaryDetailHeartRateFragment extends Fragment {

    private SummaryViewModel model;
    private ChartFunctions chartFunctions;
    private LineChart lineChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
        chartFunctions = new ChartFunctions(getContext());
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
                    lineChart.highlightValue(null);
                    lineChart.getXAxis().setAxisMaximum(23);
                    dailyHeartRate();
                } else if(pos == 1) {
                    lineChart.highlightValue(null);
                    lineChart.getXAxis().resetAxisMaximum();
                    weeklyHeartRate();
                } else if(pos == 2) {
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
        chartFunctions.setupLineChart(lineChart, model.getChartViewMarker());
        // Specific to this fragment
        lineChart.getXAxis().setAxisMaximum(23);

        dailyHeartRate();
    }

    private void dailyHeartRate(){
        Observer<List<SummaryDetailHeartRateUtil>> observer = data -> chartFunctions.setLineChartData(lineChart, chartFunctions.parseHeartRateUtil(data),new String[]{getString(R.string.summary_detail_heart_rate_average), getString(R.string.summary_detail_heart_rate_maximum), getString(R.string.summary_detail_heart_rate_minimum)}, 24);
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        heartRateMeasurementRepository.selectLastDay(LocalDate.now(), observer);
    }

    private void weeklyHeartRate(){
        Observer<List<SummaryDetailHeartRateUtil>> observer = data -> chartFunctions.setLineChartData(lineChart, chartFunctions.parseHeartRateUtil(data), new String[]{getString(R.string.summary_detail_heart_rate_average), getString(R.string.summary_detail_heart_rate_maximum), getString(R.string.summary_detail_heart_rate_minimum)}, 7);
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        heartRateMeasurementRepository.selectSeveralDays(LocalDate.now(), 7, observer);
    }

    private void monthlyHeartRate(){
        Observer<List<SummaryDetailHeartRateUtil>> observer = data -> chartFunctions.setLineChartData(lineChart, chartFunctions.parseHeartRateUtil(data), new String[]{getString(R.string.summary_detail_heart_rate_average), getString(R.string.summary_detail_heart_rate_maximum), getString(R.string.summary_detail_heart_rate_minimum)}, 30);
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(getContext());
        heartRateMeasurementRepository.selectSeveralDays(LocalDate.now(), 30, observer);
    }

}

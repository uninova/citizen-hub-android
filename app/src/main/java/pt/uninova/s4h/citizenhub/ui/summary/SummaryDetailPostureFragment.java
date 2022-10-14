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
    private ChartFunctions chartFunctions;
    private LineChart lineChart;
    private PieChart pieChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
        chartFunctions = new ChartFunctions(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_detail_posture, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        chartFunctions.setupLineChart(lineChart, model.getChartViewMarker());
        chartFunctions.setupPieChart(pieChart);
        // Specific to this fragment
        lineChart.getAxisLeft().setAxisMaximum(100);

        dailyPosture();
    }

    private void dailyPosture() {
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerCorrect = correct -> {
            Observer<List<SummaryDetailUtil>> observerIncorrect = incorrect -> {
                chartFunctions.setAreaChart(lineChart, correct, incorrect, new String[]{getString(R.string.summary_detail_posture_correct), getString(R.string.summary_detail_posture_incorrect)}, 24);
                chartFunctions.setPieChart(pieChart, correct, incorrect);
            };
            postureMeasurementRepository.readLastDayIncorrectPosture(LocalDate.now(), observerIncorrect);
        };
        postureMeasurementRepository.readLastDayCorrectPosture(LocalDate.now(), observerCorrect);
    }

    private void weeklyPosture() {
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerCorrect = correct -> {
            Observer<List<SummaryDetailUtil>> observerIncorrect = incorrect -> chartFunctions.setAreaChart(lineChart, correct, incorrect, new String[] {getString(R.string.summary_detail_posture_correct), getString(R.string.summary_detail_posture_incorrect)}, 7);
            postureMeasurementRepository.readLastSevenDaysIncorrectPosture(LocalDate.now(), observerIncorrect);
        };
        postureMeasurementRepository.readLastSevenDaysCorrectPosture(LocalDate.now(), observerCorrect);
    }

    private void monthlyPosture() {
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(getContext());
        Observer<List<SummaryDetailUtil>> observerCorrect = correct -> {
            Observer<List<SummaryDetailUtil>> observerIncorrect = incorrect -> chartFunctions.setAreaChart(lineChart, correct, incorrect, new String[] {getString(R.string.summary_detail_posture_correct), getString(R.string.summary_detail_posture_incorrect)}, 30);
            postureMeasurementRepository.readLastThirtyDaysIncorrectPosture(LocalDate.now(), observerIncorrect);
        };
        postureMeasurementRepository.readLastThirtyDaysCorrectPosture(LocalDate.now(), observerCorrect);
    }

}

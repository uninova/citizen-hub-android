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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SummaryDetailLumbarExtensionFragment extends Fragment {

    private SummaryViewModel model;
    private LineChart lineChart;
    private BottomNavigationView bottomNavigationViewTime;
    private BottomNavigationView bottomNavigationViewLumbarExtension;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_detail_lumbar_extension, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lineChart = requireView().findViewById(R.id.line_chart);
        model.setupLineChart(lineChart);

        bottomNavigationViewTime = requireView().findViewById(R.id.nav_view_time);
        bottomNavigationViewTime.setOnNavigationItemSelectedListener(this::onNavigationItemSelectedTime);

        bottomNavigationViewLumbarExtension = requireView().findViewById(R.id.nav_view_lumbar_extension);
        bottomNavigationViewLumbarExtension.setOnNavigationItemSelectedListener(this::onNavigationItemSelectedLumbarExtension);

        model.setupLineChart(lineChart);
        dailyDuration();
    }

    /*
     *
     * */
    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationItemSelectedTime(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_day:
                switch (bottomNavigationViewLumbarExtension.getSelectedItemId()) {
                    case R.id.nav_duration: dailyDuration(); break;
                    case R.id.nav_score: dailyScore(); break;
                    case R.id.nav_repetitions: dailyRepetitions(); break;
                    case R.id.nav_weight: dailyWeight(); break;
                }
                break;
            case R.id.nav_week:
                switch (bottomNavigationViewLumbarExtension.getSelectedItemId()) {
                    case R.id.nav_duration: weeklyDuration(); break;
                    case R.id.nav_score: weeklyScore(); break;
                    case R.id.nav_repetitions: weeklyRepetitions(); break;
                    case R.id.nav_weight: weeklyWeight(); break;
                }
                break;
            case R.id.nav_month:
                switch (bottomNavigationViewLumbarExtension.getSelectedItemId()) {
                    case R.id.nav_duration: monthlyDuration(); break;
                    case R.id.nav_score: monthlyScore(); break;
                    case R.id.nav_repetitions: monthlyRepetitions(); break;
                    case R.id.nav_weight: monthlyWeight(); break;
                }
                break;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationItemSelectedLumbarExtension(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_duration:
                switch (bottomNavigationViewTime.getSelectedItemId()) {
                    case R.id.nav_day: dailyDuration(); break;
                    case R.id.nav_week: weeklyDuration(); break;
                    case R.id.nav_month: monthlyDuration(); break;
                }
                break;
            case R.id.nav_score:
                switch (bottomNavigationViewTime.getSelectedItemId()) {
                    case R.id.nav_day: dailyScore(); break;
                    case R.id.nav_week: weeklyScore(); break;
                    case R.id.nav_month: monthlyScore(); break;
                }
                break;
            case R.id.nav_repetitions:
                switch (bottomNavigationViewTime.getSelectedItemId()) {
                    case R.id.nav_day: dailyRepetitions(); break;
                    case R.id.nav_week: weeklyRepetitions(); break;
                    case R.id.nav_month: monthlyRepetitions(); break;
                }
                break;
            case R.id.nav_weight:
                switch (bottomNavigationViewTime.getSelectedItemId()) {
                    case R.id.nav_day: dailyWeight(); break;
                    case R.id.nav_week: weeklyWeight(); break;
                    case R.id.nav_month: monthlyWeight(); break;
                }
                break;
        }
        return true;
    }

    private void dailyDuration() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_duration), 24);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastDayDuration(LocalDate.now(), observer);
    }

    private void weeklyDuration() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_duration), 7);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastSevenDaysDuration(LocalDate.now(), observer);
    }

    private void monthlyDuration() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_score), 30);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastThirtyDaysDuration(LocalDate.now(), observer);
    }

    private void dailyScore() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_score), 24);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastDayScore(LocalDate.now(), observer);
    }

    private void weeklyScore() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_score), 7);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastSevenDaysScore(LocalDate.now(), observer);
    }

    private void monthlyScore() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_score), 30);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastThirtyDaysScore(LocalDate.now(), observer);
    }

    private void dailyRepetitions() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_repetitions), 24);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastDayRepetitions(LocalDate.now(), observer);
    }

    private void weeklyRepetitions() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_repetitions), 7);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastSevenDaysRepetitions(LocalDate.now(), observer);
    }

    private void monthlyRepetitions() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_repetitions), 30);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastThirtyDaysRepetitions(LocalDate.now(), observer);
    }

    private void dailyWeight() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_weight), 24);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastDayWeight(LocalDate.now(), observer);
    }

    private void weeklyWeight() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_weight), 7);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastSevenDaysWeight(LocalDate.now(), observer);
    }

    private void monthlyWeight() {
        Observer<List<SummaryDetailUtil>> observer = data -> model.setLineChartData(data, lineChart, getString(R.string.summary_detail_lumbar_extension_weight), 30);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(getContext());
        lumbarExtensionTrainingRepository.readLastThirtyDaysWeight(LocalDate.now(), observer);
    }

}

package pt.uninova.s4h.citizenhub.ui.summary;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.CaloriesSnapshotMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.DistanceSnapshotMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.StepsSnapshotMeasurementRepository;
import pt.uninova.s4h.citizenhub.report.CanvasWriter;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SummaryDetailActivityFragment extends Fragment {

    private SummaryViewModel model;
    private ChartFunctions chartFunctions;
    private BarChart barChart;
    private TabLayout tabLayout;
    private TabLayout tabLayoutActivity;
    private TextView textViewLabel;
    private TextView textViewXLabel;
    private TextView textViewYLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
        chartFunctions = new ChartFunctions(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_detail_activity, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barChart = view.findViewById(R.id.bar_chart);

        textViewLabel = view.findViewById(R.id.tv_activity);
        textViewXLabel = view.findViewById(R.id.text_view_x_axis_label);
        textViewYLabel = view.findViewById(R.id.text_view_y_axis_label);

        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayoutActivity = view.findViewById(R.id.tab_layout_activity);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                if(pos == 0) {
                    textViewXLabel.setText(getString(R.string.summary_detail_time_hours));
                    barChart.highlightValue(null);
                    switch(tabLayoutActivity.getSelectedTabPosition()) {
                        case 0: dailySteps(); break;
                        case 1: dailyDistance(); break;
                        case 2: dailyCalories(); break;
                    }
                } else if(pos == 1) {
                    textViewXLabel.setText(getString(R.string.summary_detail_time_days));
                    barChart.highlightValue(null);
                    switch(tabLayoutActivity.getSelectedTabPosition()) {
                        case 0: weeklySteps(); break;
                        case 1: weeklyDistance(); break;
                        case 2: weeklyCalories(); break;
                    }
                } else if(pos == 2) {
                    textViewXLabel.setText(getString(R.string.summary_detail_time_days));
                    barChart.highlightValue(null);
                    switch(tabLayoutActivity.getSelectedTabPosition()) {
                        case 0: monthlySteps(); break;
                        case 1: monthlyDistance(); break;
                        case 2: monthlyCalories(); break;
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

        tabLayoutActivity.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                if(pos == 0) {
                    textViewLabel.setText(getString(R.string.summary_detail_activity_steps));
                    textViewYLabel.setText(getString(R.string.summary_detail_activity_steps));
                    barChart.highlightValue(null);
                    switch(tabLayout.getSelectedTabPosition()) {
                        case 0: dailySteps(); break;
                        case 1: weeklySteps(); break;
                        case 2: monthlySteps(); break;
                    }
                } else if(pos == 1) {
                    textViewLabel.setText(getString(R.string.summary_detail_activity_distance));
                    textViewYLabel.setText(getString(R.string.summary_detail_activity_distance_with_units));
                    barChart.highlightValue(null);
                    switch(tabLayout.getSelectedTabPosition()) {
                        case 0: dailyDistance(); break;
                        case 1: weeklyDistance(); break;
                        case 2: monthlyDistance(); break;
                    }
                } else if(pos == 2) {
                    textViewLabel.setText(getString(R.string.summary_detail_activity_calories));
                    textViewYLabel.setText(getString(R.string.summary_detail_activity_calories_with_units));
                    barChart.highlightValue(null);
                    switch(tabLayout.getSelectedTabPosition()) {
                        case 0: dailyCalories(); break;
                        case 1: weeklyCalories(); break;
                        case 2: monthlyCalories(); break;
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

        Button testButton = view.findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View chart = view.findViewById(R.id.bar_chart);
                /*Bitmap b = barChart.getChartBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();*/
                PdfDocument document = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(700, 842, 1).create();
                final PdfDocument.Page[] page = {document.startPage(pageInfo)};
                Canvas canvas = page[0].getCanvas();
                canvas.setDensity(72);
                chart.draw(canvas);
                final CanvasWriter canvasWriter = new CanvasWriter(canvas);
                canvasWriter.draw();
                document.finishPage(page[0]);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    document.writeTo(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte[] outByteArray = out.toByteArray();
                document.close();

                try {
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(path.toString(), "chart" + ".pdf");
                    OutputStream os = new FileOutputStream(file);
                    os.write(outByteArray);
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        chartFunctions.setupBarChart(barChart, model.getChartViewMarker());
        dailySteps();
    }

    private void dailySteps() {
        Observer<List<SummaryDetailUtil>> observer = data -> chartFunctions.setBarChartData(barChart, data, getString(R.string.summary_detail_activity_steps), 24);
        StepsSnapshotMeasurementRepository stepsSnapshotMeasurementRepository = new StepsSnapshotMeasurementRepository(getContext());
        stepsSnapshotMeasurementRepository.readLastDay(LocalDate.now(), observer);
    }

    private void weeklySteps() {
        Observer<List<SummaryDetailUtil>> observer = data -> chartFunctions.setBarChartData(barChart, data, getString(R.string.summary_detail_activity_steps), 7);
        StepsSnapshotMeasurementRepository stepsSnapshotMeasurementRepository = new StepsSnapshotMeasurementRepository(getContext());
        stepsSnapshotMeasurementRepository.readLastSevenDays(LocalDate.now(), observer);
    }

    private void monthlySteps() {
        Observer<List<SummaryDetailUtil>> observer = data -> chartFunctions.setBarChartData(barChart, data, getString(R.string.summary_detail_activity_steps), 30);
        StepsSnapshotMeasurementRepository stepsSnapshotMeasurementRepository = new StepsSnapshotMeasurementRepository(getContext());
        stepsSnapshotMeasurementRepository.readLastThirtyDays(LocalDate.now(), observer);
    }

    private void dailyDistance() {
        Observer<List<SummaryDetailUtil>> observer = data -> chartFunctions.setBarChartData(barChart, data, getString(R.string.summary_detail_activity_distance), 24);
        DistanceSnapshotMeasurementRepository distanceSnapshotMeasurementRepository = new DistanceSnapshotMeasurementRepository(getContext());
        distanceSnapshotMeasurementRepository.readLastDay(LocalDate.now(), observer);
    }

    private void weeklyDistance() {
        Observer<List<SummaryDetailUtil>> observer = data -> chartFunctions.setBarChartData(barChart, data, getString(R.string.summary_detail_activity_distance), 7);
        DistanceSnapshotMeasurementRepository distanceSnapshotMeasurementRepository = new DistanceSnapshotMeasurementRepository(getContext());
        distanceSnapshotMeasurementRepository.readLastSevenDays(LocalDate.now(), observer);
    }

    private void monthlyDistance(){
        Observer<List<SummaryDetailUtil>> observer = data -> chartFunctions.setBarChartData(barChart, data, getString(R.string.summary_detail_activity_distance), 30);
        DistanceSnapshotMeasurementRepository distanceSnapshotMeasurementRepository = new DistanceSnapshotMeasurementRepository(getContext());
        distanceSnapshotMeasurementRepository.readLastThirtyDays(LocalDate.now(), observer);
    }

    private void dailyCalories(){
        Observer<List<SummaryDetailUtil>> observer = data -> chartFunctions.setBarChartData(barChart, data, getString(R.string.summary_detail_activity_calories), 24);
        CaloriesSnapshotMeasurementRepository caloriesSnapshotMeasurementRepository = new CaloriesSnapshotMeasurementRepository(getContext());
        caloriesSnapshotMeasurementRepository.readLastDay(LocalDate.now(), observer);
    }

    private void weeklyCalories(){
        Observer<List<SummaryDetailUtil>> observer = data -> chartFunctions.setBarChartData(barChart, data, getString(R.string.summary_detail_activity_calories), 7);
        CaloriesSnapshotMeasurementRepository caloriesSnapshotMeasurementRepository = new CaloriesSnapshotMeasurementRepository(getContext());
        caloriesSnapshotMeasurementRepository.readLastSevenDays(LocalDate.now(), observer);
    }

    private void monthlyCalories(){
        Observer<List<SummaryDetailUtil>> observer = data -> chartFunctions.setBarChartData(barChart, data, getString(R.string.summary_detail_activity_calories), 30);
        CaloriesSnapshotMeasurementRepository caloriesSnapshotMeasurementRepository = new CaloriesSnapshotMeasurementRepository(getContext());
        caloriesSnapshotMeasurementRepository.readLastThirtyDays(LocalDate.now(), observer);
    }

}

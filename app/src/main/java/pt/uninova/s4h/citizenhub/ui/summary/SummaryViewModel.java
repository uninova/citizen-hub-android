package pt.uninova.s4h.citizenhub.ui.summary;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.entity.BloodPressureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.persistence.entity.util.LumbarExtensionTrainingSummary;
import pt.uninova.s4h.citizenhub.persistence.entity.util.PostureClassificationSum;
import pt.uninova.s4h.citizenhub.persistence.repository.BloodPressureMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.BreathingRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.CaloriesMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.DistanceMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.HeartRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.PostureMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.SampleRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.StepsMeasurementRepository;

public class SummaryViewModel extends AndroidViewModel {

    private final LiveData<List<BloodPressureMeasurementRecord>> dailyBloodPressureMeasurement;
    private final LiveData<Double> dailyBreathingRate;
    private final LiveData<Integer> dailyDataExistence;
    private final LiveData<Double> dailyHeartRate;
    private final LiveData<LumbarExtensionTrainingSummary> dailyLumbarExtensionTraining;
    private final LiveData<List<PostureClassificationSum>> dailyPostureMeasurement;
    private final LiveData<Integer> dailyStepsAllTypes;
    private final LiveData<Double> dailyDistanceAllTypes;
    private final LiveData<Double> dailyCaloriesAllTypes;

    public SummaryViewModel(Application application) {
        super(application);

        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(application);
        BreathingRateMeasurementRepository breathingRateMeasurementRepository = new BreathingRateMeasurementRepository(application);
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(application);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(application);
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(application);
        SampleRepository sampleRepository = new SampleRepository(application);
        StepsMeasurementRepository stepsMeasurementRepository = new StepsMeasurementRepository(application);
        DistanceMeasurementRepository distanceMeasurementRepository = new DistanceMeasurementRepository(application);
        CaloriesMeasurementRepository caloriesMeasurementRepository = new CaloriesMeasurementRepository(application);

        final LocalDate now = LocalDate.now();

        dailyLumbarExtensionTraining = lumbarExtensionTrainingRepository.readLatest(now);
        dailyBloodPressureMeasurement = bloodPressureMeasurementRepository.read(now);
        dailyBreathingRate = breathingRateMeasurementRepository.readAverage(now);
        dailyDataExistence = sampleRepository.readCount(now);
        dailyHeartRate = heartRateMeasurementRepository.readAverage(now);
        dailyPostureMeasurement = postureMeasurementRepository.readClassificationSum(now);
        dailyStepsAllTypes = stepsMeasurementRepository.getStepsAllTypes(now);
        dailyDistanceAllTypes = distanceMeasurementRepository.getDistanceAllTypes(now);
        dailyCaloriesAllTypes = caloriesMeasurementRepository.getCaloriesAllTypes(now);
    }

    public LiveData<LumbarExtensionTrainingSummary> getDailyLumbarExtensionTraining() {
        return dailyLumbarExtensionTraining;
    }

    public LiveData<List<BloodPressureMeasurementRecord>> getDailyBloodPressureMeasurement() {
        return dailyBloodPressureMeasurement;
    }

    public LiveData<Double> getDailyBreathingRateMeasurement() {
        return dailyBreathingRate;
    }

    public LiveData<Integer> getDailyDataExistence() {
        return dailyDataExistence;
    }

    public LiveData<Double> getDailyHeartRate() {
        return dailyHeartRate;
    }

    public LiveData<List<PostureClassificationSum>> getDailyPostureMeasurement() {
        return dailyPostureMeasurement;
    }

    public LiveData<Integer> getDailyStepsAllTypes() {return dailyStepsAllTypes;}

    public LiveData<Double> getDailyDistanceAllTypes(){return dailyDistanceAllTypes;}

    public LiveData<Double> getDailyCaloriesAllTypes(){return dailyCaloriesAllTypes;}

    public void setupBarChart(BarChart barChart) {
        barChart.setDrawGridBackground(false);
        barChart.setScaleEnabled(true);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setMarker(new MyMarkerView(getApplication().getBaseContext(), R.layout.fragment_summary_detail_line_chart_marker));

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setDrawGridLines(false);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    public void setupLineChart(LineChart lineChart) {
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setMarker(new MyMarkerView(getApplication().getBaseContext(), R.layout.fragment_summary_detail_line_chart_marker));

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setDrawGridLines(false);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    public void setupPieChart(PieChart pieChart) {
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
    }

    public String[] setLabels(int max) {
        String[] labels = new String[max + 1];
        int i = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, - max + 1);

        if (max == 24) {
            while(i < max) {
                labels[i] = String.valueOf(i);
                i++;
            }
        } else if (max == 7) {
            while (i < max) {
                labels[i] = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH).substring(0, 3);
                cal.add(Calendar.DATE, + 1);
                i++;
            }
        } else if (max == 30) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
            while (i < max) {
                labels[i] = sdf.format(cal.getTime());
                cal.add(Calendar.DATE, + 1);
                i++;
            }
        }

        return labels;
    }

    public void setBarChartData(List<SummaryDetailUtil> list, BarChart barChart, String label, int max) {
        List<BarEntry> entries = new ArrayList<>();
        int currentTime = 0;

        for (SummaryDetailUtil data : list) {
            while (currentTime < data.getTime()) {
                entries.add(new BarEntry(currentTime, -1));
                currentTime++;
            }
            entries.add(new BarEntry(data.getTime(), data.getValue1()));
            currentTime++;
        }

        while (currentTime < max) {
            entries.add(new BarEntry(currentTime, -1));
            currentTime++;
        }

        BarDataSet barDataSet = new BarDataSet(entries, label);
        barDataSet.setColor(getApplication().getColor(R.color.colorS4HLightBlue));
        barDataSet.setDrawValues(false);

        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        dataSet.add(barDataSet);

        BarData barData = new BarData(dataSet);
        barData.setValueFormatter(new MyValueFormatter());
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(setLabels(max)));
        barChart.getDescription().setText(label);
        barChart.invalidate();
    }

    public void setLineChartData(List<List<SummaryDetailUtil>> list, LineChart lineChart, String[] label, int max) {
        int color = 0;
        ArrayList<ILineDataSet> dataSet = new ArrayList<>();

        for (List<SummaryDetailUtil> data : list) {
            dataSet.add(setLineChartDataSet(data, label[color], max, color));
            color++;
        }

        LineData lineData = new LineData(dataSet);
        lineData.setValueFormatter(new MyValueFormatter());
        lineChart.setData(lineData);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(setLabels(max)));
        lineChart.invalidate();
    }

    public void setLineChartDataTest(List<SummaryDetailUtil> list, LineChart lineChart, String[] label, int max) {
        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();
        float time;

        for (SummaryDetailUtil data : list) {
            time = data.getTime();
            entries1.add(new BarEntry(time, data.getValue1()));
            if(data.getValue2() != null)
                entries2.add(new BarEntry(time, data.getValue2()));
            if(data.getValue3() != null)
                entries3.add(new BarEntry(time, data.getValue3()));
        }

        LineDataSet lineDataSet1 = getLineDataSet(entries1, label[0], 0);
        LineDataSet lineDataSet2 = getLineDataSet(entries2, label[1], 1);
        LineDataSet lineDataSet3 = getLineDataSet(entries3, label[2], 2);

        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
        dataSet.add(lineDataSet1);
        if (lineDataSet2 != null)
            dataSet.add(lineDataSet2);
        if (lineDataSet3 != null)
            dataSet.add(lineDataSet3);

        LineData lineData = new LineData(dataSet);
        lineData.setValueFormatter(new MyValueFormatter());
        lineChart.setData(lineData);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(setLabels(max)));
        lineChart.invalidate();
    }

    private LineDataSet getLineDataSet(List<Entry> entries, String label, int color){
        if (entries.size() < 1)
            return null;

        LineDataSet lineDataSet = new LineDataSet(entries, label);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(true);
        switch (color) {
            case 0:
                lineDataSet.setColor(getApplication().getColor(R.color.colorS4HLightBlue));
                lineDataSet.setCircleColor(getApplication().getColor(R.color.colorS4HLightBlue));
                lineDataSet.setCircleHoleColor(getApplication().getColor(R.color.colorS4HLightBlue));
                break;
            case 1:
                lineDataSet.setColor(getApplication().getColor(R.color.colorS4HOrange));
                lineDataSet.setCircleColor(getApplication().getColor(R.color.colorS4HOrange));
                lineDataSet.setCircleHoleColor(getApplication().getColor(R.color.colorS4HOrange));
                break;
            case 2:
                lineDataSet.setColor(getApplication().getColor(R.color.colorS4HTurquoise));
                lineDataSet.setCircleColor(getApplication().getColor(R.color.colorS4HTurquoise));
                lineDataSet.setCircleHoleColor(getApplication().getColor(R.color.colorS4HTurquoise));
                break;
        }
        return lineDataSet;
    }

    public LineDataSet setLineChartDataSet(List<SummaryDetailUtil> list, String label, int max, int color) {
        List<Entry> entries = new ArrayList<>();
        int currentTime = 0;

        for (SummaryDetailUtil data : list) {
            while (currentTime < data.getTime()) {
                //entries.add(new BarEntry(currentTime, 0));
                currentTime++;
            }
            entries.add(new BarEntry(data.getTime(), data.getValue1()));
            currentTime++;
        }

        while (currentTime < max) {
            //entries.add(new BarEntry(currentTime, 0));
            currentTime++;
        }

        LineDataSet lineDataSet = new LineDataSet(entries, label);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(true);
        switch (color) {
            case 0:
                lineDataSet.setColor(getApplication().getColor(R.color.colorS4HLightBlue));
                lineDataSet.setCircleColor(getApplication().getColor(R.color.colorS4HLightBlue));
                lineDataSet.setCircleHoleColor(getApplication().getColor(R.color.colorS4HLightBlue));
                break;
            case 1:
                lineDataSet.setColor(getApplication().getColor(R.color.colorS4HOrange));
                lineDataSet.setCircleColor(getApplication().getColor(R.color.colorS4HOrange));
                lineDataSet.setCircleHoleColor(getApplication().getColor(R.color.colorS4HOrange));
                break;
            case 2:
                lineDataSet.setColor(getApplication().getColor(R.color.colorS4HTurquoise));
                lineDataSet.setCircleColor(getApplication().getColor(R.color.colorS4HTurquoise));
                lineDataSet.setCircleHoleColor(getApplication().getColor(R.color.colorS4HTurquoise));
                break;
        }
        return lineDataSet;
    }

}

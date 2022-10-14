package pt.uninova.s4h.citizenhub.ui.summary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailBloodPressureUtil;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailHeartRateUtil;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;

public class ChartFunctions {

    private final Context context;

    public ChartFunctions(Context context) {
        this.context = context;
    }

    // This section has functions used to parse different utils to a more generic one used to pass the data to the charts //
    public List<SummaryDetailUtil> parseBloodPressureUtil(List<SummaryDetailBloodPressureUtil> bloodPressureUtils){
        List<SummaryDetailUtil> utils = new ArrayList<>();
        for (SummaryDetailBloodPressureUtil data : bloodPressureUtils){
            SummaryDetailUtil util = new SummaryDetailUtil();
            util.setValue1(data.getSystolic());
            util.setValue2(data.getDiastolic());
            util.setValue3(data.getMean());
            util.setTime(data.getTime());
            utils.add(util);
        }
        return utils;
    }

    public List<SummaryDetailUtil> parseHeartRateUtil(List<SummaryDetailHeartRateUtil> heartRateUtils){
        List<SummaryDetailUtil> utils = new ArrayList<>();
        for (SummaryDetailHeartRateUtil data : heartRateUtils){
            SummaryDetailUtil util = new SummaryDetailUtil();
            util.setValue1(data.getAverage());
            util.setValue2(data.getMaximum());
            util.setValue3(data.getMinimum());
            util.setTime(data.getTime());
            utils.add(util);
        }
        return utils;
    }
    //********************************************************************************************************************//

    // This section has functions used to define some characteristics of the different charts that cannot done in the layout //
    public void setupBarChart(BarChart barChart,  ChartMarkerView chartMarkerView) {
        barChart.setDrawGridBackground(false);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(true);
        barChart.getLegend().setEnabled(false);
        barChart.setMarker(chartMarkerView);

        Description desc = new Description();
        desc.setTextSize(9f);
        desc.setTextColor(Color.BLACK);
        desc.setPosition(60,15);
        barChart.setDescription(desc);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setDrawGridLines(false);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    public void setupLineChart(LineChart lineChart, ChartMarkerView chartMarkerView) {
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setMarker(chartMarkerView);

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
    //***********************************************************************************************************************//

    /* setLabels: used to get the labels for the charts
    * Parameters:
    * max (int) - it represents the maximum of the X axis
    * Return:
    * labels (String) - a string that contains the labels which will be displayed in the charts */
    public String[] setLabels(int max) {
        String[] labels = new String[max + 1];
        int i = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, - max + 1);

        if (max == 24) {
            while(i <= max) {
                labels[i] = String.valueOf(i);
                i++;
            }
        } else if (max == 7) {
            while (i < max) {
                labels[i] = Objects.requireNonNull(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)).substring(0, 3);
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
    // This sections has functions used to input data into the different charts //
    /* setBarChartData: used to plot the data on a bar chart
    * Parameters:
    * list (List<SummaryDetailUtil>) - the data retrieved from the queries
    * barChart (BarChart) - a barChart
    * label (String) - the label of the information to be displayed
    * max (int) - represents the maximum of the X axis */
    public void setBarChartData(BarChart barChart, List<SummaryDetailUtil> list, String label, int max) {
        List<BarEntry> entries = new ArrayList<>();
        int currentTime = 0;

        for (SummaryDetailUtil data : list) {
            while (currentTime < data.getTime()) {
                entries.add(new BarEntry(currentTime, 0));
                currentTime++;
            }
            entries.add(new BarEntry(data.getTime(), data.getValue1()));
            currentTime++;
        }

        while (currentTime < max) {
            entries.add(new BarEntry(currentTime, 0));
            currentTime++;
        }
        if(max == 24)
            entries.add(new BarEntry(currentTime, -1));

        BarDataSet barDataSet = new BarDataSet(entries, label);
        barDataSet.setColor(ContextCompat.getColor(context, R.color.colorS4HLightBlue));
        barDataSet.setDrawValues(false);

        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        dataSet.add(barDataSet);

        BarData barData = new BarData(dataSet);
        barData.setValueFormatter(new ChartValueFormatter());
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(setLabels(max)));
        barChart.getDescription().setText(label);
        barChart.invalidate();
    }

    private void setStackedBar(BarChart barChart, List<SummaryDetailUtil> list1, List<SummaryDetailUtil> list2, String[] labels, int max){
        float[] values1 = new float[max];
        float[] values2 = new float[max];

        List<BarEntry> entries = new ArrayList<>();

        for (SummaryDetailUtil data : list1) {
            values1[Math.round(data.getTime())] = data.getValue1();
        }

        for (SummaryDetailUtil data : list2) {
            values2[Math.round(data.getTime())] = data.getValue1();
        }

        for (int i = 0; i < max; i++){
            entries.add(new BarEntry(i, new float[]{values1[i], values2[i]}));
        }

        BarDataSet barDataSet = new BarDataSet(entries, null);
        barDataSet.setColors(ContextCompat.getColor(context, R.color.colorS4HLightBlue), ContextCompat.getColor(context, R.color.colorS4HOrange));
        barDataSet.setStackLabels(labels);

        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        dataSet.add(barDataSet);

        BarData barData = new BarData(dataSet);
        barData.setValueFormatter(new ChartValueFormatter());

        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(setLabels(max)));
        //barChart.groupBars(0.2f, 0.25f, 0.05f);
        barChart.invalidate();
    }

    public void setLineChartData(LineChart lineChart, List<SummaryDetailUtil> list, String[] label, int max) {
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
        lineData.setValueFormatter(new ChartValueFormatter());
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
                lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorS4HLightBlue));
                lineDataSet.setCircleColor(ContextCompat.getColor(context, R.color.colorS4HLightBlue));
                lineDataSet.setCircleHoleColor(ContextCompat.getColor(context, R.color.colorS4HLightBlue));
                break;
            case 1:
                lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorS4HOrange));
                lineDataSet.setCircleColor(ContextCompat.getColor(context, R.color.colorS4HOrange));
                lineDataSet.setCircleHoleColor(ContextCompat.getColor(context, R.color.colorS4HOrange));
                break;
            case 2:
                lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorS4HTurquoise));
                lineDataSet.setCircleColor(ContextCompat.getColor(context, R.color.colorS4HTurquoise));
                lineDataSet.setCircleHoleColor(ContextCompat.getColor(context, R.color.colorS4HTurquoise));
                break;
        }
        return lineDataSet;
    }

    public void setAreaChart(LineChart lineChart, List<SummaryDetailUtil> list1, List<SummaryDetailUtil> list2, String[] labels, int max){
        float[] values1 = new float[max];
        float[] values2 = new float[max];
        float[] offset = new float[max];

        for (SummaryDetailUtil data : list1) {
            values1[Math.round(data.getTime())] = data.getValue1();
            offset[Math.round(data.getTime())] = data.getValue1();
        }

        for (SummaryDetailUtil data : list2) {
            values2[Math.round(data.getTime())] = data.getValue1() + offset[Math.round(data.getTime())];
        }

        int currentTime = 0;
        float total;
        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();

        while(currentTime < max){
            total = values2[currentTime] + values1[currentTime];
            if(total > 3600000){
                values1[currentTime] = values1[currentTime] * 3600000 / total;
                values2[currentTime] = values2[currentTime] * 3600000 / total;
            }
            entries1.add(new BarEntry(currentTime, values1[currentTime] * 100 / 3600000));
            entries2.add(new BarEntry(currentTime, values2[currentTime] * 100 / 3600000));
            currentTime++;
        }

        LineDataSet lineDataSet1 = setLineDataSet(entries1, labels[0], ContextCompat.getColor(context, R.color.colorS4HOrange));
        LineDataSet lineDataSet2 = setLineDataSet(entries2, labels[1], ContextCompat.getColor(context, R.color.colorS4HLightBlue));

        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
        dataSet.add(lineDataSet2);
        dataSet.add(lineDataSet1);

        LineData lineData = new LineData(dataSet);
        lineData.setValueFormatter(new ChartValueFormatter());

        lineChart.setData(lineData);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(setLabels(max)));
        lineChart.invalidate();
    }

    private LineDataSet setLineDataSet(List<Entry> entries, String label, int color){
        LineDataSet lineDataSet = new LineDataSet(entries, label);
        lineDataSet.setColor(color);
        lineDataSet.setFillColor(color);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(255);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return lineDataSet;
    }

    public void setPieChart(PieChart pieChart, List<SummaryDetailUtil> list1, List<SummaryDetailUtil> list2){
        int value1 = 0;
        int value2 = 0;
        for (SummaryDetailUtil data : list1)
            value1 += data.getValue1();
        for (SummaryDetailUtil data : list2)
            value2 += data.getValue1();

        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(value1, secondsToString(value1 / 1000)));
        pieEntries.add(new PieEntry(value2, secondsToString(value2 / 1000)));
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(ContextCompat.getColor(context, R.color.colorS4HLightBlue), ContextCompat.getColor(context, R.color.colorS4HOrange));
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private String secondsToString(long value) {
        long seconds = value;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        if (minutes > 0)
            seconds = seconds % 60;

        if (hours > 0) {
            minutes = minutes % 60;
        }

        String result = ((hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m " : "") + (seconds > 0 ? seconds + "s" : "")).trim();

        return result.equals("") ? "0s" : result;
    }

    /* public void setLineChartData(List<List<SummaryDetailUtil>> list, LineChart lineChart, String[] label, int max) {
        int color = 0;
        ArrayList<ILineDataSet> dataSet = new ArrayList<>();

        for (List<SummaryDetailUtil> data : list) {
            dataSet.add(setLineChartDataSet(data, label[color], max, color));
            color++;
        }

        LineData lineData = new LineData(dataSet);
        lineData.setValueFormatter(new ChartValueFormatter());
        lineChart.setData(lineData);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(setLabels(max)));
        lineChart.invalidate();
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
    }*/

}

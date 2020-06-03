package pt.uninova.s4h.citizenhub.ui.reports;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.CircularGauge;
import com.anychart.charts.Pie;
import com.anychart.core.axes.Circular;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.gauge.pointers.Bar;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.graphics.vector.text.HAlign;
import com.anychart.graphics.vector.text.VAlign;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.json.JSONException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAccessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import datastorage.DatabaseHelperInterface;
import datastorage.MeasurementsDbHelper;
import datastorage.pdf;
import pt.uninova.s4h.citizenhub.ui.BackgroundService;
import pt.uninova.s4h.citizenhub.ui.Home;
import pt.uninova.s4h.citizenhub.ui.R;
import pt.uninova.s4h.citizenhub.ui.login.SignUpActivity;
import pt.uninova.s4h.citizenhub.ui.posture.PostureFragment;

public class ReportsFragment extends Fragment {

    private ReportsViewModel galleryViewModel;
    public MaterialCalendarView calendarView;
    View root;
    Button reportPDF;
    int standing_minutes = 0;
    int seated_good_minutes = 0;
    int seated_bad_minutes = 0;
    int steps_objective = 10000;
    int counter = 3;
    int hours_sitting = 0, minutes_sitting = 0, hours_goodPosture = 0, minutes_goodPosture = 0;
    int hours_standing = 0, minutes_standing = 0;
    int minutes_badPosture = 0, hours_badPosture = 0;
    private BarChart mChart;
    public static CalendarDay daySelected;
    String[] months = {"January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"}; //To force my formatting, will probably change later

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(ReportsViewModel.class);
        root = inflater.inflate(R.layout.fragment_reports, container, false);
        final TextView textView = root.findViewById(R.id.textView_selectday);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Home.fab.hide();

        //Calendar: Set Current Day
        calendarView = root.findViewById(R.id.calendar);
        LocalDate date = LocalDate.now();
        calendarView.setSelectedDate(date);
        List<CalendarDay> today = new ArrayList<CalendarDay>();
        CalendarDay dayCurrent = CalendarDay.from(date);
        today.add(dayCurrent);

        //reportPDF = root.findViewById(R.id.button_pdf);

        //Calendar: Set Days With Activity
        final List<CalendarDay> daysWithActivity;
        daysWithActivity = (List<CalendarDay>) getDaysWithActivity();
        setDaysWithActivity(daysWithActivity);
        calendarView.addDecorator(new EventDecorator(Color.GREEN, today));
        /*reportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPDF();
            }
        });*/
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                if(daysWithActivity.contains(calendarDay)) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View root2 = inflater.inflate(R.layout.fragment_reports_dayselected, null);
                    ViewGroup rootView = (ViewGroup) getView();
                    rootView.removeAllViews();
                    rootView.addView(root2);

                    Button pdf = root2.findViewById(R.id.button_pdf);
                    pdf.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            standing_minutes = 0;
                            seated_good_minutes = 0;
                            seated_bad_minutes = 0;
                            hours_sitting = 0;
                            minutes_sitting = 0;
                            hours_goodPosture = 0;
                            minutes_goodPosture = 0;
                            hours_standing = 0;
                            minutes_standing = 0;
                            minutes_badPosture = 0;
                            hours_badPosture = 0;
                            goPDF();
                        }
                    });

                    TextView dayTitle = root.findViewById(R.id.textView_DayTitle);
                    daySelected = calendarDay;
                    String date = daySelected.toString();
                    String day = date.substring(date.lastIndexOf("}")-2,date.lastIndexOf("}"));
                    String month = date.substring(date.indexOf("-")+1, date.lastIndexOf("-"));
                    String month_name = months[Integer.parseInt(month)-1];
                    String year = date.substring(date.indexOf("{")+1, date.indexOf("{")+5);
                    Log.i("CR", "Selected Day: " + day + " " + month_name + " " + year);
                    String day_for_title;
                    if(day.contains("-"))
                        day_for_title = day.substring(1,2);
                    else
                        day_for_title = day;

                    dayTitle.setText("Selected Day: " + day_for_title + " " + month_name + " " + year);

                    drawCheese(calendarDay, root2); //posture and body position
                    drawGauge(calendarDay, steps_objective, root2); //steps
                    drawColumn(calendarDay, root2); //heart rate

                    /*
                    daySelected = calendarDay;
                    Intent intent = new Intent(getActivity(), DaySelectedActivity.class);
                    startActivity(intent);*/
                }
                else {
                    Toast.makeText(getContext(),"This day does not have activity.", Toast.LENGTH_LONG);
                }
            }
        });
        return root;
}

    public void goPDF (){
        int steps = 0, calories = 0, heartRateAvg = 0;
        int min_HR = 0, max_HR = 0;
        float distance = 0;
        int counter_heartRateAvg = 0;


        int counter = 0;
        int aux_minutes_before = 0;
        int aux_minutes_now = 0;
        int before = -1;
        int now = -1;

        String timestamp_minHR = " ";
        String timestamp_maxHR = " ";

        //set current day
        LocalDate dayCurrent = LocalDate.now();
        CalendarDay currentDay = CalendarDay.from(dayCurrent);

        //getValues
        //sitting time and good posture TODO

        //steps, calories, distance
        SQLiteOpenHelper databaseHelperInterface = new DatabaseHelperInterface(getContext());
        Cursor cursor = ViewData(databaseHelperInterface);
        if(cursor.getCount()==0) {
            Log.i("Home_SetValues", "No info from steps.");
        }
        else {
            while (cursor.moveToNext()){
                Log.i("Home_SetValues", "Valores da tabela:" + cursor.getString(2)
                        + cursor.getString(3)
                        + cursor.getString(4));
                if (cursor.getString(4).equals("Steps"))
                {
                    String timestamp = cursor.getString(2);
                    int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                    String month = timestamp.substring(4, 7);
                    DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                            .withLocale(Locale.ENGLISH);
                    TemporalAccessor accessor = parser.parse(month);
                    int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                    int day = Integer.parseInt(timestamp.substring(8,10));
                    int value = Integer.parseInt(cursor.getString(3));
                    CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                    Log.i("Home_SetValues", dayExample.toString() + " " + currentDay.toString());
                    if (dayExample.equals(currentDay) && value > steps)
                    {
                        steps = value;
                    }
                }
                else if (cursor.getString(4).equals("Calories"))
                {
                    String timestamp = cursor.getString(2);
                    int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                    String month = timestamp.substring(4, 7);
                    DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                            .withLocale(Locale.ENGLISH);
                    TemporalAccessor accessor = parser.parse(month);
                    int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                    int day = Integer.parseInt(timestamp.substring(8,10));
                    int value = Integer.parseInt(cursor.getString(3));
                    CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                    Log.i("Home_SetValues", dayExample.toString() + " " + currentDay.toString());
                    if (dayExample.equals(currentDay) && value > calories)
                    {
                        calories = value;
                    }
                }
                else if (cursor.getString(4).equals("Distance"))
                {
                    String timestamp = cursor.getString(2);
                    int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                    String month = timestamp.substring(4, 7);
                    DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                            .withLocale(Locale.ENGLISH);
                    TemporalAccessor accessor = parser.parse(month);
                    int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                    int day = Integer.parseInt(timestamp.substring(8,10));
                    int value = Integer.parseInt(cursor.getString(3));
                    CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                    Log.i("Home_SetValues", dayExample.toString() + " " + currentDay.toString());
                    if (dayExample.equals(currentDay) && value > distance)
                    {
                        distance = value;
                    }
                }
                else if (cursor.getString(4).equals("HeartRate"))
                {
                    String timestamp = cursor.getString(2);
                    int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                    String month = timestamp.substring(4, 7);
                    DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                            .withLocale(Locale.ENGLISH);
                    TemporalAccessor accessor = parser.parse(month);
                    int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                    int day = Integer.parseInt(timestamp.substring(8,10));
                    int value = Integer.parseInt(cursor.getString(3));
                    CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                    Log.i("Home_SetValues", dayExample.toString() + " " + currentDay.toString());
                    if (dayExample.equals(currentDay))
                    {
                        heartRateAvg += value;
                        counter_heartRateAvg++;
                        if (min_HR == 0 || max_HR == 0) {
                            min_HR = value;
                            max_HR = value;
                            timestamp_maxHR = timestamp;
                            timestamp_minHR = timestamp;
                        }
                        if (value < min_HR && value > 0) {
                            min_HR = value;timestamp_minHR = timestamp;
                        }
                        if (value > max_HR){
                            max_HR = value;timestamp_maxHR = timestamp;
                        }
                    }
                }
                else if (cursor.getString(4).equals("BackPosture"))
                {
                    Log.i("Reports_drawCheese", "GOT HERE3");
                    String timestamp = cursor.getString(2);
                    int year = Integer.parseInt(timestamp.substring(timestamp.length() - 4));
                    String month = timestamp.substring(4, 7);
                    DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                            .withLocale(Locale.ENGLISH);
                    TemporalAccessor accessor = parser.parse(month);
                    int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                    int day = Integer.parseInt(timestamp.substring(8, 10));
                    int value = Integer.parseInt(cursor.getString(3));
                    int hours = Integer.parseInt(timestamp.substring(11, 13));
                    int minute = Integer.parseInt(timestamp.substring(14, 16));
                    int minutes = hours * 60 + minute;
                    CalendarDay dayExample = CalendarDay.from(year, month_number, day);
                    Log.i("CRA", dayExample.toString() + " " + currentDay.toString());
                    if (dayExample.equals(currentDay)) {
                        if (counter == 0) {
                            //Log.i("debug_positions",line);
                            aux_minutes_before = minutes;
                            before = value;
                            counter++;
                            continue;
                        }
                        counter++;
                        aux_minutes_now = minutes;
                        now = value;

                        //Log.i("debug_positions", String.valueOf(aux_minutes_before));
                        //Log.i("debug_positions", String.valueOf(aux_minutes_now));
                        //Log.i("debug_positions", before);
                        //Log.i("debug_positions", now);

                        count_minutes("BackPosture", before, now,
                                aux_minutes_before, aux_minutes_now);

                        before = now;
                        aux_minutes_before = aux_minutes_now;
                    }
                }
                else if (cursor.getString(4).equals("BodyPosture")) {
                    Log.i("Reports_drawCheese", "GOT HERE4");
                    String timestamp = cursor.getString(2);
                    int year = Integer.parseInt(timestamp.substring(timestamp.length() - 4));
                    String month = timestamp.substring(4, 7);
                    DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                            .withLocale(Locale.ENGLISH);
                    TemporalAccessor accessor = parser.parse(month);
                    int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                    int day = Integer.parseInt(timestamp.substring(8, 10));
                    int value = Integer.parseInt(cursor.getString(3));
                    int hours = Integer.parseInt(timestamp.substring(11, 13));
                    int minute = Integer.parseInt(timestamp.substring(14, 16));
                    int minutes = hours * 60 + minute;
                    CalendarDay dayExample = CalendarDay.from(year, month_number, day);
                    Log.i("CRA", dayExample.toString() + " " + currentDay.toString());
                    if (dayExample.equals(currentDay)) {
                        if (counter == 0) {
                            aux_minutes_before = minutes;
                            before = value;
                            counter++;
                            continue;
                        }
                        counter++;
                        aux_minutes_now = minutes;
                        now = value;

                        //Log.i("debug_positions", String.valueOf(aux_minutes_before));
                        //Log.i("debug_positions", String.valueOf(aux_minutes_now));
                        //Log.i("debug_positions", before);
                        //Log.i("debug_positions", now);

                        count_minutes("BodyPosture", before, now,
                                aux_minutes_before, aux_minutes_now);

                        Log.i("AQUIII", " " + seated_bad_minutes + seated_bad_minutes + minutes_standing);
                        before = now;
                        aux_minutes_before = aux_minutes_now;
                    }
                }
                else
                {
                    Log.i("Reports_drawGauge","characteristic not identified");
                }
            }
            if (counter_heartRateAvg > 0)
                heartRateAvg = heartRateAvg / counter_heartRateAvg;
            distance = distance / 1000;
            Log.i("AQUIII", " " +  + standing_minutes);
        }

        //final values
        int final_hours_goodPosture = seated_good_minutes / 60;
        int final_minutes_goodPosture = seated_good_minutes % 60;
        int final_hours_sitting = (seated_good_minutes + seated_bad_minutes) / 60;
        int final_minutes_sitting = (seated_good_minutes + seated_bad_minutes) % 60;


        timestamp_maxHR = timestamp_maxHR.substring(11, timestamp_maxHR.length()-5);

        timestamp_minHR = timestamp_minHR.substring(11, timestamp_minHR.length()-5);

        pdf PDF = new pdf(heartRateAvg, steps, (int)distance, calories,final_hours_sitting,
                final_minutes_sitting,final_hours_goodPosture,final_minutes_goodPosture,
                Home.loggedEmail, 26, 10000,
                minutes_standing / 60, minutes_standing % 60,
                min_HR,max_HR, timestamp_minHR, timestamp_maxHR);
        PDF.createPDF();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    private void setData (int count)
    {
        ArrayList<BarEntry> yVals = new ArrayList<>();

        for (int i = 0; i < count; i++)
        {
            float value = (float) Math.random()*100;
            yVals.add(new BarEntry(i,(int) value));
        }

        BarDataSet set = new BarDataSet(yVals, "Data Set");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);

        BarData data = new BarData(set);

        mChart.setData(data);
        mChart.invalidate();
        mChart.animateY(500);
    }

    public Cursor ViewData(SQLiteOpenHelper databaseHelperInterface){
        SQLiteDatabase db = databaseHelperInterface.getReadableDatabase();
        String query = "Select * from measurements";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public List<CalendarDay>  getDaysWithActivity()
    {
        List<CalendarDay> calendardays = new ArrayList<CalendarDay>();
        //CalendarDay dayExample = CalendarDay.from(2020,4,5); //dummyday
        //calendardays.add(dayExample);
        //TODO getstuff from the tables
        SQLiteOpenHelper databaseHelperInterface = new DatabaseHelperInterface(getContext());
        Cursor cursor = ViewData(databaseHelperInterface);
        if(cursor.getCount()==0) {
            Log.i("CR", "No devices to show");
        }
        else {
                while (cursor.moveToNext()){
                    Log.i("CR", "Valores da tabela:" + cursor.getString(2) + cursor.getString(3));
                    String timestamp = cursor.getString(2);
                    int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                    String month = timestamp.substring(4, 7);
                    DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                            .withLocale(Locale.ENGLISH);
                    TemporalAccessor accessor = parser.parse(month);
                    int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                    int day = Integer.parseInt(timestamp.substring(8,10));
                    CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                    calendardays.add(dayExample);
                }
            }
        return calendardays;
    }

    public void setDaysWithActivity (List<CalendarDay> calendarDays){
        calendarView.addDecorator(new EventDecorator(Color.BLUE, calendarDays));
    }

    public void drawGauge(CalendarDay Day, int steps_objective, View root)
    {
        //anychart start
        AnyChartView anyChartView = root.findViewById(R.id.any_chart_view2);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        String objective = String.valueOf(steps_objective);
        int steps = 0;

        if (counter == -1)
        {
            //dummy data for testing
            steps = 0;
        }
        else if (counter == 0)
        {
            //if day is empty
            steps = 0;
        }
        else
        {
            //if day has values
            SQLiteOpenHelper databaseHelperInterface = new DatabaseHelperInterface(getContext());
            Cursor cursor = ViewData(databaseHelperInterface);
            if(cursor.getCount()==0) {
                Log.i("Reports_drawGauge", "No devices to show");
            }
            else {
                while (cursor.moveToNext()){
                    Log.i("Reports_drawGauge", "Valores da tabela:" + cursor.getString(2)
                            + cursor.getString(3)
                            + cursor.getString(4));
                    if (cursor.getString(4).equals("Steps"))
                    {
                        String timestamp = cursor.getString(2);
                        int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                        String month = timestamp.substring(4, 7);
                        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                                .withLocale(Locale.ENGLISH);
                        TemporalAccessor accessor = parser.parse(month);
                        int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                        int day = Integer.parseInt(timestamp.substring(8,10));
                        int value = Integer.parseInt(cursor.getString(3));
                        CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                        Log.i("Reports_drawGauge", dayExample.toString() + " " + Day.toString());
                        if (dayExample.equals(Day) && value > steps)
                        {
                            steps = value;
                        }
                    }
                    else
                    {
                        Log.i("Reports_drawGauge","not steps");
                    }
                }
            }
        }
        String percentage_done;
        if (objective==null)
            objective = "0";

        if (objective=="")
            objective = "0";

        if (Integer.valueOf(objective) == 0){
            percentage_done = String.valueOf(steps*100/1);
        }
        else{
            percentage_done = String.valueOf(steps*100/(Integer.valueOf(objective)));
        }

        //Log.i("debug_dailyreport", percentage_done);

        CircularGauge circularGauge = AnyChart.circular();
        //circularGauge.data(new SingleValueDataSet(new String[] { "23", "34", "67", "93", "56", "100"}));
        circularGauge.data(new SingleValueDataSet(new String[] { "100", percentage_done, "100"}));
        circularGauge.fill("#fff")
                .stroke(null)
                .padding(0d, 0d, 0d, 0d)
                .margin(100d, 100d, 100d, 100d);
        circularGauge.startAngle(0d);
        circularGauge.sweepAngle(270d);

        Circular xAxis = circularGauge.axis(0)
                .radius(100d)
                .width(1d)
                .fill((Fill) null);
        xAxis.scale()
                .minimum(0d)
                .maximum(100d);
        xAxis.ticks("{ interval: 1 }")
                .minorTicks("{ interval: 1 }");
        xAxis.labels().enabled(false);
        xAxis.ticks().enabled(false);
        xAxis.minorTicks().enabled(false);

        circularGauge.label(0d)
                .text("Objective <span style=\"\">[" + objective + "]</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE);
        circularGauge.label(0d)
                .anchor(Anchor.RIGHT_CENTER)
                .padding(0d, 10d, 0d, 0d)
                .height(17d / 1d + "%")
                .offsetY(100d + "%")
                .offsetX(0d);
        Bar bar0 = circularGauge.bar(0d);
        bar0.dataIndex(0d);
        bar0.radius(100d);
        bar0.width(17d);
        bar0.fill(new SolidFill("#64b5f6", 1d));
        bar0.stroke(null);
        bar0.zIndex(5d);
        Bar bar100 = circularGauge.bar(100d);
        bar100.dataIndex(5d);
        bar100.radius(100d);
        bar100.width(17d);
        bar100.fill(new SolidFill("#F5F4F4", 1d));
        bar100.stroke("1 #e5e4e4");
        bar100.zIndex(4d);

        circularGauge.label(1d)
                .text("Done <span style=\"\">[" + String.valueOf(steps) + "]</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE);
        circularGauge.label(1d)
                .anchor(Anchor.RIGHT_CENTER)
                .padding(0d, 10d, 0d, 0d)
                .height(17d / 1d + "%")
                .offsetY(70d + "%")
                .offsetX(0d);
        Bar bar1 = circularGauge.bar(1d);
        bar1.dataIndex(1d);
        bar1.radius(70d);
        bar1.width(25d);
        bar1.fill(new SolidFill("#1976d2", 1d));
        bar1.stroke(null);
        bar1.zIndex(5d);
        Bar bar101 = circularGauge.bar(101d);
        bar101.dataIndex(5d);
        bar101.radius(80d);
        bar101.width(17d);
        bar101.fill(new SolidFill("#F5F4F4", 1d));
        bar101.stroke("1 #e5e4e4");
        bar101.zIndex(4d);

        circularGauge.margin(50d, 50d, 50d, 50d);
        circularGauge.title()
                .text("Steps");
        //       .useHtml(true);
        circularGauge.title().enabled(true);
        circularGauge.title().hAlign(HAlign.CENTER);
        circularGauge.title()
                .padding(0d, 0d, 0d, 0d)
                .margin(0d, 0d, 20d, 0d);

        anyChartView.setChart(circularGauge);
        //anychart end
    }

    public void drawColumn(CalendarDay Day, View root)
    {
        //anychart start
        AnyChartView anyChartView = root.findViewById(R.id.any_chart_view3);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<DataEntry>();
        counter = 1; //increment this for the number of stuff..
        //file read
        if (counter == -1)
        {
            //dummy data for testing
            data = columnDummyData();
        }
        else if (counter == 0)
        {
            //if day is empty
            data.add(new ValueDataEntry("0:00", 0));
        }
        else
        {
            //if day has values
            SQLiteOpenHelper databaseHelperInterface = new DatabaseHelperInterface(getContext());
            Cursor cursor = ViewData(databaseHelperInterface);
            if(cursor.getCount()==0) {
                Log.i("Reports_drawColumn", "No devices to show");
                data.add(new ValueDataEntry("0:00", 0));
            }
            else {
                while (cursor.moveToNext()){
                    Log.i("Reports_drawColumn", "Valores da tabela:" + cursor.getString(2)
                            + cursor.getString(3)
                            + cursor.getString(4));
                    if (cursor.getString(4).equals("HeartRate"))
                    {
                        String timestamp = cursor.getString(2);
                        int year = Integer.parseInt(timestamp.substring(timestamp.length()-4));
                        String month = timestamp.substring(4, 7);
                        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                                .withLocale(Locale.ENGLISH);
                        TemporalAccessor accessor = parser.parse(month);
                        int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                        int day = Integer.parseInt(timestamp.substring(8,10));
                        int value = Integer.parseInt(cursor.getString(3));
                        String hours = timestamp.substring(11,16);
                        CalendarDay dayExample = CalendarDay.from(year,month_number,day);
                        Log.i("CRA", dayExample.toString() + " " + Day.toString());
                        if (dayExample.equals(Day))
                        {
                            data.add(new ValueDataEntry(hours, value));
                        }
                    }
                    else
                    {
                        Log.i("Reports_drawColumn","not heart rate");
                    }
                }
            }
        }
        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        cartesian.animation(true);
        cartesian.title("Heart Rate");
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Time of the Day");
        cartesian.yAxis(0).title("bpm");
        anyChartView.setChart(cartesian);
        //anychart end
    }

    public List<DataEntry> columnDummyData()
    {
        List<DataEntry> data = new ArrayList<DataEntry>();
        data.add(new ValueDataEntry("0:00", 64));
        data.add(new ValueDataEntry("0:01", 57));
        data.add(new ValueDataEntry("0:02", 78));
        data.add(new ValueDataEntry("0:03", 79));
        data.add(new ValueDataEntry("0:04", 78));
        data.add(new ValueDataEntry("0:05", 67));
        data.add(new ValueDataEntry("0:06", 65));
        data.add(new ValueDataEntry("0:07", 45));
        data.add(new ValueDataEntry("0:08", 65));
        data.add(new ValueDataEntry("0:09", 75));
        data.add(new ValueDataEntry("0:10", 85));
        data.add(new ValueDataEntry("0:11", 100));
        data.add(new ValueDataEntry("0:12", 55));
        data.add(new ValueDataEntry("0:13", 47));
        data.add(new ValueDataEntry("0:14", 99));
        data.add(new ValueDataEntry("0:15", 0));
        data.add(new ValueDataEntry("0:16", 67));
        data.add(new ValueDataEntry("0:17", 55));
        data.add(new ValueDataEntry("0:18", 120));
        data.add(new ValueDataEntry("0:19", 22));
        data.add(new ValueDataEntry("0:20", 67));
        return data;
    }

    public void drawCheese(CalendarDay Day, View root) {
        //anychart start
        AnyChartView anyChartView = root.findViewById(R.id.any_chart_view);

        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        int counter = 0;
        int aux_minutes_before = 0;
        int aux_minutes_now = 0;
        int before = -1;
        int now = -1;

        Log.i("Reports_drawCheese","GOT HERE1");
        //if day has values
        SQLiteOpenHelper databaseHelperInterface = new DatabaseHelperInterface(getContext());
        Cursor cursor = ViewData(databaseHelperInterface);
        if (cursor.getCount() == 0) {
            Log.i("Reports_drawCheese", "Nothing to show");

        } else {
            while (cursor.moveToNext()) {
                Log.i("Reports_drawCheese", "Valores da tabela:" + cursor.getString(2)
                        + cursor.getString(3)
                        + cursor.getString(4));
                Log.i("Reports_drawCheese","GOT HERE2");
                if (cursor.getString(4).equals("BackPosture")) {
                    Log.i("Reports_drawCheese","GOT HERE3");
                    String timestamp = cursor.getString(2);
                    int year = Integer.parseInt(timestamp.substring(timestamp.length() - 4));
                    String month = timestamp.substring(4, 7);
                    DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                            .withLocale(Locale.ENGLISH);
                    TemporalAccessor accessor = parser.parse(month);
                    int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                    int day = Integer.parseInt(timestamp.substring(8, 10));
                    int value = Integer.parseInt(cursor.getString(3));
                    int hours = Integer.parseInt(timestamp.substring(11, 13));
                    int minute = Integer.parseInt(timestamp.substring(14, 16));
                    int minutes = hours * 60 + minute;
                    CalendarDay dayExample = CalendarDay.from(year, month_number, day);
                    Log.i("CRA", dayExample.toString() + " " + Day.toString());
                    if (dayExample.equals(Day)) {
                        if (counter == 0) {
                            //Log.i("debug_positions",line);
                            aux_minutes_before = minutes;
                            before = value;
                            counter++;
                            continue;
                        }
                        counter++;
                        aux_minutes_now = minutes;
                        now = value;

                        //Log.i("debug_positions", String.valueOf(aux_minutes_before));
                        //Log.i("debug_positions", String.valueOf(aux_minutes_now));
                        //Log.i("debug_positions", before);
                        //Log.i("debug_positions", now);

                        count_minutes("BackPosture", before, now,
                                aux_minutes_before, aux_minutes_now);

                        before = now;
                        aux_minutes_before = aux_minutes_now;
                    }
                } else if (cursor.getString(4).equals("BodyPosture")) {
                    Log.i("Reports_drawCheese","GOT HERE4");
                    String timestamp = cursor.getString(2);
                    int year = Integer.parseInt(timestamp.substring(timestamp.length() - 4));
                    String month = timestamp.substring(4, 7);
                    DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                            .withLocale(Locale.ENGLISH);
                    TemporalAccessor accessor = parser.parse(month);
                    int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                    int day = Integer.parseInt(timestamp.substring(8, 10));
                    int value = Integer.parseInt(cursor.getString(3));
                    int hours = Integer.parseInt(timestamp.substring(11, 13));
                    int minute = Integer.parseInt(timestamp.substring(14, 16));
                    int minutes = hours * 60 + minute;
                    CalendarDay dayExample = CalendarDay.from(year, month_number, day);
                    Log.i("CRA", dayExample.toString() + " " + Day.toString());
                    if (dayExample.equals(Day)) {
                        if (counter == 0) {
                            aux_minutes_before = minutes;
                            before = value;
                            counter++;
                            continue;
                        }
                        counter++;
                        aux_minutes_now = minutes;
                        now = value;

                        //Log.i("debug_positions", String.valueOf(aux_minutes_before));
                        //Log.i("debug_positions", String.valueOf(aux_minutes_now));
                        //Log.i("debug_positions", before);
                        //Log.i("debug_positions", now);

                        count_minutes("BodyPosture", before, now,
                                aux_minutes_before, aux_minutes_now);

                        before = now;
                        aux_minutes_before = aux_minutes_now;
                    }
                } else {
                    Log.i("Reports_drawCheese","GOT HERE5");
                    Log.i("Reports_drawCheese", "not BackPosture nor BodyPosture");
                }
            }
        }
        if (counter == 0)
        {
            data.add(new ValueDataEntry("Standing", 0));
            data.add(new ValueDataEntry("Good Posture", 0));
            data.add(new ValueDataEntry("Bad Posture", 0));
            data.add(new ValueDataEntry("Other", 24));
        }
        else{
            data.add(new ValueDataEntry("Standing", standing_minutes));
            data.add(new ValueDataEntry("Sitting w/ Good Posture", seated_good_minutes));
            data.add(new ValueDataEntry("Sitting w/ Bad Posture", seated_bad_minutes));
            //data.add(new ValueDataEntry("Other", 24*60-((standing_minutes+seated_bad_minutes+seated_good_minutes))));
            //Log.i("debug_positions", String.valueOf(standing_minutes));
            //Log.i("debug_positions", String.valueOf(seated_good_minutes));
            //Log.i("debug_positions", String.valueOf(seated_bad_minutes));
        }
        pie.data(data);

        //pie.title("Fruits imported in 2015 (in kg)");
        pie.title().enabled(false);

        //pie.labels().position("inside");
        //pie.fill("aquastyle");
        //pie.fill("black");

        pie.legend().title().enabled(false);
        /*pie.legend().title()
                .text("Retail channels")
                .padding(0d, 0d, 10d, 0d);*/

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);
        //anychart end
    }

    public void count_minutes(String characteristic, int before, int now, int aux_minutes_before, int aux_minutes_now)
    {
        Log.i("debug_positions", "GOT HERe " + before + " " + now + " " +
                aux_minutes_before + " " + aux_minutes_now);
        if (now == 1 && characteristic.equals("BodyPosture"))
        {
            standing_minutes += aux_minutes_now-aux_minutes_before;
            Log.i("debug_positions", "Contei standing " + String.valueOf(standing_minutes));
        }
        if (now == 0 && characteristic.equals("BodyPosture"))
        {
            return; //TODO currently, it's ignoring sitting time
        }
        if (now == 1 && characteristic.equals("BackPosture"))
        {
            seated_good_minutes += aux_minutes_now-aux_minutes_before;
            Log.i("debug_positions", "Contei good " + String.valueOf(seated_good_minutes));
        }
        if (now == 0 && characteristic.equals("BackPosture"))
        {
            seated_bad_minutes += aux_minutes_now-aux_minutes_before;
            Log.i("debug_positions", "Contei bad " + String.valueOf(seated_bad_minutes));
        }
    }



    //Decorator
    public class EventDecorator implements DayViewDecorator {

        private int color;
        private HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(10, color));
        }
    }
}


package pt.uninova.s4h.citizenhub.ui.reports;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAccessor;

import java.text.DateFormatSymbols;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import datastorage.MeasurementsDbHelper;
import pt.uninova.s4h.citizenhub.ui.R;

public class DaySelectedActivity extends AppCompatActivity {

    private BarChart mChart;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reports_dayselected_v2);

        //Set Titles
        TextView dayTitle = findViewById(R.id.textView_Day);
        String date = ReportsFragment.daySelected.toString();
        String day = date.substring(date.lastIndexOf("}")-2,date.lastIndexOf("}"));
        String month = date.substring(date.indexOf("-")+1, date.lastIndexOf("-"));
        String month_name = months[Integer.parseInt(month)-1];
        String year = date.substring(date.indexOf("{")+1, date.indexOf("{")+5);

        dayTitle.setText("Selected Day: " + day + " " + month_name + " " + year);

        //Set Heart Rate Measurements
        mChart = (BarChart) findViewById(R.id.chart_BarChart);
        mChart.getDescription().setEnabled(false);
        setData(10);
        mChart.setFitBars(true);
    }

    private void setData (int count) {
        ArrayList<BarEntry> yVals = new ArrayList<>();

        MeasurementsDbHelper deviceDbHelper = new MeasurementsDbHelper(getApplicationContext());
        Cursor cursor = ViewData(deviceDbHelper);
        if (cursor.getCount() == 0) {
            Log.i("DaySelectedActivityLog", "No Readings.");
        } else {
            int i = 0;
            while (cursor.moveToNext()) {

                Log.i("DaySelectedActivityLog", "Got Value From Table:"
                        + cursor.getString(2) + cursor.getString(3));
                String timestamp = cursor.getString(2);
                int year = Integer.parseInt(timestamp.substring(timestamp.length() - 4));
                int day = Integer.parseInt(timestamp.substring(8, 10));
                String month = timestamp.substring(4, 7);
                DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM")
                        .withLocale(Locale.ENGLISH);
                TemporalAccessor accessor = parser.parse(month);
                int month_number = accessor.get(ChronoField.MONTH_OF_YEAR);
                int value = Integer.parseInt(cursor.getString(3));
                int hours = Integer.parseInt(timestamp.substring(11, 13));
                int minutes = Integer.parseInt(timestamp.substring(14, 16));
                int seconds = Integer.parseInt(timestamp.substring(17, 19));
                int time = hours*3600+minutes*60+seconds;
                CalendarDay dayExample = CalendarDay.from(year, month_number, day);
                Log.i("DaySelectedActivityLog", dayExample.toString() + " " + ReportsFragment.daySelected.toString());
                if (dayExample.equals(ReportsFragment.daySelected)) {
                    Log.i("DaySelectedActivityLog", "Got value into the graph." + value);
                    yVals.add(new BarEntry(time, value));
                    i++;
                }
            }

            BarDataSet set = new BarDataSet(yVals, "HR in bpm");
            set.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set.setDrawValues(true);

            BarData data = new BarData(set);

            mChart.setData(data);
            mChart.invalidate();
            mChart.animateY(500);

        }
    }

    public Cursor ViewData (MeasurementsDbHelper deviceDbHelper){
        SQLiteDatabase db = deviceDbHelper.getReadableDatabase();
        String query = "Select * from measurements";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}

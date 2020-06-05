package pt.uninova.s4h.citizenhub.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAccessor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import datastorage.DatabaseHelperInterface;
import pt.uninova.s4h.citizenhub.ui.Home;
import pt.uninova.s4h.citizenhub.ui.R;

import static pt.uninova.s4h.citizenhub.ui.Home.fab;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    int hours_sitting = 0, minutes_sitting = 0, hours_goodPosture = 0, minutes_goodPosture = 0;
    int hours_standing = 0, minutes_standing = 0;
    int minutes_badPosture = 0, hours_badPosture = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((Home) getActivity()).setActionBarTitle("Today's Activity");
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setValuesForHomeDisplay(root);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        fab.hide();
        return root;
    }

    public Cursor ViewData(SQLiteOpenHelper databaseHelperInterface){
        SQLiteDatabase db = databaseHelperInterface.getReadableDatabase();
        String query = "Select * from measurements";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void count_minutes(String characteristic, int before, int now, int aux_minutes_before, int aux_minutes_now)
    {
        Log.i("debug_positions", "GOT HERe " + before + " " + now + " " +
                aux_minutes_before + " " + aux_minutes_now);
        if (now == 1 && characteristic.equals("BodyPosture"))
        {
            minutes_standing += aux_minutes_now-aux_minutes_before;
            Log.i("debug_positions", "Contei standing " + String.valueOf(minutes_standing));
        }
        if (now == 0 && characteristic.equals("BodyPosture"))
        {
            return; //TODO currently, it's ignoring sitting time
        }
        if (now == 1 && characteristic.equals("BackPosture"))
        {
            minutes_goodPosture += aux_minutes_now-aux_minutes_before;
            Log.i("debug_positions", "Contei good " + String.valueOf(minutes_goodPosture));
        }
        if (now == 0 && characteristic.equals("BackPosture"))
        {
            minutes_badPosture += aux_minutes_now-aux_minutes_before;
            Log.i("debug_positions", "Contei bad " + String.valueOf(minutes_badPosture));
        }
    }

    public void setValuesForHomeDisplay(View root){

        int steps = 0, heartRateAvg = 0;
        float distance = 0, kilocalories = 0, calories = 0;
        int counter_heartRateAvg = 0;


        int counter = 0;
        int aux_minutes_before = 0;
        int aux_minutes_now = 0;
        int before = -1;
        int now = -1;

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
            kilocalories = calories/1000;
        }

        //setValues
        TextView textSitting = root.findViewById(R.id.textHome_sitting);
        TextView textSteps = root.findViewById(R.id.textHome_steps);
        TextView textCalories = root.findViewById(R.id.textHome_calories);
        TextView textDistance = root.findViewById(R.id.textHome_distance);
        TextView textHR = root.findViewById(R.id.textHome_HR);

        int final_hours_goodPosture = minutes_goodPosture / 60;
        int final_minutes_goodPosture = minutes_goodPosture % 60;
        hours_sitting = (minutes_goodPosture + minutes_badPosture) / 60;
        minutes_sitting = (minutes_goodPosture + minutes_badPosture) % 60;

        NumberFormat f = new DecimalFormat("00");
        textSitting.setText("Spent " + hours_sitting + "h" + f.format(minutes_sitting) +
                "m sitting and " + final_hours_goodPosture + "h" +
                f.format(final_minutes_goodPosture) + "m with good posture.");

        NumberFormat g = new DecimalFormat("0.00");
        textDistance.setText("You've walked " + g.format(distance) + " km!");

        textSteps.setText("That is " + steps + " steps only today");


        NumberFormat h = new DecimalFormat("0.000");
        textCalories.setText("and " + h.format(kilocalories) + " kilocalories burned!");

        textHR.setText("Your average heart rate is " + heartRateAvg + " bpm.");
    };
}
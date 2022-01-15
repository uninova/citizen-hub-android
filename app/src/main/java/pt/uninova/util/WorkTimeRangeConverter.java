package pt.uninova.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceManager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WorkTimeRangeConverter {

    private static final String KEY_WORK_DAYS = "workdays";
    private static final String KEY_WORK_TIME_START = "workStart";
    private static final String KEY_WORK_TIME_END = "workEnd";
    private LocalTime workStart;
    private LocalTime workEnd;
    private List<String> workDays;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


    private static volatile WorkTimeRangeConverter instance = null;

    private WorkTimeRangeConverter(Context context) {
        workDays = new ArrayList<>();
        load(context);

    }

    private LocalTime stringToLocalTime(String timeString){
        return LocalTime.parse( timeString, formatter);
    }

    public static WorkTimeRangeConverter getInstance(Context context) {
        if (instance == null) {
            synchronized(WorkTimeRangeConverter.class) {
                if (instance == null) {
                    instance = new WorkTimeRangeConverter(context);
                }
            }
        }

        return instance;
    }

    private void load(Context context){
       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        workStart = stringToLocalTime(preferences.getString(KEY_WORK_TIME_START, "09:00"));
        workEnd = stringToLocalTime(preferences.getString(KEY_WORK_TIME_END,"17:00"));
        workDays = getArrayPrefs(preferences.getString(KEY_WORK_DAYS,"ola"),context);
    }

    public int isNowWorkTime(){
        LocalTime now = LocalTime.parse(LocalTime.now().format(formatter));
        if (now.isAfter(workStart) && now.isBefore(workEnd)) {
            System.out.println("tá dentro");
            return 1;
        }
        return 0;
    }

    //fazer worktime from LocalDateTime

    public void refreshTimeVariables(Context context){
        load(context);
    }

    private List<String> getArrayPrefs(String weekDays, Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences("workdays", 0);
        int size = prefs.getInt(weekDays + "_size", 0);
        List<String> workDaysList = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            workDaysList.add(prefs.getString(weekDays, null));
        return workDaysList;
    }

    public List<String> getCurrentEntries(MultiSelectListPreference preference) {
        CharSequence[] entries = preference.getEntries();
        CharSequence[] entryValues = preference.getEntryValues();
        List<String> currentEntries = new ArrayList<>();
        Set<String> currentEntryValues = preference.getValues();

        for (int i = 0; i < entries.length; i++)
            if (currentEntryValues.contains(entryValues[i]))
                currentEntries.add(entries[i].toString());
        return currentEntries;
    }

    public LocalTime getWorkStart() {
        return workStart;
    }

    public void setWorkStart(LocalTime workStart) {
        this.workStart = workStart;
    }

    public LocalTime getWorkEnd() {
        return workEnd;
    }

    public void setWorkEnd(LocalTime workEnd) {
        this.workEnd = workEnd;
    }

}



//get shared preferences values
//get current time & compare (when and where?)


//if () return isWorking
//usar em todos os inserts(?)


//reports -> 2 queries, 2 layouts
//summary -> igual (?)

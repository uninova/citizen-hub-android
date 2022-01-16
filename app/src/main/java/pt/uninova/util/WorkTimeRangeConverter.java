package pt.uninova.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkTimeRangeConverter {

    private static final String KEY_WORK_DAYS = "workdays";
    private static final String KEY_WORK_TIME_START = "workStart";
    private static final String KEY_WORK_TIME_END = "workEnd";
    private static volatile WorkTimeRangeConverter instance = null;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private LocalTime workStart;
    private LocalTime workEnd;
    private List<String> workDays;

    private WorkTimeRangeConverter(Context context) {
        load(context);
        workDays = new ArrayList<>();
    }

    public static WorkTimeRangeConverter getInstance(Context context) {
        if (instance == null) {
            synchronized (WorkTimeRangeConverter.class) {
                if (instance == null) {
                    instance = new WorkTimeRangeConverter(context);
                }
            }
        }

        return instance;
    }

    private LocalTime stringToLocalTime(String timeString) {
        return LocalTime.parse(timeString, formatter);
    }

    public void load(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        workStart = stringToLocalTime(preferences.getString(KEY_WORK_TIME_START, "09:00"));
        workEnd = stringToLocalTime(preferences.getString(KEY_WORK_TIME_END, "17:00"));
        Set<String> workDaysSet = preferences.getStringSet("weekdays", new HashSet<>());

        if (workDaysSet != null & workDays != null) {
            workDays.clear();
            workDays.addAll(workDaysSet);
        }
    }

    public int isNowWorkTime() {

        if (workDays != null) {
            for (String day : workDays) {

                if (day.equalsIgnoreCase(LocalDateTime.now().getDayOfWeek().name())) {

                    LocalTime now = LocalTime.parse(LocalTime.now().format(formatter));
                    if (now.isAfter(workStart) && now.isBefore(workEnd)) {
                        return 1;
                    }
                    return 0;
                }
            }
        }

        return 0;
    }

    public void refreshTimeVariables(Context context, List<String> weekdays) {
        refreshWeekDays(weekdays);
        load(context);
    }

    public void refreshWeekDays(List<String> weekDays) {
        workDays = weekDays;
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
package pt.uninova.s4h.citizenhub;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TimePreference extends DialogPreference
{
    public int hour = 0;
    public int minute = 0;

    public static int parseHour(String value)
    {
        try
        {
            String[] time = value.split(":");
            return (Integer.parseInt(time[0]));
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static int parseMinute(String value)
    {
        try
        {
            String[] time = value.split(":");
            return (Integer.parseInt(time[1]));
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static String timeToString(int h, int m)
    {
        return String.format(Locale.getDefault(),"%02d", h) + ":" + String.format(Locale.getDefault(),"%02d", m);
    }

    @Override
    public CharSequence getSummary() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, hour, minute);
        return DateFormat.getTimeFormat(getContext()).format(new Date(cal.getTimeInMillis()));
    }

    public TimePreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index)
    {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue)
    {
        String value;
        if (restoreValue)
        {
            if (defaultValue == null) value = getPersistedString("00:00");
            else value = getPersistedString(defaultValue.toString());
        }
        else
        {
            value = defaultValue.toString();
        }

        hour = parseHour(value);
        minute = parseMinute(value);
        setSummary(getSummary());
    }

    public void persistStringValue(String value)
    {
        persistString(value);
    }


}
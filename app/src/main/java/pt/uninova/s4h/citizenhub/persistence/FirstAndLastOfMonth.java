package pt.uninova.s4h.citizenhub.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FirstAndLastOfMonth {
    public static Integer catchTheFirstDayOfTheMonth(Integer month) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return calendarToInteger(cal);
    }

    public static Integer catchTheLastDayOfTheMonth(Integer month) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.set(cal.get(Calendar.YEAR), month, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendarToInteger(cal);
    }

    public static Integer calendarToInteger(Calendar date) {
        if (date != null) {
            return Integer.valueOf(date.get(Calendar.YEAR) + "" + leftPad("" + (date.get(Calendar.MONTH) + 1), "0", 2) + "" + leftPad("" + date.get(Calendar.DAY_OF_MONTH), "0", 2));
        }
        return null;
    }

    public static String leftPad(String str, String character, int size) {
        if (str != null) {
            int delta = size - str.length();
            for (int i = 0; i < delta; i++) {
                str = character + str;
            }
        }
        return str;
    }
}

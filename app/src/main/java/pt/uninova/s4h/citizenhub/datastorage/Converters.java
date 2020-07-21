package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public static Date fromTimestamp(String value) {
        try {
            return value == null ? null : df.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return date == null ? null : df.format(date);
    }
}
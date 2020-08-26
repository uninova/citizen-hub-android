package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.TypeConverter;

import java.util.Date;

public class TimestampConverter {

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime() / 1000L;
    }

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value * 1000L);
    }

}
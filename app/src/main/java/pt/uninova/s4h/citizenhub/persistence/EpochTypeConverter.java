package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.TypeConverter;
import pt.uninova.util.Pair;

import java.time.LocalDate;
import java.util.Date;

public class EpochTypeConverter {

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime() / 1000L;
    }

    @TypeConverter
    public static Long fromLocalDate(LocalDate value) {
        return value == null ? null : value.toEpochDay() * 86400L;
    }

    @TypeConverter
    public static Long fromPair(Pair<Integer, Integer> month) {
        return month == null ? null : LocalDate.of(month.getFirst(), month.getSecond(), 1).toEpochDay() * 86400L;
    }

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value * 1000L);
    }

    @TypeConverter
    public static LocalDate toLocalDate(Long value) {
        return value == null ? null : LocalDate.ofEpochDay( value / 86400L);
    }

}
package pt.uninova.s4h.citizenhub.persistence.conversion;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import pt.uninova.s4h.citizenhub.util.Pair;

public class EpochTypeConverter {

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime() / 1000L;
    }

    @TypeConverter
    public static Long fromInstant(Instant instant) {
        return instant == null ? null : instant.getEpochSecond();
    }

    @TypeConverter
    public static Long fromLocalDate(LocalDate value) {
        return value == null ? null : value.toEpochDay() * 86400L;
    }

    @TypeConverter
    public static Long fromLocalDateTime(LocalDateTime value) {
        return value == null ? null : value.toEpochSecond(ZoneOffset.UTC);
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
    public static Instant toInstant(Long value) {
        return value == null ? null : Instant.ofEpochSecond(value);
    }

    @TypeConverter
    public static LocalDate toLocalDate(Long value) {
        return value == null ? null : LocalDate.ofEpochDay(value / 86400L);
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(Long value) {
        return value == null ? null : LocalDateTime.ofEpochSecond(value, 0, ZoneOffset.UTC);

    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(Integer value) {
        return value == null ? null : LocalDateTime.ofEpochSecond(value, 0, ZoneOffset.UTC);

    }
}
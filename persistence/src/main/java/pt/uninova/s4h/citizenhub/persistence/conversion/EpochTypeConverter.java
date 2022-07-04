package pt.uninova.s4h.citizenhub.persistence.conversion;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class EpochTypeConverter {

    @TypeConverter
    public static Long fromInstant(Instant value) {
        return value == null ? null : value.toEpochMilli();
    }

    @TypeConverter
    public static Long fromLocalDate(LocalDate value) {
        return value == null ? null : value.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @TypeConverter
    public static Long fromLocalDateTime(LocalDateTime value) {
        return value == null ? null : value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @TypeConverter
    public static Instant toInstant(Long value) {
        return value == null ? null : Instant.ofEpochMilli(value);
    }

    @TypeConverter
    public static LocalDate toLocalDate(Long value) {
        return value == null ? null : Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(Long value) {
        return value == null ? null : Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
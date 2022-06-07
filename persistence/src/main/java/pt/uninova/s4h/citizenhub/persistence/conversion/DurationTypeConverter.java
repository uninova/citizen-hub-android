package pt.uninova.s4h.citizenhub.persistence.conversion;

import androidx.room.TypeConverter;

import java.time.Duration;

public class DurationTypeConverter {

    @TypeConverter
    public static Long fromDuration(Duration duration) {
        return duration.toMillis();
    }

    @TypeConverter
    public static Duration toDuration(Long millis) {
        if (millis == null)
            return null;

        return Duration.ofMillis(millis);
    }
}

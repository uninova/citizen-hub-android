package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.TypeConverter;

public class MeasurementKindTypeConverter {

    @TypeConverter
    public static MeasurementKind fromInt(int id) {
        return MeasurementKind.find(id);
    }

    @TypeConverter
    public static int toInt(MeasurementKind measurementKind) {
        return measurementKind.getId();
    }
}

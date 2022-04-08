package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.TypeConverter;

public class SampleTypeConverter {

    @TypeConverter
    public static SampleRecord fromInt(int id) {
        return new SampleRecord(id,null,null);
    }

    @TypeConverter
    public static int toInt(SampleRecord sampleRecord) {
        return sampleRecord.getId();
    }

}

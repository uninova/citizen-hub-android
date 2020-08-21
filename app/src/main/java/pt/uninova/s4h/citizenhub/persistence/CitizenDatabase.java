package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Device.class, Source.class, Measurement.class}, views = {AverageMeasurement.class}, version = 1)
@TypeConverters({TimestampConverter.class})

public abstract class CitizenDatabase extends RoomDatabase {

    public abstract DeviceDAO deviceDao();

    public abstract SourceDAO sourceDAO();

    public abstract MeasurementDAO measurementDAO();

}

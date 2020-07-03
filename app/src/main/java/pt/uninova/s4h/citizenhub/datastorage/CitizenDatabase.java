package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Device.class, Source.class, Measurement.class}, version = 1)
public abstract class CitizenDatabase extends RoomDatabase {

    public abstract DeviceDAO deviceDao();

    public abstract SourceDAO sourceDAO();

    public abstract MeasurementDAO measurementDAO();

}

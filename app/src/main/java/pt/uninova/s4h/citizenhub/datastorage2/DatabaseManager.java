package pt.uninova.s4h.citizenhub.datastorage2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Device.class, Source.class}, version = 1)
public abstract class DatabaseManager extends RoomDatabase {

    public abstract DeviceDAO deviceDao();

    public abstract SourceDAO sourceDAO();

}

package pt.uninova.s4h.citizenhub.persistence;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Device.class, Source.class, Measurement.class}, views = {AverageMeasurement.class, DailySummary.class, DateMeasurement.class}, version = 2, exportSchema = false)
public abstract class CitizenHubDatabase extends RoomDatabase {

    private static volatile CitizenHubDatabase INSTANCE;

    public static CitizenHubDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (CitizenHubDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CitizenHubDatabase.class, "citizen_hub_database").fallbackToDestructiveMigration().build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract DailySummaryDao dailySummaryDao();

    public abstract DeviceDao deviceDao();

    public abstract SourceDao sourceDao();

    public abstract MeasurementDao measurementDao();

}



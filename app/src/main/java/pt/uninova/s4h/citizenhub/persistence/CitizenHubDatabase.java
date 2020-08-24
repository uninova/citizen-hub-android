package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Device.class, Source.class, Measurement.class}, views = {AverageMeasurement.class, DailySummary.class, DateMeasurement.class}, version = 2)
public abstract class CitizenHubDatabase extends RoomDatabase {

    public abstract DailySummaryDao dailySummaryDao();

    public abstract DeviceDAO deviceDao();

    public abstract SourceDAO sourceDAO();

    public abstract MeasurementDAO measurementDao();

    private static volatile CitizenHubDatabase INSTANCE;

    private CitizenHubDatabase database;

//    public CitizenHubDatabase(Context context) {
//        context = Application
//        database = Room.databaseBuilder(context, CitizenHubDatabase.class, "citizen").build();
//    }

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



    @Override
    public void close() {
        database.close();
    }
}



package pt.uninova.s4h.citizenhub.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DeviceRecord.class, Measurement.class, Feature.class, LumbarExtensionTrainingRecord.class, SmartBearUploadDateRecord.class}, version = 37)
public abstract class CitizenHubDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile CitizenHubDatabase INSTANCE;

    public static CitizenHubDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (CitizenHubDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CitizenHubDatabase.class, "citizen_hub_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public static ExecutorService executorService() {
        return EXECUTOR_SERVICE;
    }

    public abstract DeviceDao deviceDao();

    public abstract FeatureDao featureDao();

    public abstract LumbarExtensionTrainingDao lumbarExtensionTrainingDao();

    public abstract MeasurementDao measurementDao();

    public abstract SmartBearUploadDateDao smartBearUploadDateDao();
}



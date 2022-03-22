package pt.uninova.s4h.citizenhub.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DeviceRecord.class, Measurement.class, Feature.class, LumbarExtensionTraining.class}, version = 34, exportSchema = false)
public abstract class CitizenHubDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile CitizenHubDatabase INSTANCE;


    static final Migration MIGRATION_32_33 = new Migration(32,33) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE measurement "
                    + " ADD COLUMN is_working INTEGER");
        }

    };

    public static CitizenHubDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (CitizenHubDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CitizenHubDatabase.class, "citizen_hub_database").fallbackToDestructiveMigration()
                            .addMigrations(MIGRATION_32_33).build();
                }
            }
        }

        return INSTANCE;
    }

    public static ExecutorService executorService() {
        return EXECUTOR_SERVICE;
    }

    public abstract DeviceDao deviceDao();

    public abstract MeasurementDao measurementDao();

    public abstract FeatureDao featureDao();

    public abstract LumbarExtensionTrainingDao lumbarExtensionTrainingDao();
}



package pt.uninova.s4h.citizenhub.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.uninova.s4h.citizenhub.persistence.dao.BloodPressureMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.dao.CaloriesMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.dao.CaloriesSnapshotMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.dao.DeviceDao;
import pt.uninova.s4h.citizenhub.persistence.dao.DistanceSnapshotMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.dao.FeatureDao;
import pt.uninova.s4h.citizenhub.persistence.dao.HeartRateMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.dao.LumbarExtensionTrainingDao;
import pt.uninova.s4h.citizenhub.persistence.dao.PostureMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.dao.PulseRateMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.dao.ReportDao;
import pt.uninova.s4h.citizenhub.persistence.dao.SampleDao;
import pt.uninova.s4h.citizenhub.persistence.dao.SmartBearUploadDateDao;
import pt.uninova.s4h.citizenhub.persistence.dao.StepsSnapshotMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.dao.TagDao;
import pt.uninova.s4h.citizenhub.persistence.entity.BloodPressureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.CaloriesMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.CaloriesSnapshotMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.DeviceRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.DistanceSnapshotMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.Feature;
import pt.uninova.s4h.citizenhub.persistence.entity.HeartRateMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.LumbarExtensionTrainingMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.PostureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.PulseRateMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.SampleRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.SmartBearUploadDateRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.StepsSnapshotMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.TagRecord;


@Database(autoMigrations = {},
        entities = {
                BloodPressureMeasurementRecord.class,
                CaloriesMeasurementRecord.class,
                CaloriesSnapshotMeasurementRecord.class,
                DistanceSnapshotMeasurementRecord.class,
                StepsSnapshotMeasurementRecord.class,
                DeviceRecord.class,
                Feature.class,
                HeartRateMeasurementRecord.class,
                LumbarExtensionTrainingMeasurementRecord.class,
                PostureMeasurementRecord.class,
                PulseRateMeasurementRecord.class,
                SampleRecord.class,
                SmartBearUploadDateRecord.class,
                TagRecord.class
        },
        version = 100)
public abstract class CitizenHubDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile CitizenHubDatabase INSTANCE;

    public static CitizenHubDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (CitizenHubDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CitizenHubDatabase.class, "citizen_hub_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public static ExecutorService executorService() {
        return EXECUTOR_SERVICE;
    }

    public abstract BloodPressureMeasurementDao bloodPressureMeasurementDao();

    public abstract CaloriesMeasurementDao caloriesMeasurementDao();

    public abstract CaloriesSnapshotMeasurementDao caloriesSnapshotMeasurementDao();

    public abstract DeviceDao deviceDao();

    public abstract DistanceSnapshotMeasurementDao distanceSnapshotMeasurementDao();

    public abstract FeatureDao featureDao();

    public abstract HeartRateMeasurementDao heartRateMeasurementDao();

    public abstract LumbarExtensionTrainingDao lumbarExtensionTrainingDao();

    public abstract PostureMeasurementDao postureMeasurementDao();

    public abstract PulseRateMeasurementDao pulseRateMeasurementDao();

    public abstract ReportDao reportDao();

    public abstract SampleDao sampleDao();

    public abstract SmartBearUploadDateDao smartBearUploadDateDao();

    public abstract StepsSnapshotMeasurementDao stepsSnapshotMeasurementDao();

    public abstract TagDao tagDao();
}



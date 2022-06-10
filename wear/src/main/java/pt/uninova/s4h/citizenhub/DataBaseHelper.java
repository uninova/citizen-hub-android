package pt.uninova.s4h.citizenhub;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import pt.uninova.s4h.citizenhub.data.HeartRateMeasurement;
import pt.uninova.s4h.citizenhub.data.StepsSnapshotMeasurement;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String MEASUREMENTS_TABLE = "MEASUREMENTS_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MEASUREMENT_TYPE = "MEASUREMENT_TYPE";
    public static final String COLUMN_MEASUREMENT_TIMESTAMP = "MEASUREMENT_TIMESTAMP";
    public static final String COLUMN_MEASUREMENT_VALUE = "MEASUREMENT_VALUE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "measurements.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement= "CREATE TABLE " + MEASUREMENTS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MEASUREMENT_TYPE + " INT, " + COLUMN_MEASUREMENT_TIMESTAMP + " TIMESTAMP, " + COLUMN_MEASUREMENT_VALUE + " FLOAT)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {}

    public static boolean addMeasurement(HeartRateMeasurement measurement, DataBaseHelper dataBaseHelper, long timestamp){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_MEASUREMENT_TYPE, measurement.getType());
        cv.put(COLUMN_MEASUREMENT_VALUE, measurement.getValue());
        cv.put(COLUMN_MEASUREMENT_TIMESTAMP, timestamp);

        long insert = db.insert(MEASUREMENTS_TABLE, null, cv);
        return insert != -1;
    }

    public static boolean addMeasurement(StepsSnapshotMeasurement measurement, DataBaseHelper dataBaseHelper, long timestamp){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_MEASUREMENT_TYPE, measurement.getType());
        cv.put(COLUMN_MEASUREMENT_VALUE, measurement.getValue());
        cv.put(COLUMN_MEASUREMENT_TIMESTAMP, timestamp);

        long insert = db.insert(MEASUREMENTS_TABLE, null, cv);
        return insert != -1;
    }
}

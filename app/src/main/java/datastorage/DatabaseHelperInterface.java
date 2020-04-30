package datastorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperInterface extends SQLiteOpenHelper implements DatabaseHelper {

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "S4HDB.db";

    private static final int DATABASE_VERSION = 1;
    String SQL_CREATE_DEVICES_TABLE = "CREATE TABLE " + DeviceContract.DeviceEntry.TABLE_NAME + " ("
            + DeviceContract.DeviceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + DeviceContract.DeviceEntry.COLUMN_DEVICE_NAME + " TEXT NOT NULL, "
            + DeviceContract.DeviceEntry.COLUMN_DEVICE_ADDRESS + " TEXT NOT NULL UNIQUE, "
            + DeviceContract.DeviceEntry.COLUMN_DEVICE_TYPE + " TEXT NOT NULL, "
            + DeviceContract.DeviceEntry.COLUMN_DEVICE_STATE + " BLOB " + " ) ";
    String SQL_CREATE_MEASUREMENTS_TABLE = "CREATE TABLE " + MeasurementsContract.MeasureEntry.TABLE_NAME + " ("
            + MeasurementsContract.MeasureEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + MeasurementsContract.MeasureEntry.COLUMN_SOURCE_UUID + " TEXT NOT NULL, "
            + MeasurementsContract.MeasureEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL, "
            + MeasurementsContract.MeasureEntry.COLUMN_MEASUREMENT_VALUE + " TEXT NOT NULL, "
            + MeasurementsContract.MeasureEntry.COLUMN_CHARACTERISTIC_NAME + " TEXT NOT NULL " + " ) ";
    String SQL_CREATE_SOURCE_TABLE = "CREATE TABLE " + SourceContract.SourceEntry.TABLE_NAME + " ("
            + SourceContract.SourceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + SourceContract.SourceEntry.COLUMN_SOURCE_UUID + " TEXT NOT NULL, "
            + DeviceContract.DeviceEntry.COLUMN_DEVICE_ADDRESS + " TEXT UNIQUE, "
            + SourceContract.SourceEntry.COLUMN_SOURCE_TYPE + " TEXT NOT NULL, "
            + SourceContract.SourceEntry.COLUMN_SOURCE_INTERVAL + " INTEGER " + " ) ";

    /**
     * Constructs a new instance of {@link DeviceDbHelper}.
     *
     * @param context of the app
     */
    public DatabaseHelperInterface(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_DEVICES_TABLE);
        db.execSQL(SQL_CREATE_MEASUREMENTS_TABLE);
        db.execSQL(SQL_CREATE_SOURCE_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MeasurementsContract.MeasureEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DeviceContract.DeviceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SourceContract.SourceEntry.TABLE_NAME);

        onCreate(db);
    }


}
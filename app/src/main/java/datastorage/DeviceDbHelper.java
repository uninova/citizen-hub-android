package datastorage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static datastorage.DeviceContract.DeviceEntry.TABLE_NAME;

public class DeviceDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DeviceDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "devices.db";
    private static final String MEASUREMENTS_DB_NAME = "measurements.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 7;

    /**
     * Constructs a new instance of {@link DeviceDbHelper}.
     *
     * @param context of the app
     */
    public DeviceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_DEVICES_TABLE = "CREATE TABLE " + DeviceContract.DeviceEntry.TABLE_NAME + " ("
                + DeviceContract.DeviceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + DeviceContract.DeviceEntry.COLUMN_DEVICE_NAME + " TEXT NOT NULL, "
                + DeviceContract.DeviceEntry.COLUMN_DEVICE_ADDRESS + " TEXT NOT NULL UNIQUE, "
                + DeviceContract.DeviceEntry.COLUMN_DEVICE_TYPE + " TEXT NOT NULL, "
                + DeviceContract.DeviceEntry.COLUMN_DEVICE_STATE + " BLOB " + " ) ";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_DEVICES_TABLE);
    }

    public void openAndQueryDatabase(DeviceDbHelper deviceDbHelper) {
        try {
            SQLiteDatabase newDB = deviceDbHelper.getWritableDatabase();
            Cursor c = newDB.rawQuery("SELECT name, address,connected FROM " +
                    TABLE_NAME +
                    " where connected > 0", null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String name = c.getString(c.getColumnIndex("date"));
                        String address = c.getString(c.getColumnIndex("value"));
                        Log.d("ONCREATEE", name + "," + address);
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }
    }


    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DeviceContract.DeviceEntry.TABLE_NAME);
        onCreate(db);
    }


}
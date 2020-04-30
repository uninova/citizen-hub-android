package datastorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SourceDbHelper extends SQLiteOpenHelper {


    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "source.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link DeviceDbHelper}.
     *
     * @param context of the app
     */
    public SourceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_SOURCE_TABLE = "CREATE TABLE " + SourceContract.SourceEntry.TABLE_NAME + " ("
                + SourceContract.SourceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + SourceContract.SourceEntry.COLUMN_SOURCE_UUID + " TEXT NOT NULL, "
                + DeviceContract.DeviceEntry.COLUMN_DEVICE_ADDRESS + " TEXT UNIQUE, "
                + SourceContract.SourceEntry.COLUMN_SOURCE_TYPE + " TEXT NOT NULL, "
                + SourceContract.SourceEntry.COLUMN_SOURCE_INTERVAL + " INTEGER " + " ) ";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_SOURCE_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SourceContract.SourceEntry.TABLE_NAME);
        onCreate(db);
    }
}
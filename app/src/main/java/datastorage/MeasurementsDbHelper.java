package datastorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static datastorage.MeasurementsContract.MeasureEntry.TABLE_NAME;

public class MeasurementsDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MeasurementsDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "measurements.db";

    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link DeviceDbHelper}.
     *
     * @param context of the app
     */
    public MeasurementsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_MEASUREMENTS_TABLE =  "CREATE TABLE " + MeasurementsContract.MeasureEntry.TABLE_NAME + " ("
                + MeasurementsContract.MeasureEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + MeasurementsContract.MeasureEntry.COLUMN_DATE +" TEXT NOT NULL, "
                + MeasurementsContract.MeasureEntry.COLUMN_MEASUREMENT_VALUE +" TEXT NOT NULL, "
                + MeasurementsContract.MeasureEntry.COLUMN_CHARACTERISTIC_NAME +" TEXT NOT NULL " + " ) ";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MEASUREMENTS_TABLE);
    }



    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MeasurementsContract.MeasureEntry.TABLE_NAME);
        onCreate(db);
    }


}
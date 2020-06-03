package datastorage;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceDbHelper deviceDbHelper = new DeviceDbHelper(this);
        MeasurementsDbHelper measurementsDbHelper = new MeasurementsDbHelper(this);
        // Create and/or open a database to read from it
        SQLiteDatabase db = deviceDbHelper.getReadableDatabase();
        SQLiteDatabase mesDb = measurementsDbHelper.getReadableDatabase();
    }

    private void insertDevice() {

    }


}

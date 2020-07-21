import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pt.uninova.s4h.citizenhub.datastorage.AverageMeasurement;
import pt.uninova.s4h.citizenhub.datastorage.CitizenDatabaseClient;
import pt.uninova.s4h.citizenhub.datastorage.Device;
import pt.uninova.s4h.citizenhub.datastorage.DeviceDAO;
import pt.uninova.s4h.citizenhub.datastorage.Measurement;
import pt.uninova.s4h.citizenhub.datastorage.MeasurementDAO;
import pt.uninova.s4h.citizenhub.datastorage.SourceDAO;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private DeviceDAO deviceDAO;
    private MeasurementDAO measurementDAO;
    private SourceDAO sourceDAO;
    private CitizenDatabaseClient db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = CitizenDatabaseClient.getInstance(context);
        deviceDAO = db.getDatabase().deviceDao();
        measurementDAO = db.getDatabase().measurementDAO();
        sourceDAO = db.getDatabase().sourceDAO();
    }

    @After
    public void closeDb() throws Throwable {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        Device device1 = new Device("Hexoskin", "00:00:00:01", "health", "on");
        Device device2 = new Device("Miband2", "00:00:00:02", "health", "on");
        Device device3 = new Device("Miband2", "00:00:00:03", "health", "on");
        Device device4 = new Device("PokemongoPLus", "00:00:00:04", "unknown", "on");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        df.format(date);
        Measurement measurement1 = new Measurement(1, "00:00:00:01", date, "70", "HeartRate");
        Measurement measurement2 = new Measurement(2, "00:00:00:02", date, "75", "HeartRate");
        Measurement measurement3 = new Measurement(3, "00:00:00:03", date, "80", "HeartRate");

        deviceDAO.addDevice(device1);
        deviceDAO.addDevice(device2);
        deviceDAO.addDevice(device3);
        deviceDAO.addDevice(device4);

        measurementDAO.addMeasurement(measurement1);
        measurementDAO.addMeasurement(measurement2);
        measurementDAO.addMeasurement(measurement3);


        LiveData<List<AverageMeasurement>> byName1 = measurementDAO.getMeasurementsWithoutTime();

       /* for (AverageMeasurement averageMeasurement : requireNonNull(byName1.getValue())) {
            Log.d("MeasurementTable", averageMeasurement.AverageValue + " " + averageMeasurement.date + averageMeasurement.characteristicName + "\n");
        }
        */
    }

}
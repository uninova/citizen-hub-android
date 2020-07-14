import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
        db = new CitizenDatabaseClient(context);
        deviceDAO = db.getDatabase().deviceDao();
        measurementDAO = db.getDatabase().measurementDAO();
        sourceDAO = db.getDatabase().sourceDAO();
    }

    @After
    public void closeDb() throws Throwable {
        db.finalize();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        Device device1 = new Device("Hexoskin", "00:00:00:01", "health", "on");
        Device device2 = new Device("Miband2", "00:00:00:02", "health", "on");
        Device device3 = new Device("Miband2", "00:00:00:03", "health", "on");
        Device device4 = new Device("PokemongoPLus", "00:00:00:04", "unknown", "on");
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        Measurement measurement1 = new Measurement(1, "00:00:00:01", date, "70", "HeartRate");
        Measurement measurement2 = new Measurement(2, "00:00:00:02", date, "75", "HeartRate");
        Measurement measurement3 = new Measurement(3, "00:00:00:03", date, "80", "HeartRate");
        Measurement measurement4 = new Measurement(4, "00:00:00:04", date, "70", "Unknown");

        deviceDAO.addDevice(device1);
        deviceDAO.addDevice(device2);
        deviceDAO.addDevice(device3);
        deviceDAO.addDevice(device4);

        measurementDAO.addMeasurement(measurement1);
        measurementDAO.addMeasurement(measurement2);
        measurementDAO.addMeasurement(measurement3);
        measurementDAO.addMeasurement(measurement4);


        List<Device> byName = deviceDAO.getDevices();
        List<Measurement> byName1 = measurementDAO.getMeasurementsWithCharacteristic("HeartRate");

        for (Device device : byName) {
            Log.d("DeviceTable", device.getName() + " " + device.getAddress() + "\n");
        }

        for (Measurement measurement : byName1) {
            Log.d("MeasurementTable", measurement.getUuid() + " " + measurement.getName() + " " + measurement.getTimestamp() + " " + measurement.getValue() + "\n");
        }
    }

}
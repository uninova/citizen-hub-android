import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import pt.uninova.s4h.citizenhub.datastorage.CitizenDatabaseClient;
import pt.uninova.s4h.citizenhub.datastorage.Device;
import pt.uninova.s4h.citizenhub.datastorage.DeviceDAO;
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
        Device device = new Device("asd", "asdf", "asdfg", "on");
        deviceDAO.addDevice(device);
        List<Device> byName = deviceDAO.getDevices();
    }
}
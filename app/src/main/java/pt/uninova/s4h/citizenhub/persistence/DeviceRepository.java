package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DeviceRepository {
    private DeviceDao deviceDAO;

    public DeviceRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        deviceDAO = citizenHubDatabase.deviceDao();
    }

    public void insert(Device device) {
        new InsertDeviceAsyncTask(deviceDAO).execute(device);
    }

    public void update(Device device) {
        new UpdateDeviceAsyncTask(deviceDAO).execute(device);
    }

    public void delete(Device device) {
        new DeleteDeviceAsyncTask(deviceDAO).execute(device);

    }

    public void deleteAll() {
        new DeleteAllDevicesAsyncTask(deviceDAO).execute();

    }

    public LiveData<List<Device>> getAllDevices() {
        return deviceDAO.getDevices();
    }

    private static class InsertDeviceAsyncTask extends AsyncTask<Device, Void, Void> {
        private DeviceDao deviceDAO;

        private InsertDeviceAsyncTask(DeviceDao deviceDAO) {
            this.deviceDAO = deviceDAO;
        }

        @Override
        protected Void doInBackground(Device... devices) {
            deviceDAO.addDevice(devices[0]);
            return null;
        }
    }

    private static class UpdateDeviceAsyncTask extends AsyncTask<Device, Void, Void> {
        private DeviceDao deviceDAO;

        private UpdateDeviceAsyncTask(DeviceDao deviceDAO) {
            this.deviceDAO = deviceDAO;
        }

        @Override
        protected Void doInBackground(Device... devices) {
            deviceDAO.updateDevice(devices[0]);
            return null;
        }
    }

    private static class DeleteDeviceAsyncTask extends AsyncTask<Device, Void, Void> {
        private DeviceDao deviceDAO;

        private DeleteDeviceAsyncTask(DeviceDao deviceDAO) {
            this.deviceDAO = deviceDAO;
        }

        @Override
        protected Void doInBackground(Device... devices) {
            deviceDAO.deleteDevice(devices[0]);
            return null;
        }
    }

    private static class DeleteAllDevicesAsyncTask extends AsyncTask<Void, Void, Void> {
        private DeviceDao deviceDAO;

        private DeleteAllDevicesAsyncTask(DeviceDao deviceDAO) {
            this.deviceDAO = deviceDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            deviceDAO.deleteAllDevices();
            return null;
        }


    }

}

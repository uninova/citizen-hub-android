package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MeasurementRepository {
    private MeasurementDao measurementDAO;
    private LiveData<List<Measurement>> allMeasurements;

    public MeasurementRepository(Application application) {
        CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        measurementDAO = citizenHubDatabase.measurementDao();
        allMeasurements = measurementDAO.getMeasurements();
    }

    public void insert(Measurement measurement) {
        new InsertMeasurementAsyncTask(measurementDAO).execute(measurement);
    }

    public void update(Measurement measurement) {
        new UpdateMeasurementAsyncTask(measurementDAO).execute(measurement);
    }

    public void delete(Measurement measurement) {
        new DeleteMeasurementAsyncTask(measurementDAO).execute(measurement);

    }

    public void deleteAll() {
        new DeleteAllMeasurementsAsyncTask(measurementDAO).execute();

    }

    public LiveData<List<Measurement>> getAllMeasurements() {
        return allMeasurements;
    }

    private static class InsertMeasurementAsyncTask extends AsyncTask<Measurement, Void, Void> {
        private MeasurementDao measurementDAO;

        private InsertMeasurementAsyncTask(MeasurementDao measurementDAO) {
            this.measurementDAO = measurementDAO;
        }

        @Override
        protected Void doInBackground(Measurement... measurements) {
            measurementDAO.addMeasurement(measurements[0]);
            return null;
        }
    }

    private static class UpdateMeasurementAsyncTask extends AsyncTask<Measurement, Void, Void> {
        private MeasurementDao measurementDAO;

        private UpdateMeasurementAsyncTask(MeasurementDao measurementDAO) {
            this.measurementDAO = measurementDAO;
        }

        @Override
        protected Void doInBackground(Measurement... measurements) {
            measurementDAO.updateMeasurement(measurements[0]);
            return null;
        }
    }

    private static class DeleteMeasurementAsyncTask extends AsyncTask<Measurement, Void, Void> {
        private MeasurementDao measurementDAO;

        private DeleteMeasurementAsyncTask(MeasurementDao measurementDAO) {
            this.measurementDAO = measurementDAO;
        }

        @Override
        protected Void doInBackground(Measurement... measurements) {
            measurementDAO.deleteMeasurement(measurements[0]);
            return null;
        }
    }

    private static class DeleteAllMeasurementsAsyncTask extends AsyncTask<Void, Void, Void> {
        private MeasurementDao measurementDAO;

        private DeleteAllMeasurementsAsyncTask(MeasurementDao measurementDAO) {
            this.measurementDAO = measurementDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            measurementDAO.deleteAllMeasurements();
            return null;
        }


    }

}

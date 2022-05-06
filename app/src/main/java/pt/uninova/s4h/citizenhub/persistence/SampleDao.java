package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;

import pt.uninova.s4h.citizenhub.data.BloodPressureMeasurement;
import pt.uninova.s4h.citizenhub.data.BloodPressureValue;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;

@Dao
public abstract class SampleDao {

    private final BloodPressureMeasurementDao bloodPressureMeasurementDao;

    public SampleDao(CitizenHubDatabase database) {
        bloodPressureMeasurementDao = database.bloodPressureDao();
    }

    @Insert
    public abstract long insert(SampleRecord sampleRecord);

    @Transaction
    void insert(Sample sample) {
        final SampleRecord sampleRecord = new SampleRecord(sample.getTimestamp(), sample.getSource().getAddress());
        final int sampleId = (int) insert(sampleRecord);

        for (Measurement<?> measurement : sample.getMeasurements()) {
            switch (measurement.getType()) {
                case Measurement.BLOOD_PRESSURE:
                    BloodPressureValue bloodPressureValue = ((BloodPressureMeasurement) measurement).getValue();
                    BloodPressureMeasurementRecord bloodPressureMeasurementRecord = new BloodPressureMeasurementRecord(sampleId, bloodPressureValue.getSystolic(), bloodPressureValue.getDiastolic(), bloodPressureValue.getMeanArterialPressure());

                    bloodPressureMeasurementDao.insert(bloodPressureMeasurementRecord);
                    break;
            }
        }
    }
}


package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;

import pt.uninova.s4h.citizenhub.data.BloodPressureMeasurement;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;

@Dao
public interface SampleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(SampleRecord sampleRecord);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    default void insert(Sample sample) {

        SampleRecord record = new SampleRecord(sample.getTimestamp(), sample.getSource().getAddress());
        final long sampleId = insert(record);
        record.setId((int) sampleId);

        for (Measurement<?> measurement : sample.getMeasurements()
        ) {

            switch (measurement.getType()) {
                case Measurement.BLOOD_PRESSURE:
                    insert(record, (BloodPressureMeasurement) measurement);
                    break;
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    default void insert(SampleRecord sampleRecord, BloodPressureMeasurement measurement) {
        BloodPressureRecord record = new BloodPressureRecord(sampleRecord, measurement.getValue().getSystolic(), measurement.getValue().getDiastolic(), measurement.getValue().getMeanArterialPressure());
        insert(record);
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(BloodPressureRecord bloodPressureRecord);

}


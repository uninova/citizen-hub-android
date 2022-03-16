package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface SampleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Sample sample);

    @Transaction
    void insertSampleWithMeasurements(List<SampleWithMeasurements> sampleWithMeasurements);
}


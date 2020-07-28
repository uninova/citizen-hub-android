package pt.uninova.s4h.citizenhub.datastorage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface MeasurementDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMeasurement(Measurement measurement);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMeasurement(Measurement measurement);

    @Delete
    void deleteMeasurement(Measurement measurement);

    @Query("DELETE FROM measurements")
    void deleteAllMeasurements();

    @Query("SELECT * FROM measurements")
    LiveData<List<Measurement>> getMeasurements();

    @Query("SELECT * FROM measurements WHERE type = :characteristicType")
    LiveData<List<Measurement>> getMeasurementsWithCharacteristic(int characteristicType);

    @Query("SELECT * FROM AverageMeasurement")
    LiveData<List<AverageMeasurement>> getMeasurementsWithoutTime();

    @Query("SELECT AverageValue FROM AverageMeasurement WHERE CharacteristicType = :characteristicType AND Date=(date(:datez)) ")
    float getAvgFromDay(int characteristicType, Date datez);
    // SELECT AverageValue,CharacteristicName, date FROM AverageMeasurement WHERE CharacteristicName = 'HeartRate' AND date=('2020-07-21')


}

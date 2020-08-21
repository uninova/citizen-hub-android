package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.*;

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
    @TypeConverters(TimestampConverter.class)
    float getAvgFromDay(int characteristicType, Date datez);
    // SELECT AverageValue,CharacteristicName, date FROM AverageMeasurement WHERE CharacteristicName = 'HeartRate' AND date=('2020-07-21')


}

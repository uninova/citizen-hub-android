package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.Date;
import java.util.List;

@Dao
public interface MeasurementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMeasurement(Measurement measurement);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMeasurement(Measurement measurement);

    @Delete
    void deleteMeasurement(Measurement measurement);

    @Query("DELETE FROM measurement")
    void deleteAllMeasurements();

    @Query("SELECT * FROM measurement")
    LiveData<List<Measurement>> getMeasurements();

    @Query("SELECT * FROM measurement WHERE kind_id = :characteristicType")
    LiveData<List<Measurement>> getMeasurementsWithCharacteristic(int characteristicType);

    @Query("SELECT * FROM AverageMeasurement")
    LiveData<List<AverageMeasurement>> getMeasurementsWithoutTime();

    @Query("SELECT AverageValue FROM AverageMeasurement WHERE CharacteristicType = :characteristicType AND Date=(date(:datez)) ")
    @TypeConverters(TimestampConverter.class)
    float getAvgFromDay(int characteristicType, Date datez);

}

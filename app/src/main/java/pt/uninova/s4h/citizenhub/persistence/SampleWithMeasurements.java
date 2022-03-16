package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Relation;

import java.time.Instant;
import java.util.List;

public class SampleWithMeasurements extends Sample {

    @Relation(parentColumn = "id", entityColumn = "id", entity = BloodPressureMeasurement.class)
    private List<BloodPressureMeasurement> bloodPressureMeasurement;

    public SampleWithMeasurements(Integer id, Instant timestamp, String device_address) {
        super(id, timestamp, device_address);
    }

    public List<BloodPressureMeasurement> get(){
        return this.bloodPressureMeasurement;
    }


}

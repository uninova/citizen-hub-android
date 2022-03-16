package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Relation;

import java.time.Instant;

public class SampleWithMeasurements extends Sample {

    @Relation(parentColumn = "id", entityColumn = "id", entity = BloodPressureMeasurement.class)
    private BloodPressureMeasurement bloodPressureMeasurement;

    public SampleWithMeasurements(Integer id, Instant timestamp, String device_address) {
        super(id, timestamp, device_address);
    }

    public BloodPressureMeasurement getBloodPressureMeasurement(){
        return this.bloodPressureMeasurement;
    }


}

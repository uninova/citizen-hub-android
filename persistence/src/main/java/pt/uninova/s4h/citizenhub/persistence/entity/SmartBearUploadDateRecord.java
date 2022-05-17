package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;

@Entity(tableName = "smart_bear_upload_date")
public class SmartBearUploadDateRecord {

    @PrimaryKey
    @TypeConverters({EpochTypeConverter.class})
    private LocalDate date;

    public SmartBearUploadDateRecord(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

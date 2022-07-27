package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;

@Entity(tableName = "smart_bear_daily_report")
public class SmartBearDailyReportRecord {

    @PrimaryKey
    @TypeConverters({EpochTypeConverter.class})
    @NonNull
    private LocalDate date;

    public SmartBearDailyReportRecord(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

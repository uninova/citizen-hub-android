package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;

public class Smart4HealthWeeklyReportRecord {

    @PrimaryKey
    @TypeConverters({EpochTypeConverter.class})
    @NonNull
    private LocalDate date;

    @NonNull
    @ColumnInfo(defaultValue = "0")
    private Boolean fhir;

    @NonNull
    @ColumnInfo(defaultValue = "0")
    private Boolean pdf;

    public Smart4HealthWeeklyReportRecord(LocalDate date, Boolean fhir, Boolean pdf) {
        this.date = date;
        this.fhir = fhir;
        this.pdf = pdf;
    }

    public LocalDate getDate() {
        return date;
    }

    public Boolean getFhir() {
        return fhir;
    }

    public Boolean getPdf() {
        return pdf;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setFhir(Boolean fhir) {
        this.fhir = fhir;
    }

    public void setPdf(Boolean pdf) {
        this.pdf = pdf;
    }

}

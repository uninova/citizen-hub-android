package pt.uninova.s4h.citizenhub.report;

import android.content.Context;

import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;

public class StringMeasurementId implements LocalizedString {

    private final int measurementId;
    private final MeasurementKindLocalization measurementKindLocalization;

    public StringMeasurementId(int measurementId, MeasurementKindLocalization measurementKindLocalization/*, MeasurementKindLocalization measurementKindLocalization*/){
        this.measurementId = measurementId;
        this.measurementKindLocalization = measurementKindLocalization;
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    @Override
    public String getLocalizedString(){
        return measurementKindLocalization.localize(measurementId);
    }
}

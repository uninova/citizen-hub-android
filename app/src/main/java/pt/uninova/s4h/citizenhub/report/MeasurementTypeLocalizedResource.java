package pt.uninova.s4h.citizenhub.report;

import android.content.Context;

import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;

public class MeasurementTypeLocalizedString implements LocalizedString {

    private final int measurementType;
    private final MeasurementKindLocalization measurementKindLocalization;

    public MeasurementTypeLocalizedString(Context context, int measurementType) {
        this.measurementType = measurementType;
        this.measurementKindLocalization = new MeasurementKindLocalization(context);
    }

    /***************************************
     * This section only has get functions *
     ***************************************/

    public int getMeasurementType() {
        return measurementType;
    }

    @Override
    public String getLocalizedString() {
        return measurementKindLocalization.localize(measurementType);
    }
}

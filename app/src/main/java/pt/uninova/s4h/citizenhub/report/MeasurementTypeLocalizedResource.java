package pt.uninova.s4h.citizenhub.report;

import android.content.Context;

import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;

public class MeasurementTypeLocalizedResource implements LocalizedResource {

    private final int measurementType;
    private final MeasurementKindLocalization measurementKindLocalization;

    public MeasurementTypeLocalizedResource(Context context, int measurementType) {
        this.measurementType = measurementType;
        this.measurementKindLocalization = new MeasurementKindLocalization(context);
    }

    public MeasurementTypeLocalizedResource(MeasurementKindLocalization measurementKindLocalization, int measurementType) {
        this.measurementType = measurementType;
        this.measurementKindLocalization = measurementKindLocalization;
    }


    public int getMeasurementType() {
        return measurementType;
    }

    @Override
    public String getLocalizedString() {
        return measurementKindLocalization.localize(measurementType);
    }
}

package pt.uninova.s4h.citizenhub.report;

import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;

public class LabelMeasurementId implements LocalizedLabel{

    private final int measurementId;
    private final MeasurementKindLocalization measurementKindLocalization;

    public LabelMeasurementId(int measurementId, MeasurementKindLocalization measurementKindLocalization){
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

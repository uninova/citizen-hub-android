package pt.uninova.s4h.citizenhub;

import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class FeatureListItem {
        private MeasurementKind measurementKind;
    private boolean active;

    public FeatureListItem(MeasurementKind measurementKind)
    {
        this(measurementKind,false);
    }

    public FeatureListItem(MeasurementKind measurementKind, boolean active)
    {
        this.measurementKind = measurementKind;
        this.active = active;
    }

    public MeasurementKind getMeasurementKind() {
        return measurementKind;
    }

    public void setMeasurementKind(MeasurementKind measurementKind) {
        this.measurementKind = measurementKind;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void enable() {
        //Objects.requireNonNull(((CitizenHubServiceBound) application).getService().getAgentOrchestrator().getDeviceAgentMap().get(device)).enableMeasurement(getMeasurementKind());
    }

    //
}

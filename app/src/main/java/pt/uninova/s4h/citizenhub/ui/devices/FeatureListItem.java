package pt.uninova.s4h.citizenhub.ui.devices;


public class FeatureListItem {

    private int featureId;
    private String label;
    private boolean active;

    public FeatureListItem(int featureId, String label) {
        this(featureId, label, false);
    }

    public FeatureListItem(int featureId, String label, boolean active) {
        this.featureId = featureId;
        this.label = label;
        this.active = active;
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int value) {
        this.featureId = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}

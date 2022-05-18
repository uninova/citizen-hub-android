package pt.uninova.s4h.citizenhub;


public class FeatureListItem {

    private String label;
    private int featureId;
    private boolean active;

    public FeatureListItem(int featureId, String label) {
        this(featureId, label, false);
    }

    public FeatureListItem(int featureId, String label, boolean active) {
        this.featureId = featureId;
        this.active = active;
        this.label = label;
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int value) {
        this.featureId = value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

package pt.uninova.s4h.citizenhub.persistence.entity.util;

public class AggregateSummary {

    private Double average;
    private Double maximum;
    private Double minimum;

    public AggregateSummary(Double average, Double maximum, Double minimum) {
        this.average = average;
        this.maximum = maximum;
        this.minimum = minimum;
    }

    public Double getAverage() {
        return average;
    }

    public Double getMaximum() {
        return maximum;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }
}

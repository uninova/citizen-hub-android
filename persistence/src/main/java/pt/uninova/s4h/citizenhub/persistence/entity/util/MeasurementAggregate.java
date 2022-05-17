package pt.uninova.s4h.citizenhub.persistence;

public class MeasurementAggregate {

    private Integer measurementKind;
    private Double average;
    private Integer count;
    private Double max;
    private Double min;
    private Double sum;

    public Integer getMeasurementKind() {
        return measurementKind;
    }

    public void setMeasurementKind(Integer measurementKind) {
        this.measurementKind = measurementKind;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}

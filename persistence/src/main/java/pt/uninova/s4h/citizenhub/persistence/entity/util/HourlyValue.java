package pt.uninova.s4h.citizenhub.persistence;

public class HourlyValue {

    private Integer hour;
    private Double value;

    public Integer getHour() {
        return hour;
    }

    public Double getValue() {
        return value;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}

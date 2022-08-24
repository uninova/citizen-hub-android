package pt.uninova.s4h.citizenhub.persistence.entity.util;

public class ActivityDetailUtil {

    private Double value;

    private Double time;

    public Float getValue(){
        return Float.valueOf(value.toString());
    }

    public void setValue(Double value) { this.value = value; }

    public Float getTime(){
        return Float.valueOf(time.toString());
    }

    public void setTime(Double time) { this.time = time; }

}

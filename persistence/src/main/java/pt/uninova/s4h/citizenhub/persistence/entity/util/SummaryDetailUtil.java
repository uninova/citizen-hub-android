package pt.uninova.s4h.citizenhub.persistence.entity.util;

public class SummaryDetailUtil {

    private Double value1;

    private Double value2;

    private Double value3;

    private Double time;

    public Float getValue1(){
        return Float.valueOf(value1.toString());
    }

    public void setValue1(Double value) { this.value1 = value; }

    public Float getValue2(){
        return Float.valueOf(value2.toString());
    }

    public void setValue2(Double value) { this.value2 = value; }

    public Float getValue3(){
        return Float.valueOf(value3.toString());
    }

    public void setValue3(Double value) { this.value3 = value; }

    public Float getTime(){
        return Float.valueOf(time.toString());
    }

    public void setTime(Double time) { this.time = time; }

}

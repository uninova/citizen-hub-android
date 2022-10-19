package pt.uninova.s4h.citizenhub.persistence.entity.util;

public class SummaryDetailBloodPressureUtil {

    private Double systolic;

    private Double diastolic;

    private Double mean;

    private Double time;

    public Double getSystolic(){
        return systolic;
    }

    public void setSystolic(Double systolic) { this.systolic = systolic; }

    public Double getDiastolic(){ return diastolic; }

    public void setDiastolic(Double diastolic) { this.diastolic = diastolic; }

    public Double getMean(){
        return mean;
    }

    public void setMean(Double mean) { this.mean = mean; }

    public Double getTime(){
        return time;
    }

    public void setTime(Double time) { this.time = time; }

}

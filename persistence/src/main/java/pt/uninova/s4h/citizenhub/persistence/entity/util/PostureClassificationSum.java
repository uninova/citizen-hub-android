package pt.uninova.s4h.citizenhub.persistence.entity.util;

public class PostureClassificationSum {

    private Integer classification;
    private Double duration;

    public PostureClassificationSum(Integer classification, Double duration) {
        this.classification = classification;
        this.duration = duration;
    }

    public Integer getClassification() {
        return classification;
    }

    public Double getDuration() {
        return duration;
    }

    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }
}

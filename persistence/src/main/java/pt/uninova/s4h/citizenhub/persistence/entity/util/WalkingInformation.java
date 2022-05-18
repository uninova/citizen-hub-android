package pt.uninova.s4h.citizenhub.persistence.entity.util;

public class WalkingInformation {

    private Integer steps;
    private Double distance;
    private Double calories;

    public WalkingInformation(Integer steps, Double distance, Double calories) {
        this.steps = steps;
        this.distance = distance;
        this.calories = calories;
    }

    public Double getCalories() {
        return calories;
    }

    public Double getDistance() {
        return distance;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }
}

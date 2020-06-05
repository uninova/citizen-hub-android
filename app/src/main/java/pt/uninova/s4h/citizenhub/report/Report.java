package pt.uninova.s4h.citizenhub.report;

public class Report {

    private final String pathName;
    private String heartrate, steps, distance, calories, hoursSitting, minutesSitting, hoursGoodPosture, minutesGoodPosture;
    private String username, age, stepsGoal, standingTimeHours, standingTimeMinutes, minHeartRate, maxHeartrate;
    private String minHeartRateTime, maxHeartRateTime;


    public Report(int heartrate, int steps, int distance, int calories, int hoursSitting, int minutesSitting, int hoursGoodPosture, int minutesGoodPosture,
                  String username, int age, int stepsgoal, int standingtimehours, int standingtimeminutes, int minheartrate, int maxheartrate, String minheartratetime,
                  String maxheartratetime, String pathName) {
        this.heartrate = String.valueOf(heartrate);
        this.steps = String.valueOf(steps);
        this.distance = String.valueOf(distance);
        this.calories = String.valueOf(calories);
        this.hoursSitting = String.valueOf(hoursSitting);
        this.minutesSitting = String.valueOf(minutesSitting);
        this.hoursGoodPosture = String.valueOf(hoursGoodPosture);
        this.minutesGoodPosture = String.valueOf(minutesGoodPosture);
        this.username = username;
        this.age = String.valueOf(age);
        this.stepsGoal = String.valueOf(stepsgoal);
        this.standingTimeHours = String.valueOf(standingtimehours);
        this.standingTimeMinutes = String.valueOf(standingtimeminutes);
        this.maxHeartrate = String.valueOf(maxheartrate);
        this.minHeartRate = String.valueOf(minheartrate);
        this.minHeartRateTime = minheartratetime;
        this.maxHeartRateTime = maxheartratetime;
        this.pathName = pathName;
    }

    public String getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(String heartrate) {
        this.heartrate = heartrate;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getHoursSitting() {
        return hoursSitting;
    }

    public void setHoursSitting(String hoursSitting) {
        this.hoursSitting = hoursSitting;
    }

    public String getMinutesSitting() {
        return minutesSitting;
    }

    public void setMinutesSitting(String minutesSitting) {
        this.minutesSitting = minutesSitting;
    }

    public String getHoursGoodPosture() {
        return hoursGoodPosture;
    }

    public void setHoursGoodPosture(String hoursGoodPosture) {
        this.hoursGoodPosture = hoursGoodPosture;
    }

    public String getMinutesGoodPosture() {
        return minutesGoodPosture;
    }

    public void setMinutesGoodPosture(String minutesGoodPosture) {
        this.minutesGoodPosture = minutesGoodPosture;
    }


    public String getPathName() {
        return pathName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStepsGoal() {
        return stepsGoal;
    }

    public void setStepsGoal(String stepsGoal) {
        this.stepsGoal = stepsGoal;
    }

    public String getStandingTimeHours() {
        return standingTimeHours;
    }

    public void setStandingTimeHours(String standingTimeHours) {
        this.standingTimeHours = standingTimeHours;
    }

    public String getStandingTimeMinutes() {
        return standingTimeMinutes;
    }

    public void setStandingTimeMinutes(String standingTimeMinutes) {
        this.standingTimeMinutes = standingTimeMinutes;
    }

    public String getMinHeartRate() {
        return minHeartRate;
    }

    public void setMinHeartRate(String minHeartRate) {
        this.minHeartRate = minHeartRate;
    }

    public String getMaxHeartrate() {
        return maxHeartrate;
    }

    public void setMaxHeartrate(String maxHeartrate) {
        this.maxHeartrate = maxHeartrate;
    }

    public String getMinHeartRateTime() {
        return minHeartRateTime;
    }

    public void setMinHeartRateTime(String minHeartRateTime) {
        this.minHeartRateTime = minHeartRateTime;
    }

    public String getMaxHeartRateTime() {
        return maxHeartRateTime;
    }

    public void setMaxHeartRateTime(String maxHeartRateTime) {
        this.maxHeartRateTime = maxHeartRateTime;
    }
}

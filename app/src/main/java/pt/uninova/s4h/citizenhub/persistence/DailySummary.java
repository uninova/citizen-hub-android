package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.TypeConverters;

import java.util.Date;

@DatabaseView(value = "SELECT DISTINCT date, (SELECT AVG(value) FROM date_measurement WHERE kind_id = 1 AND date_measurement.date = main.date GROUP BY date) average_heart_rate, (SELECT SUM(value) FROM date_measurement WHERE kind_id = 2 AND date_measurement.date = main.date GROUP BY date) sum_steps, (SELECT SUM(value) FROM date_measurement WHERE kind_id = 3 AND date_measurement.date = main.date GROUP BY date) sum_distance, (SELECT SUM(value) FROM date_measurement WHERE kind_id = 4 AND date_measurement.date = main.date GROUP BY date) sum_calories, (SELECT SUM(value) FROM date_measurement WHERE kind_id = 5 AND date_measurement.date = main.date GROUP BY date) sum_good_posture, (SELECT SUM(value) FROM date_measurement WHERE kind_id = 6 AND date_measurement.date = main.date GROUP BY date) sum_bad_posture FROM date_measurement AS main", viewName = "daily_summary")
public class DailySummary {

    @ColumnInfo(name = "average_heart_rate")
    private Double averageHeartRate;
    @TypeConverters(TimestampConverter.class)
    private Date date;
    @ColumnInfo(name = "sum_bad_posture")
    private Integer sumBadPosture;
    @ColumnInfo(name = "sum_calories")
    private Integer sumCalories;
    @ColumnInfo(name = "sum_distance")
    private Integer sumDistance;
    @ColumnInfo(name = "sum_good_posture")
    private Integer sumGoodPosture;
    @ColumnInfo(name = "sum_steps")
    private Integer sumSteps;

    public Double getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(Double averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getSumBadPosture() {
        return sumBadPosture;
    }

    public void setSumBadPosture(Integer sumBadPosture) {
        this.sumBadPosture = sumBadPosture;
    }

    public Integer getSumCalories() {
        return sumCalories;
    }

    public void setSumCalories(Integer sumCalories) {
        this.sumCalories = sumCalories;
    }

    public Integer getSumDistance() {
        return sumDistance;
    }

    public void setSumDistance(Integer sumDistance) {
        this.sumDistance = sumDistance;
    }

    public Integer getSumGoodPosture() {
        return sumGoodPosture;
    }

    public void setSumGoodPosture(Integer sumGoodPosture) {
        this.sumGoodPosture = sumGoodPosture;
    }

    public Integer getSumSteps() {
        return sumSteps;
    }

    public void setSumSteps(Integer sumSteps) {
        this.sumSteps = sumSteps;
    }

}

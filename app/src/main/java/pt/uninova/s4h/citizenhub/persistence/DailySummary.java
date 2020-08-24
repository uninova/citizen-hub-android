package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.TypeConverters;

import java.util.Date;

@DatabaseView(value = "SELECT DISTINCT date, (SELECT AVG(value) FROM date_measurement WHERE date_measurement.kind_id = 1 AND date_measurement.date = main.date GROUP BY date) average_heart_rate, (SELECT SUM(value) FROM date_measurement WHERE date_measurement.kind_id = 2 AND date_measurement.date = main.date GROUP BY date) sum_steps, (SELECT SUM(value) FROM date_measurement WHERE date_measurement.kind_id = 3 AND date_measurement.date = main.date GROUP BY date) sum_distance, (SELECT SUM(value) FROM date_measurement WHERE date_measurement.kind_id = 4 AND date_measurement.date = main.date GROUP BY date) sum_calories, (SELECT SUM(value) FROM date_measurement WHERE date_measurement.kind_id = 5 AND date_measurement.date = main.date GROUP BY date) sum_good_posture, (SELECT SUM(value) FROM date_measurement WHERE date_measurement.kind_id = 6 AND date_measurement.date = main.date GROUP BY date) sum_bad_posture FROM date_measurement AS main", viewName = "daily_summary")
public class DailySummary {

    @ColumnInfo(name = "average_heart_rate")
    private float averageHeartRate;
    @TypeConverters(TimestampConverter.class)
    private Date date;
    @ColumnInfo(name = "sum_bad_posture")
    private float sumBadPosture;
    @ColumnInfo(name = "sum_calories")
    private int sumCalories;
    @ColumnInfo(name = "sum_distance")
    private int sumDistance;
    @ColumnInfo(name = "sum_good_posture")
    private float sumGoodPosture;
    @ColumnInfo(name = "sum_steps")
    private int sumSteps;

    public float getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(float averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getSumBadPosture() {
        return sumBadPosture;
    }

    public void setSumBadPosture(float sumBadPosture) {
        this.sumBadPosture = sumBadPosture;
    }

    public int getSumCalories() {
        return sumCalories;
    }

    public void setSumCalories(int sumCalories) {
        this.sumCalories = sumCalories;
    }

    public int getSumDistance() {
        return sumDistance;
    }

    public void setSumDistance(int sumDistance) {
        this.sumDistance = sumDistance;
    }

    public float getSumGoodPosture() {
        return sumGoodPosture;
    }

    public void setSumGoodPosture(float sumGoodPosture) {
        this.sumGoodPosture = sumGoodPosture;
    }

    public int getSumSteps() {
        return sumSteps;
    }

    public void setSumSteps(int sumSteps) {
        this.sumSteps = sumSteps;
    }
}

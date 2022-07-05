package pt.uninova.s4h.citizenhub.report;

import android.content.Context;
import android.content.res.Resources;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;
import pt.uninova.s4h.citizenhub.persistence.entity.util.ReportUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.ReportRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DailyReportGenerator {

    private final Resources resources;
    private final MeasurementKindLocalization localization;

    public DailyReportGenerator(Context context) {
        this.resources = context.getResources();
        this.localization = new MeasurementKindLocalization(context);
    }

    private void groupSimpleRecords(ReportUtil reportUtil, List<Group> groups) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        System.out.println("Entered Simples Daily Reports.");
        if (reportUtil.getCalories() != null || reportUtil.getDistance() != null || reportUtil.getSteps() != null) {
            //StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_CALORIES, localization);
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_DISTANCE_SNAPSHOT, localization);
            Group groupActivity = new Group(label);
            if (reportUtil.getSteps() != null) {
                groupActivity.getItemList().add(new Item(new StringType(resources.getString(R.string.report_steps_label)), new StringValue(decimalFormat.format(reportUtil.getSteps())), new StringUnits("-")));
            }
            if (reportUtil.getDistance() != null) {
                groupActivity.getItemList().add(new Item(new StringType(resources.getString(R.string.report_distance_label)), new StringValue(decimalFormat.format(reportUtil.getDistance())), new StringUnits(resources.getString(R.string.report_distance_units))));
            }
            if (reportUtil.getCalories() != null) {
                groupActivity.getItemList().add(new Item(new StringType(resources.getString(R.string.report_calories_label)), new StringValue(decimalFormat.format(reportUtil.getCalories())), new StringUnits(resources.getString(R.string.report_calories_units))));
            }
            groups.add(groupActivity);
        }
        /*if(reportUtil.getCalories()!=null){
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_CALORIES, localization);
            Group groupCalories = new Group(label);
            groupCalories.getItemList().add(new Item(new StringType("Calories"), new StringValue(reportUtil.getCalories().toString())));
            groups.add(groupCalories);
        }
        if(reportUtil.getDistance()!=null){
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_DISTANCE_SNAPSHOT, localization);
            Group groupDistance = new Group(label);
            groupDistance.getItemList().add(new Item(new StringType("Distance"), new StringValue(reportUtil.getDistance().toString())));
            groups.add(groupDistance);
        }
        if(reportUtil.getSteps()!=null){
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_STEPS_SNAPSHOT, localization);
            Group groupSteps = new Group(label);
            groupSteps.getItemList().add(new Item(new StringType("Steps"), new StringValue(reportUtil.getSteps().toString())));
            groups.add(groupSteps);
        }*/
        if(reportUtil.getMaxBreathingRate() != null && reportUtil.getMinBreathingRate() != null && reportUtil.getAvgBreathingRate() != null){
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_BREATHING_RATE, localization);
            Group groupBreathingRate = new Group(label);
            groupBreathingRate.getItemList().add(new Item(new StringType(resources.getString(R.string.report_br_average_label)), new StringValue(decimalFormat.format(reportUtil.getAvgBreathingRate())), new StringUnits(resources.getString(R.string.report_br_units))));
            groupBreathingRate.getItemList().add(new Item(new StringType(resources.getString(R.string.report_br_maximum_label)), new StringValue(decimalFormat.format(reportUtil.getMaxBreathingRate())), new StringUnits(resources.getString(R.string.report_br_units))));
            groupBreathingRate.getItemList().add(new Item(new StringType(resources.getString(R.string.report_br_minimum_label)), new StringValue(decimalFormat.format(reportUtil.getMinBreathingRate())), new StringUnits(resources.getString(R.string.report_br_units))));
            groups.add(groupBreathingRate);
        }
        if (reportUtil.getMaxHeartRate() != null && reportUtil.getMinHeartRate() != null && reportUtil.getAvgHeartRate() != null) {
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_HEART_RATE, localization);
            Group groupHeartRate = new Group(label);
            groupHeartRate.getItemList().add(new Item(new StringType(resources.getString(R.string.report_hr_average_label)), new StringValue(decimalFormat.format(reportUtil.getAvgHeartRate())), new StringUnits(resources.getString(R.string.report_hr_units))));
            groupHeartRate.getItemList().add(new Item(new StringType(resources.getString(R.string.report_hr_maximum_label)), new StringValue(decimalFormat.format(reportUtil.getMaxHeartRate())), new StringUnits(resources.getString(R.string.report_hr_units))));
            groupHeartRate.getItemList().add(new Item(new StringType(resources.getString(R.string.report_hr_minimum_label)), new StringValue(decimalFormat.format(reportUtil.getMinHeartRate())), new StringUnits(resources.getString(R.string.report_hr_units))));
            groups.add(groupHeartRate);
        }
        if (reportUtil.getCorrectPostureDuration() != null) {
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_POSTURE, localization);
            Group groupPosture = new Group(label);
            groupPosture.getItemList().add(new Item(new StringType(resources.getString(R.string.report_correct_posture_label)), new StringValue(secondsToString(reportUtil.getCorrectPostureDuration().getSeconds())), new StringUnits("-")));
            if (reportUtil.getWrongPostureDuration() != null) {
                groupPosture.getItemList().add(new Item(new StringType(resources.getString(R.string.report_incorrect_posture_label)), new StringValue(secondsToString(reportUtil.getWrongPostureDuration().getSeconds())), new StringUnits("-")));
            } else {
                groupPosture.getItemList().add(new Item(new StringType(resources.getString(R.string.report_incorrect_posture_label)), new StringValue("0s"), new StringUnits("-")));
            }
            groups.add(groupPosture);
        } else {
            if (reportUtil.getWrongPostureDuration() != null) {
                StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_POSTURE, localization);
                Group groupPosture = new Group(label);
                groupPosture.getItemList().add(new Item(new StringType(resources.getString(R.string.report_correct_posture_label)), new StringValue("0s"), new StringUnits("-")));
                groupPosture.getItemList().add(new Item(new StringType(resources.getString(R.string.report_incorrect_posture_label)), new StringValue(secondsToString(reportUtil.getWrongPostureDuration().getSeconds())), new StringUnits("-")));
                groups.add(groupPosture);
            }
        }
    }

    private void groupBloodPressure(List<ReportUtil> observeBloodPressure, List<Group> groups) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        System.out.println("Entered Blood Pressure.");
        if (observeBloodPressure.size() > 0) {
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_BLOOD_PRESSURE, localization);
            Group groupBloodPressure = new Group(label);
            for (ReportUtil reportUtil : observeBloodPressure) {
                StringTimestamp timestamp = new StringTimestamp(reportUtil.getTimestamp().toString());
                Group group = new Group(timestamp);
                group.getItemList().add(new Item(new StringType(resources.getString(R.string.report_bp_average_label)), new StringValue(decimalFormat.format(reportUtil.getMeanArterialPressure())), new StringUnits(resources.getString(R.string.report_bp_units))));
                group.getItemList().add(new Item(new StringType(resources.getString(R.string.report_bp_diastolic_label)), new StringValue(decimalFormat.format(reportUtil.getDiastolic())), new StringUnits(resources.getString(R.string.report_bp_units))));
                group.getItemList().add(new Item(new StringType(resources.getString(R.string.report_bp_systolic_label)), new StringValue(decimalFormat.format(reportUtil.getSystolic())), new StringUnits(resources.getString(R.string.report_bp_units))));
                group.getItemList().add(new Item(new StringType(resources.getString(R.string.report_pulse_rate)), new StringValue(decimalFormat.format(reportUtil.getPulseRate())), new StringUnits(resources.getString(R.string.report_hr_units))));
                groupBloodPressure.getGroupList().add(group);
            }
            groups.add(groupBloodPressure);
        }
    }

    private void groupLumbarExtensionTraining(List<ReportUtil> observerLumbarExtension, List<Group> groups) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        System.out.println("Entered Lumbar Extension.");
        if (observerLumbarExtension.size() > 0) {
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_LUMBAR_EXTENSION_TRAINING, localization);
            Group groupLumbarExtension = new Group(label);
            for (ReportUtil reportUtil : observerLumbarExtension) {
                StringTimestamp timestamp = new StringTimestamp(reportUtil.getTimestamp().toString());
                Group group = new Group(timestamp);
                System.out.println("Report: " + reportUtil.getLumbarExtensionDuration());
                System.out.println("Report: " + reportUtil.getLumbarExtensionDuration().getSeconds());
                group.getItemList().add(new Item(new StringType(resources.getString(R.string.report_lumbar_training_score_label)), new StringValue(decimalFormat.format(reportUtil.getLumbarExtensionScore())), new StringUnits(resources.getString(R.string.report_lumbar_training_score_units))));
                group.getItemList().add(new Item(new StringType(resources.getString(R.string.report_lumbar_training_repetitions_label)), new StringValue(decimalFormat.format(reportUtil.getLumbarExtensionRepetitions())), new StringUnits("-")));
                group.getItemList().add(new Item(new StringType(resources.getString(R.string.report_lumbar_training_weight_label)), new StringValue(decimalFormat.format(reportUtil.getLumbarExtensionWeight())), new StringUnits(resources.getString(R.string.report_lumbar_training_weight_units))));
                group.getItemList().add(new Item(new StringType(resources.getString(R.string.report_lumbar_training_duration_label)), new StringValue(secondsToString(reportUtil.getLumbarExtensionDuration().getSeconds())), new StringUnits("-")));
                if (reportUtil.getCalories() != null) {
                    group.getItemList().add(new Item(new StringType(resources.getString(R.string.report_calories_label)), new StringValue(decimalFormat.format(reportUtil.getCalories())), new StringUnits(resources.getString(R.string.report_calories_units))));
                }
                groupLumbarExtension.getGroupList().add(group);
            }
            groups.add(groupLumbarExtension);
        }
    }

    private String secondsToString(long value) {
        long seconds = value;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        if (minutes > 0)
            seconds = seconds % 60;

        if (hours > 0) {
            minutes = minutes % 60;
        }

        String result = ((hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m " : "") + (seconds > 0 ? seconds + "s" : "")).trim();

        return result.equals("") ? "0s" : result;
    }

    public void generateWorkTimeReport(ReportRepository reportRepository, LocalDate date, Observer<Report> observerReport) {

        Report report = new Report("Work Time Daily Report", date);
        List<Group> groups = report.getGroups();

        //Simple Records
        reportRepository.getWorkTimeSimpleRecords(date, observerSimpleRecords -> {
            groupSimpleRecords(observerSimpleRecords, groups);
            //Blood Pressure
            reportRepository.getWorkTimeBloodPressure(date, observerBloodPressure -> {
                groupBloodPressure(observerBloodPressure, groups);
                //Lumbar Extension Training
                reportRepository.getWorkTimeLumbarExtensionTraining(date, observerLumbarExtension -> {
                    groupLumbarExtensionTraining(observerLumbarExtension, groups);
                    observerReport.observe(report);
                });
            });
        });

    }

    public void generateNotWorkTimeReport(ReportRepository reportRepository, LocalDate date, Observer<Report> observerReport) {

        Report report = new Report("Not Work Time Daily Report", date);
        List<Group> groups = report.getGroups();

        //Simple Records
        reportRepository.getNotWorkTimeSimpleRecords(date, observerSimpleRecords -> {
            groupSimpleRecords(observerSimpleRecords, groups);
            //Blood Pressure
            reportRepository.getNotWorkTimeBloodPressure(date, observerBloodPressure -> {
                groupBloodPressure(observerBloodPressure, groups);
                //Lumbar Extension Training
                reportRepository.getNotWorkTimeLumbarExtensionTraining(date, observerLumbarExtension -> {
                    groupLumbarExtensionTraining(observerLumbarExtension, groups);
                    observerReport.observe(report);
                });
            });
        });

    }

}

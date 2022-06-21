package pt.uninova.s4h.citizenhub.report;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;
import pt.uninova.s4h.citizenhub.persistence.entity.util.ReportUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.ReportRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DailyReportGenerator {

    private static void groupSimpleRecords(ReportUtil reportUtil, List<Group> groups, MeasurementKindLocalization measurementKindLocalization){

        System.out.println("Entered Simples Daily Reports.");
        if(reportUtil.getCalories()!=null){
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_CALORIES, measurementKindLocalization);
            Group groupCalories = new Group(label);
            groupCalories.getItemList().add(new Item(new StringType("Calories"), new StringValue(reportUtil.getCalories().toString())));
            groups.add(groupCalories);
        }
        if(reportUtil.getDistance()!=null){
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_DISTANCE_SNAPSHOT, measurementKindLocalization);
            Group groupDistance = new Group(label);
            groupDistance.getItemList().add(new Item(new StringType("Distance"), new StringValue(reportUtil.getDistance().toString())));
            groups.add(groupDistance);
        }
        if(reportUtil.getSteps()!=null){
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_STEPS_SNAPSHOT, measurementKindLocalization);
            Group groupSteps = new Group(label);
            groupSteps.getItemList().add(new Item(new StringType("Steps"), new StringValue(reportUtil.getSteps().toString())));
            groups.add(groupSteps);
        }
        if(reportUtil.getMaxHeartRate()!=null && reportUtil.getMinHeartRate()!=null && reportUtil.getAvgHeartRate() != null){
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_HEART_RATE, measurementKindLocalization);
            Group groupHeartRate = new Group(label);
            groupHeartRate.getItemList().add(new Item(new StringType("Max"), new StringValue(reportUtil.getMaxHeartRate().toString())));
            groupHeartRate.getItemList().add(new Item(new StringType("Min"), new StringValue(reportUtil.getMinHeartRate().toString())));
            groupHeartRate.getItemList().add(new Item(new StringType("Avg"), new StringValue(reportUtil.getAvgHeartRate().toString())));
            groups.add(groupHeartRate);
        }
        if(reportUtil.getCorrectPostureDuration()!=null){
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_POSTURE, measurementKindLocalization);
            Group groupPosture = new Group(label);
            groupPosture.getItemList().add(new Item(new StringType("Correct Posture"), new StringValue(((String.valueOf(reportUtil.getCorrectPostureDuration().getSeconds()))))));
            System.out.println("Report: " + reportUtil.getCorrectPostureDuration());
            System.out.println("Report: " + reportUtil.getCorrectPostureDuration().getSeconds());
            if(reportUtil.getWrongPostureDuration()!=null){
                groupPosture.getItemList().add(new Item(new StringType("Wrong Posture"), new StringValue((String.valueOf(reportUtil.getWrongPostureDuration().getSeconds())))));
                System.out.println("Report: " + reportUtil.getWrongPostureDuration());
                System.out.println("Report: " + reportUtil.getWrongPostureDuration().getSeconds());
            }
            else{
                groupPosture.getItemList().add(new Item(new StringType("Wrong Posture"), new StringValue("0")));
            }
            groups.add(groupPosture);
        }
        else {
            if(reportUtil.getWrongPostureDuration()!=null){
                StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_POSTURE, measurementKindLocalization);
                Group groupPosture = new Group(label);
                groupPosture.getItemList().add(new Item(new StringType("Wrong Posture"), new StringValue((String.valueOf(reportUtil.getWrongPostureDuration().getSeconds())))));
                groupPosture.getItemList().add(new Item(new StringType("Correct Posture"), new StringValue("0")));
                groups.add(groupPosture);
            }
        }
    }

    private static void groupBloodPressure(List<ReportUtil> observeBloodPressure, List<Group> groups, MeasurementKindLocalization measurementKindLocalization){
        System.out.println("Entered Blood Pressure.");
        if(observeBloodPressure.size() > 0){
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_BLOOD_PRESSURE, measurementKindLocalization);
            Group groupBloodPressure = new Group(label);
            for(ReportUtil reportUtil : observeBloodPressure) {
                StringTimestamp timestamp = new StringTimestamp(reportUtil.getTimestamp().toString());
                Group group = new Group(timestamp);
                group.getItemList().add(new Item(new StringType("Diastolic"), new StringValue(reportUtil.getDiastolic().toString())));
                group.getItemList().add(new Item(new StringType("Systolic"), new StringValue(reportUtil.getSystolic().toString())));
                group.getItemList().add(new Item(new StringType("Mean"), new StringValue(reportUtil.getMeanArterialPressure().toString())));
                group.getItemList().add(new Item(new StringType("Pulse Rate"), new StringValue(reportUtil.getPulseRate().toString())));
                groupBloodPressure.getGroupList().add(group);
            }
            groups.add(groupBloodPressure);
        }
    }

    private static void groupLumbarExtensionTraining(List<ReportUtil> observerLumbarExtension, List<Group> groups, MeasurementKindLocalization measurementKindLocalization){
        System.out.println("Entered Lumbar Extension.");
        if (observerLumbarExtension.size() > 0) {
            StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_LUMBAR_EXTENSION_TRAINING, measurementKindLocalization);
            Group groupLumbarExtension = new Group(label);
            for (ReportUtil reportUtil : observerLumbarExtension) {
                StringTimestamp timestamp = new StringTimestamp(reportUtil.getTimestamp().toString());
                Group group = new Group(timestamp);
                System.out.println("Report: " + reportUtil.getLumbarExtensionDuration());
                System.out.println("Report: " + reportUtil.getLumbarExtensionDuration().getSeconds());
                group.getItemList().add(new Item(new StringType("Duration"), new StringValue(String.valueOf(reportUtil.getLumbarExtensionDuration().getSeconds()))));
                group.getItemList().add(new Item(new StringType("Score"), new StringValue(reportUtil.getLumbarExtensionScore().toString())));
                group.getItemList().add(new Item(new StringType("Repetitions"), new StringValue(reportUtil.getLumbarExtensionRepetitions().toString())));
                group.getItemList().add(new Item(new StringType("Weight"), new StringValue(reportUtil.getLumbarExtensionWeight().toString())));
                if (reportUtil.getCalories() != null) {
                    group.getItemList().add(new Item(new StringType("Calories"), new StringValue(reportUtil.getCalories().toString())));
                }
                groupLumbarExtension.getGroupList().add(group);
            }
            groups.add(groupLumbarExtension);
        }
    }

    public static void generateWorkTimeReport(ReportRepository reportRepository, LocalDate date, MeasurementKindLocalization measurementKindLocalization, Observer<Report> observerReport) {

        Report report = new Report("Work Time Daily Report", date);
        List<Group> groups = report.getGroups();

        //Simple Records
        reportRepository.getWorkTimeSimpleRecords(date, observerSimpleRecords -> {
            groupSimpleRecords(observerSimpleRecords, groups, measurementKindLocalization);
            //Blood Pressure
            reportRepository.getWorkTimeBloodPressure(date, observerBloodPressure -> {
                groupBloodPressure(observerBloodPressure, groups, measurementKindLocalization);
                //Lumbar Extension Training
                reportRepository.getWorkTimeLumbarExtensionTraining(date, observerLumbarExtension -> {
                    groupLumbarExtensionTraining(observerLumbarExtension, groups, measurementKindLocalization);
                    observerReport.observe(report);
                });
            });
        });

    }

    public static void generateNotWorkTimeReport(ReportRepository reportRepository, LocalDate date, MeasurementKindLocalization measurementKindLocalization, Observer<Report> observerReport) {

        Report report = new Report("Not Work Time Daily Report", date);
        List<Group> groups = report.getGroups();

        //Simple Records
        reportRepository.getNotWorkTimeSimpleRecords(date, observerSimpleRecords -> {
            groupSimpleRecords(observerSimpleRecords, groups, measurementKindLocalization);
            //Blood Pressure
            reportRepository.getNotWorkTimeBloodPressure(date, observerBloodPressure -> {
                groupBloodPressure(observerBloodPressure, groups, measurementKindLocalization);
                //Lumbar Extension Training
                reportRepository.getNotWorkTimeLumbarExtensionTraining(date, observerLumbarExtension -> {
                    groupLumbarExtensionTraining(observerLumbarExtension, groups, measurementKindLocalization);
                    observerReport.observe(report);
                });
            });
        });

    }

}

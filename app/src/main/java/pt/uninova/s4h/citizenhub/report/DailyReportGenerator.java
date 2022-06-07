package pt.uninova.s4h.citizenhub.report;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;
import pt.uninova.s4h.citizenhub.persistence.entity.util.ReportUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.ReportRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DailyReportGenerator {

    public static void generateReport(ReportRepository reportRepository, LocalDate date, MeasurementKindLocalization measurementKindLocalization, Observer<Report> observerReport) {

        Report report = new Report("Daily Report", date);
        List<Group> groups = report.getGroups();

        reportRepository.getNotWorkTimeSimpleRecords(date, observer -> {
            System.out.println("Entered Simples Daily Reports.");

            for (ReportUtil reportUtil : observer) {
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
                    groupHeartRate.getItemList().add(new Item(new StringType("Max Heart Rate"), new StringValue(reportUtil.getMaxHeartRate().toString())));
                    groupHeartRate.getItemList().add(new Item(new StringType("Min Heart Rate"), new StringValue(reportUtil.getMinHeartRate().toString())));
                    groupHeartRate.getItemList().add(new Item(new StringType("Avg Heart Rate"), new StringValue(reportUtil.getAvgHeartRate().toString())));
                    groups.add(groupHeartRate);
                }
                if(reportUtil.getCorrectPostureDuration()!=null){
                    StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_POSTURE, measurementKindLocalization);
                    Group groupPosture = new Group(label);
                    groupPosture.getItemList().add(new Item(new StringType("Correct Posture"), new StringValue(((String.valueOf(reportUtil.getCorrectPostureDuration().getSeconds()))))));
                    System.out.println("Report: " + reportUtil.getCorrectPostureDuration());
                    if(reportUtil.getWrongPostureDuration()!=null){
                        groupPosture.getItemList().add(new Item(new StringType("Wrong Posture"), new StringValue((String.valueOf(reportUtil.getWrongPostureDuration().getSeconds())))));
                        System.out.println("Report: " + reportUtil.getWrongPostureDuration());
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

            //Blood Pressure
            reportRepository.getBloodPressure(date, observeBloodPressure -> {
                System.out.println("Entered Blood Pressure.");
                if(observeBloodPressure.size()>0){
                    StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_BLOOD_PRESSURE, measurementKindLocalization);
                    Group groupBloodPressure = new Group(label);
                    for(ReportUtil reportUtil : observeBloodPressure) {
                        StringTimestamp timestamp = new StringTimestamp(reportUtil.getTimestamp().toString());
                        Group group = new Group(timestamp);
                        group.getItemList().add(new Item(new StringType("Diastolic Blood Pressure"), new StringValue(reportUtil.getDiastolic().toString())));
                        group.getItemList().add(new Item(new StringType("Systolic Blood Pressure"), new StringValue(reportUtil.getSystolic().toString())));
                        group.getItemList().add(new Item(new StringType("Mean Arterial Pressure"), new StringValue(reportUtil.getMeanArterialPressure().toString())));
                        groupBloodPressure.getGroupList().add(group);
                    }
                    groups.add(groupBloodPressure);
                }

                //Lumbar Extension Training
                reportRepository.getLumbarExtensionTraining(date, observeLumbarExtension -> {
                    System.out.println("Entered Lumbar Extension.");
                    if (observeLumbarExtension.size() > 0) {
                        StringMeasurementId label = new StringMeasurementId(Measurement.TYPE_LUMBAR_EXTENSION_TRAINING, measurementKindLocalization);
                        Group groupLumbarExtension = new Group(label);
                        for (ReportUtil reportUtil : observeLumbarExtension) {
                            StringTimestamp timestamp = new StringTimestamp(reportUtil.getTimestamp().toString());
                            Group group = new Group(timestamp);
                            group.getItemList().add(new Item(new StringType("Lumbar Extension Training Duration"), new StringValue(String.valueOf(reportUtil.getLumbarExtensionDuration().getSeconds()))));
                            group.getItemList().add(new Item(new StringType("Lumbar Extension Training Score"), new StringValue(reportUtil.getLumbarExtensionScore().toString())));
                            group.getItemList().add(new Item(new StringType("Lumbar Extension Training Repetitions"), new StringValue(reportUtil.getLumbarExtensionRepetitions().toString())));
                            group.getItemList().add(new Item(new StringType("Lumbar Extension Training Weight"), new StringValue(reportUtil.getLumbarExtensionWeight().toString())));
                            if (reportUtil.getCalories() != null) {
                                group.getItemList().add(new Item(new StringType("Lumbar Extension Training Calories"), new StringValue(reportUtil.getCalories().toString())));
                            }
                            groupLumbarExtension.getGroupList().add(group);
                        }
                        groups.add(groupLumbarExtension);
                    }

                    observerReport.observe(report);
                });
            });
        });

    }

}

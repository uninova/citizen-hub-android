package pt.uninova.s4h.citizenhub.service.work;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.time.Instant;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import pt.uninova.s4h.citizenhub.BuildConfig;
import pt.uninova.s4h.citizenhub.interoperability.DailyPostureReport;
import pt.uninova.s4h.citizenhub.interoperability.SmartBearClient;
import pt.uninova.s4h.citizenhub.persistence.HourlyValue;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.SmartBearUploadDateRecord;
import pt.uninova.s4h.citizenhub.persistence.SmartBearUploadDateRepository;

public class SmartBearUploadWorker extends Worker {

    public SmartBearUploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final boolean enabled = preferences.getBoolean("accounts.smartbear.enabled", false);

        if (enabled) {
            final int patientId = preferences.getInt("accounts.smartbear.id", -1);
            final MeasurementRepository measurementRepository = new MeasurementRepository(getApplicationContext());
            final SmartBearUploadDateRepository smartBearUploadDateRepository = new SmartBearUploadDateRepository(getApplicationContext());

            SmartBearClient client = new SmartBearClient(BuildConfig.SMART_BEAR_URL, BuildConfig.SMART_BEAR_API_KEY);

            smartBearUploadDateRepository.obtainDaysWithData(MeasurementKind.GOOD_POSTURE, value -> {
                for (LocalDate i : value) {
                    if (i.compareTo(LocalDate.now()) < 0) {
                        measurementRepository.obtainHourlySum(i, MeasurementKind.GOOD_POSTURE, value1 -> {
                            measurementRepository.obtainHourlySum(i, MeasurementKind.BAD_POSTURE, value2 -> {
                                int[] good = new int[24];
                                int[] bad = new int[24];

                                for (HourlyValue j : value1) {
                                    good[j.getHour()] = j.getValue().intValue();
                                }

                                for (HourlyValue j : value2) {
                                    bad[j.getHour()] = j.getValue().intValue();
                                }

                                System.out.println("SENDING " + i.toString());

                                DailyPostureReport report = new DailyPostureReport(Integer.toString(patientId), Instant.ofEpochSecond(i.toEpochDay() * 86400), Instant.ofEpochSecond((i.toEpochDay() + 1) * 86400 - 1), good, bad);
                                client.upload(report, value3 -> {
                                    System.out.println(value3.toString());
                                    if (value3.isSuccessful()) {
                                        smartBearUploadDateRepository.add(new SmartBearUploadDateRecord(i));
                                    }
                                });
                            });
                        });
                    }
                }


            });
        }
        return Result.success();
    }
}

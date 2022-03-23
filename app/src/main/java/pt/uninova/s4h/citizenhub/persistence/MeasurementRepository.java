package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import pt.uninova.util.Pair;
import pt.uninova.util.WorkTimeRangeConverter;
import pt.uninova.util.messaging.Observer;
import pt.uninova.util.time.LocalDateInterval;

public class MeasurementRepository {

    private final MeasurementDao measurementDao;
    private final WorkTimeRangeConverter workTimeRangeConverter;
    private final Map<LocalDate, LiveData<Map<MeasurementKind, MeasurementAggregate>>> dailyAggregateMap;

    public MeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        measurementDao = citizenHubDatabase.measurementDao();
        dailyAggregateMap = new HashMap<>();
        workTimeRangeConverter = WorkTimeRangeConverter.getInstance(context.getApplicationContext());
        workTimeRangeConverter.load(context.getApplicationContext());
    }

    public void add(Measurement measurement) {
        System.out.println("MeasurementRepository.add kind=" + measurement.getKind() + " value=" + measurement.getValue());
        CitizenHubDatabase.executorService().execute(() -> {
            measurement.setIsWorking(workTimeRangeConverter.isNowWorkTime());
            measurementDao.insert(measurement);
        });
    }

    public LiveData<Map<MeasurementKind, MeasurementAggregate>> getCurrentDailyAggregate() {
        return getDailyAggregate(LocalDate.now());
    }

    public LiveData<Map<MeasurementKind, MeasurementAggregate>> getDailyAggregate(LocalDate localDate) {
        if (dailyAggregateMap.containsKey(localDate)) {
            return dailyAggregateMap.get(localDate);
        } else {
            final MediatorLiveData<Map<MeasurementKind, MeasurementAggregate>> data = new MediatorLiveData<>();

            dailyAggregateMap.put(localDate, data);

            data.addSource(measurementDao.getAggregateLive(localDate, localDate.plusDays(1)), aggregates -> {
                data.postValue(mapAggregates(aggregates));
            });


            return data;
        }
    }

    private Map<MeasurementKind, MeasurementAggregate> mapAggregates(List<MeasurementAggregate> aggregates) {
        final Map<MeasurementKind, MeasurementAggregate> aggregateMap = new HashMap<>(aggregates.size());

        for (MeasurementAggregate i : aggregates) {
            aggregateMap.put(i.getMeasurementKind(), i);
        }

        return aggregateMap;
    }

    public void obtainDaysWithData(LocalDate from, MeasurementKind measurementKind, Observer<List<LocalDate>> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            final List<LocalDate> localDates = measurementDao.getDaysWithValues(from, measurementKind);

            observer.observe(localDates);
        });
    }

    public void obtainHourlySum(LocalDate date, MeasurementKind measurementKind, Observer<List<HourlyValue>> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            final Instant from = Instant.ofEpochSecond(date.toEpochDay() * 86400 + TimeZone.getDefault().getRawOffset() / 1000);
            final List<HourlyValue> hourlyValues = measurementDao.getHourlySum(from, from.plusSeconds(86400), measurementKind);

            observer.observe(hourlyValues);
        });
    }

    public void obtainDailyAggregate(LocalDate localDate, Observer<Map<MeasurementKind, MeasurementAggregate>> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            final List<MeasurementAggregate> aggregates = measurementDao.getAggregate(localDate, localDate.plusDays(1));

            observer.observe(mapAggregates(aggregates));
        });
    }

    public void obtainDailyAggregateWorkTime(int workTime, LocalDate localDate, Observer<Map<MeasurementKind, MeasurementAggregate>> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            final List<MeasurementAggregate> aggregates = measurementDao.getAggregateWorkTime(localDate, localDate.plusDays(1), workTime);

            observer.observe(mapAggregates(aggregates));
        });
    }

    public void obtainDates(Pair<Integer, Integer> month, Observer<List<LocalDate>> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            final LocalDate from = LocalDate.of(month.getFirst(), month.getSecond(), 1);
            final LocalDate to = LocalDate.of(month.getSecond() == 12 ? month.getFirst() + 1 : month.getFirst(), month.getSecond() == 12 ? 1 : month.getSecond() + 1, 1);
            System.out.println(" MEASUREMENTT OBTAIN DATES " + measurementDao.getDates(from, to));

            observer.observe(measurementDao.getDates(from, to));
        });
    }

}

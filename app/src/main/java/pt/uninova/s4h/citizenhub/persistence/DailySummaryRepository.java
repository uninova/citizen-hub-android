package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.LiveData;
import pt.uninova.util.Observer;
import pt.uninova.util.Pair;

import java.time.LocalDate;
import java.util.List;

public class DailySummaryRepository {

    private final DailySummaryDao dailySummaryDao;

    public DailySummaryRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();
    }

    public LiveData<DailySummary> getCurrentLive() {
        return dailySummaryDao.getCurrentLive();
    }

    public LiveData<LocalDate> getLatestAvailableReportDateLive() {
        return dailySummaryDao.getLatestAvailableReportDateLive();
    }

    public void obtain(int year, int month, int day, Observer<DailySummary> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChange(dailySummaryDao.get(year, month, day)));
    }

    public void obtainCurrent(Observer<DailySummary> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChange(dailySummaryDao.getCurrent()));
    }

    public void obtainDaysWithSummaryInYearMonth(int year, int month, Observer<List<LocalDate>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChange(dailySummaryDao.getDaysWithSummaryInYearMonth(year, month)));
    }

    public void obtainAvailableReportDates(Pair<Integer, Integer> month, Observer<List<LocalDate>> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            observer.onChange(dailySummaryDao.getAvailableReportDates(month, month.getSecond() == 12 ? new Pair<>(month.getFirst() + 1, 1) : new Pair<>(month.getFirst(), month.getSecond() + 1)));
        });
    }

    public void obtainEarliestAvailableReportDate(Observer<LocalDate> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            observer.onChange(dailySummaryDao.getEarliestAvailableReportDate());
        });
    }

}

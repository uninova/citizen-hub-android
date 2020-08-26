package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class DailySummaryRepository {

    private final DailySummaryDao dailySummaryDao;

    public DailySummaryRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();
    }

    public LiveData<DailySummary> getCurrentDailySummaryLive() {
        return dailySummaryDao.getCurrentDailySummaryLive();
    }

    public void obtainCurrentDailySummary(Observer<DailySummary> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChanged(dailySummaryDao.getCurrentDailySummary()));
    }

    public void obtainDailySummary(int year, int month, int day, Observer<DailySummary> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChanged(dailySummaryDao.getDailySummary(year, month, day)));
    }

    public void obtainDaysWithSummaryInYearMonth(int year, int month, Observer<List<Integer>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChanged(dailySummaryDao.getDaysWithSummaryInYearMonth(year, month)));
    }

}

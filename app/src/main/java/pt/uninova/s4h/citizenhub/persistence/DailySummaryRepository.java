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

    public LiveData<DailySummary> getCurrentLive() {
        return dailySummaryDao.getCurrentLive();
    }

    public void obtainCurrent(Observer<DailySummary> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChanged(dailySummaryDao.getCurrent()));
    }

    public void obtain(int year, int month, int day, Observer<DailySummary> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChanged(dailySummaryDao.get(year, month, day)));
    }

    public void obtainDaysWithSummaryInYearMonth(int year, int month, Observer<List<Integer>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChanged(dailySummaryDao.getDaysWithSummaryInYearMonth(year, month)));
    }

}

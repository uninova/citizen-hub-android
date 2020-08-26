package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.LiveData;
import pt.uninova.util.Observer;

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

    public void obtain(int year, int month, int day, Observer<DailySummary> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChange(dailySummaryDao.get(year, month, day)));
    }

    public void obtainCurrent(Observer<DailySummary> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChange(dailySummaryDao.getCurrent()));
    }

    public void obtainDaysWithSummaryInYearMonth(int year, int month, Observer<List<Integer>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChange(dailySummaryDao.getDaysWithSummaryInYearMonth(year, month)));
    }
}

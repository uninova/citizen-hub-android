package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.Date;

public class DailySummaryRepository {

    private final DailySummaryDao dailySummaryDao;

    public DailySummaryRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();
    }

    public LiveData<DailySummary> getCurrentSummary() {
        return dailySummaryDao.getCurrentSummary();
    }

    public LiveData<DailySummary> getDailySummary(Date date) {
        return dailySummaryDao.getDailySummary(date);
    }

    public LiveData<DailySummary> getDailySummary(int year, int month, int day) {
        return dailySummaryDao.getDailySummary(year, month, day);
    }
}

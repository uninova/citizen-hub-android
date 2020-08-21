package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.LiveData;

public class DailySummaryRepository {

    private final DailySummaryDao dailySummaryDao;
    private final LiveData<DailySummary> dailySummary;

    public DailySummaryRepository(Application application) {
        CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getDatabase(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();

        dailySummary = dailySummaryDao.getCurrentSummary();
    }

    public LiveData<DailySummary> getCurrentSummary() {
        return dailySummary;
    }
}

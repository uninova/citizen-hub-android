package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class DailySummaryRepository {

    private final DailySummaryDao dailySummaryDao;

    public DailySummaryRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();
    }

    public void getCurrentDailySummary(Observer<DailySummary> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            observer.onChanged(dailySummaryDao.getCurrentDailySummary());
        });
    }

    public LiveData<DailySummary> getCurrentDailySummaryLive() {
        return dailySummaryDao.getCurrentDailySummaryLive();
    }
}

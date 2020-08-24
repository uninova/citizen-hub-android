package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportDetailRepository {

    private final DailySummaryDao dailySummaryDao;
    private final LiveData<DailySummary> dailySummary;

    public ReportDetailRepository(Application application, int year, int month,int day) {
        CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();

        dailySummary = dailySummaryDao.getDailySummary( year, month, day );
    }

    public LiveData<DailySummary> getDailySummary() {
        return dailySummary;
    }
}

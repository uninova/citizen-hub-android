package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportDetailRepository {

    private final DailySummaryDao dailySummaryDao;

    public ReportDetailRepository(Application application, int year, int month,int day) {
       final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();


    }

    public LiveData<DailySummary> getDailySummary(int year, int month, int day) {
        return dailySummaryDao.getDailySummary( year, month, day );
    }
}

package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportMasterRepository {

    private final DailySummaryDao dailySummaryDao;

    public ReportMasterRepository(Application application, int year, int month) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();

    }

    public LiveData<List<Integer>> getReportMasterSummary(int year, int month) {
        return dailySummaryDao.getDaysWithDataInMonthYear( year, month );
    }
}


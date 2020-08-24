package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportMasterRepository {

    private final DailySummaryDao dailySummaryDao;
    private final LiveData<List<Integer>> reportMasterSummary;

    public ReportMasterRepository(Application application, int year, int month) {
        CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();

        reportMasterSummary = dailySummaryDao.getDaysWithSummaryInYearMonth( year, month );
    }

    public LiveData<List<Integer>> getReportMasterSummary() {
        return reportMasterSummary;
    }
}

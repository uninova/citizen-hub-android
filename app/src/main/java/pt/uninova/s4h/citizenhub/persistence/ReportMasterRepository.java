package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportMasterRepository {

    private final DailySummaryDao dailySummaryDao;

    public ReportMasterRepository(Application application) {
        CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();

    }

    public LiveData<List<Integer>> getReportMasterSummary(int year, int month) {
        return dailySummaryDao.getDaysWithSummaryInYearMonth( year, month );
    }
}

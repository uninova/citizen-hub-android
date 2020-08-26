package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

public class ReportDetailRepository {

    private final DailySummaryDao dailySummaryDao;
    private final DailySummary dailySummary;

    public ReportDetailRepository(Application application, int year, int month, int day) {
        CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        dailySummaryDao = citizenHubDatabase.dailySummaryDao();

        dailySummary = dailySummaryDao.getDailySummary(year, month, day);
    }

    public DailySummary getDailySummary() {
        return dailySummary;
    }
}

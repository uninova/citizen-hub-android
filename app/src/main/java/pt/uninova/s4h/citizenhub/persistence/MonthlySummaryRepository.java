package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class MonthlySummaryRepository {

    private final MonthlySummaryDao monthlySummaryDao;
    private final LiveData<List<Integer>> getDaysWithDataInMonthYear ;

    public MonthlySummaryRepository(Application application, int month, int year) {
        CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        monthlySummaryDao = citizenHubDatabase.monthlySummaryDao();

        getDaysWithDataInMonthYear = monthlySummaryDao.getDaysWithDataInMonthYear(month,year);
    }

    public LiveData<List<Integer>> getMonthlySummary() {
        return getDaysWithDataInMonthYear;
    }
}

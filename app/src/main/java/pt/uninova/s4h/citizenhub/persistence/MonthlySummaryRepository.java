package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;

public class MonthlySummaryRepository {

    private final MonthlySummaryDao monthlySummaryDao;
    private final LiveData<MonthlySummary> monthlySummary ;

    public MonthlySummaryRepository(Application application, Date month) {
        CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        monthlySummaryDao = citizenHubDatabase.monthlySummaryDao();

        monthlySummary = monthlySummaryDao.getMonthlySummary(month);
    }

    public LiveData<MonthlySummary> getMonthlySummary() {
        return monthlySummary;
    }
}

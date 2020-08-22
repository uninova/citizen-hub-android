package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;

public class MonthlySummaryViewModel extends AndroidViewModel {

    private final MonthlySummaryRepository monthlySummaryRepository;
    private final LiveData<MonthlySummary> monthlySummary;

    public MonthlySummaryViewModel(Application application, Date month) {
        super(application);

        monthlySummaryRepository = new MonthlySummaryRepository(application, month);
        monthlySummary = monthlySummaryRepository.getMonthlySummary();
    }

    public LiveData<MonthlySummary> getMonthlySummary() {
        return monthlySummary;
    }
}

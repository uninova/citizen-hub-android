package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class MonthlySummaryViewModel extends AndroidViewModel {

    private final MonthlySummaryRepository monthlySummaryRepository;
    private final LiveData<List<Integer>> getDaysWithSummary ;

    public MonthlySummaryViewModel(Application application, int month) {
        super(application);

        monthlySummaryRepository = new MonthlySummaryRepository(application, month);
        getDaysWithSummary = monthlySummaryRepository.getMonthlySummary();
    }

    public  LiveData<List<Integer>> getMonthlySummary() {
        return getDaysWithSummary;
    }
}

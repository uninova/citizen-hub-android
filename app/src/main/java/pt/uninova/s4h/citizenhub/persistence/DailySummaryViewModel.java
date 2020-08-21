package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DailySummaryViewModel extends AndroidViewModel {

    private final DailySummaryRepository dailySummaryRepository;
    private final LiveData<DailySummary> dailySummary;

    public DailySummaryViewModel(Application application) {
        super(application);

        dailySummaryRepository = new DailySummaryRepository(application);
        dailySummary = dailySummaryRepository.getCurrentSummary();
    }

    public LiveData<DailySummary> getDailySummary() {
        return dailySummary;
    }
}

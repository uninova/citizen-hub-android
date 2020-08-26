package pt.uninova.s4h.citizenhub;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import pt.uninova.s4h.citizenhub.persistence.DailySummary;
import pt.uninova.s4h.citizenhub.persistence.DailySummaryRepository;

public class SummaryViewModel extends AndroidViewModel {

    private final MediatorLiveData<DailySummary> dailySummary;

    public SummaryViewModel(Application application) {
        super(application);

        final DailySummaryRepository dailySummaryRepository = new DailySummaryRepository(application);

        dailySummary = new MediatorLiveData<>();

        dailySummaryRepository.obtainCurrent(dailySummary::postValue);
        dailySummary.addSource(dailySummaryRepository.getCurrentLive(), dailySummary::postValue);
    }

    public LiveData<DailySummary> getDailySummary() {
        return dailySummary;
    }
}

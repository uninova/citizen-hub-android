package pt.uninova.s4h.citizenhub;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import pt.uninova.s4h.citizenhub.persistence.DailySummary;
import pt.uninova.s4h.citizenhub.persistence.DailySummaryRepository;

public class ReportDetailViewModel extends AndroidViewModel {

    private final DailySummaryRepository dailySummaryRepository;
    private final MutableLiveData<DailySummary> dailySummary;

    public ReportDetailViewModel(Application application) {
        super(application);

        dailySummaryRepository = new DailySummaryRepository(application);
        dailySummary = new MutableLiveData<>();
    }

    public LiveData<DailySummary> getDailySummary() {
        return dailySummary;
    }

    public void setParameters(int year, int month, int day) {
        dailySummaryRepository.obtainDailySummary(year, month, day, dailySummary::postValue);
    }
}

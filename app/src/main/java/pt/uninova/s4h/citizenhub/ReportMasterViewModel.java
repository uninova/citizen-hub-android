package pt.uninova.s4h.citizenhub;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import pt.uninova.s4h.citizenhub.persistence.DailySummaryRepository;

import java.util.Calendar;
import java.util.List;

public class ReportMasterViewModel extends AndroidViewModel {

    final private DailySummaryRepository dailySummaryRepository;

    final private MutableLiveData<List<Integer>> days;

    private int month;
    private int year;

    public ReportMasterViewModel(Application application) {
        super(application);

        dailySummaryRepository = new DailySummaryRepository(application);
        days = new MutableLiveData<>();

        final Calendar calendar = Calendar.getInstance();

        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        dailySummaryRepository.obtainDaysWithSummaryInYearMonth(year, month, days::postValue);
    }

    public LiveData<List<Integer>> getDays() {
        return days;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setParameters(int year, int month) {
        this.year = year;
        this.month = month;

        dailySummaryRepository.obtainDaysWithSummaryInYearMonth(year, month, days::postValue);
    }
}

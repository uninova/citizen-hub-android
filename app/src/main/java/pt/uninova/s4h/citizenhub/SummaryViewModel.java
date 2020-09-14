package pt.uninova.s4h.citizenhub;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;

import java.util.Map;

public class SummaryViewModel extends AndroidViewModel {

    private final LiveData<Map<MeasurementKind, MeasurementAggregate>> dailySummary;
    private final MeasurementRepository repository;

    public SummaryViewModel(Application application) {
        super(application);

        repository = new MeasurementRepository(application);

        dailySummary = repository.getCurrentDailyAggregate();
    }

    public LiveData<Map<MeasurementKind, MeasurementAggregate>> getDailySummary() {
        return dailySummary;
    }
}

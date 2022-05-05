package pt.uninova.s4h.citizenhub;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.Map;

import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTraining;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;

public class SummaryViewModel extends AndroidViewModel {

    private final LiveData<Map<MeasurementKind, MeasurementAggregate>> dailySummary;
    private final LiveData<LumbarExtensionTraining> lumbarSummary;
    private final LiveData<LumbarExtensionTraining> newestLumbar;
    
    public SummaryViewModel(Application application) {
        super(application);

        MeasurementRepository repository = new MeasurementRepository(application);
        LumbarExtensionTrainingRepository lumbarRepository = new LumbarExtensionTrainingRepository(application);

        dailySummary = repository.getCurrentDailyAggregate();
        lumbarSummary = lumbarRepository.getLumbarTraining(LocalDate.now());
        newestLumbar =lumbarRepository.getMostRecentLumbarTraining();
    }

    public LiveData<Map<MeasurementKind, MeasurementAggregate>> getDailySummary() {
        return dailySummary;
    }

    public LiveData<LumbarExtensionTraining> getLumbarExtensionTraining() {
        return lumbarSummary;
    }

    public LiveData<LumbarExtensionTraining> getMostRecentLumbar(){
        return newestLumbar;
    }
}

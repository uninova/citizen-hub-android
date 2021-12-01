package pt.uninova.s4h.citizenhub;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTraining;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;

import java.time.LocalDate;
import java.util.Map;

public class SummaryViewModel extends AndroidViewModel {

    private final LiveData<Map<MeasurementKind, MeasurementAggregate>> dailySummary;
    private final LumbarExtensionTraining lumbarSummary;

    private final MeasurementRepository repository;
    private final LumbarExtensionTrainingRepository lumbarRepository;
    public SummaryViewModel(Application application) {
        super(application);

        repository = new MeasurementRepository(application);
        lumbarRepository = new LumbarExtensionTrainingRepository(application);

        dailySummary = repository.getCurrentDailyAggregate();

        lumbarSummary =lumbarRepository.getLumbarTraining(LocalDate.now());
    }

    public LiveData<Map<MeasurementKind, MeasurementAggregate>> getDailySummary() {
        return dailySummary;
    }

    public LumbarExtensionTraining getLumbarSummary(){
        return lumbarSummary;
    }

}

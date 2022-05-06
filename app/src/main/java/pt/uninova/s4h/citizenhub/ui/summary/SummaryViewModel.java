package pt.uninova.s4h.citizenhub.ui.summary;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import pt.uninova.s4h.citizenhub.persistence.BloodPressureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.BloodPressureMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTrainingMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;

public class SummaryViewModel extends AndroidViewModel {

    private final LiveData<Map<MeasurementKind, MeasurementAggregate>> dailySummary;
    private final LiveData<List<LumbarExtensionTrainingMeasurementRecord>> dailyLumbarExtensionTraining;
    private final LiveData<List<BloodPressureMeasurementRecord>> dailyBloodPressureMeasurement;

    public SummaryViewModel(Application application) {
        super(application);

        MeasurementRepository measurementRepository = new MeasurementRepository(application);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(application);
        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(application);

        dailySummary = measurementRepository.getCurrentDailyAggregate();
        dailyLumbarExtensionTraining = lumbarExtensionTrainingRepository.get(LocalDate.now());
        dailyBloodPressureMeasurement = bloodPressureMeasurementRepository.get(LocalDate.now());
    }

    public LiveData<Map<MeasurementKind, MeasurementAggregate>> getDailySummary() {
        return dailySummary;
    }

    public LiveData<List<LumbarExtensionTrainingMeasurementRecord>> getDailyLumbarExtensionTraining() {
        return dailyLumbarExtensionTraining;
    }

    public LiveData<List<BloodPressureMeasurementRecord>> getDailyBloodPressureMeasurement() {
        return dailyBloodPressureMeasurement;
    }
}

package pt.uninova.s4h.citizenhub.ui.summary;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.entity.BloodPressureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.LumbarExtensionTrainingSummary;
import pt.uninova.s4h.citizenhub.persistence.entity.util.WalkingInformation;
import pt.uninova.s4h.citizenhub.persistence.repository.BloodPressureMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.HeartRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.persistence.entity.util.PostureClassificationSum;
import pt.uninova.s4h.citizenhub.persistence.repository.PostureMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.SampleRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.StepsSnapshotMeasurementRepository;

public class SummaryViewModel extends AndroidViewModel {


    private final LiveData<List<BloodPressureMeasurementRecord>> dailyBloodPressureMeasurement;
    private final LiveData<Integer> dailyDataExistence;
    private final LiveData<Double> dailyHeartRate;
    private final LiveData<LumbarExtensionTrainingSummary> dailyLumbarExtensionTraining;
    private final LiveData<List<PostureClassificationSum>> dailyPostureMeasurement;
    private final LiveData<WalkingInformation> dailyWalkingInformation;

    public SummaryViewModel(Application application) {
        super(application);

        BloodPressureMeasurementRepository bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(application);
        HeartRateMeasurementRepository heartRateMeasurementRepository = new HeartRateMeasurementRepository(application);
        LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(application);
        PostureMeasurementRepository postureMeasurementRepository = new PostureMeasurementRepository(application);
        SampleRepository sampleRepository = new SampleRepository(application);
        StepsSnapshotMeasurementRepository stepsSnapshotMeasurementRepository = new StepsSnapshotMeasurementRepository(application);

        final LocalDate now = LocalDate.now();

        dailyLumbarExtensionTraining = lumbarExtensionTrainingRepository.readLatest(now);
        dailyBloodPressureMeasurement = bloodPressureMeasurementRepository.read(now);
        dailyDataExistence = sampleRepository.readCount(now);
        dailyHeartRate = heartRateMeasurementRepository.readAverage(now);
        dailyPostureMeasurement = postureMeasurementRepository.readClassificationSum(now);
        dailyWalkingInformation = stepsSnapshotMeasurementRepository.readLatestWalkingInformation(now);
    }

    public LiveData<LumbarExtensionTrainingSummary> getDailyLumbarExtensionTraining() {
        return dailyLumbarExtensionTraining;
    }

    public LiveData<List<BloodPressureMeasurementRecord>> getDailyBloodPressureMeasurement() {
        return dailyBloodPressureMeasurement;
    }

    public LiveData<Integer> getDailyDataExistence() {
        return dailyDataExistence;
    }

    public LiveData<Double> getDailyHeartRate() {
        return dailyHeartRate;
    }

    public LiveData<List<PostureClassificationSum>> getDailyPostureMeasurement() {
        return dailyPostureMeasurement;
    }

    public LiveData<WalkingInformation> getDailyWalkingInformation() {
        return dailyWalkingInformation;
    }
}

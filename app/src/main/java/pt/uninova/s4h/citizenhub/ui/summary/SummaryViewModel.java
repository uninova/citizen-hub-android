package pt.uninova.s4h.citizenhub.ui.summary;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.data.PostureValue;
import pt.uninova.s4h.citizenhub.persistence.entity.BloodPressureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.LumbarExtensionTrainingSummary;
import pt.uninova.s4h.citizenhub.persistence.entity.util.PostureClassificationSum;
import pt.uninova.s4h.citizenhub.persistence.repository.BloodPressureMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.BreathingRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.CaloriesMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.DistanceMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.HeartRateMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.PostureMeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.SampleRepository;
import pt.uninova.s4h.citizenhub.persistence.repository.StepsMeasurementRepository;

public class SummaryViewModel extends AndroidViewModel {

    static class ActivityData {

        private Double calories;
        private Double distance;
        private Integer steps;

        public ActivityData() {
            this.calories = null;
            this.distance = null;
            this.steps = null;
        }

        public Double getCalories() {
            return calories;
        }

        public Double getDistance() {
            return distance;
        }

        public Integer getSteps() {
            return steps;
        }

        public boolean hasData() {
            return calories != null || distance != null || steps != null;
        }

        public void setCalories(Double calories) {
            this.calories = calories;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public void setSteps(Integer steps) {
            this.steps = steps;
        }
    }

    static class BloodPressureData {

        private Double diastolic;
        private Double systolic;

        public BloodPressureData() {
            diastolic = null;
            systolic = null;
        }

        public Double getDiastolic() {
            return diastolic;
        }

        public Double getSystolic() {
            return systolic;
        }

        public boolean hasData() {
            return diastolic != null && systolic != null;
        }

        public void setDiastolic(Double diastolic) {
            this.diastolic = diastolic;
        }

        public void setSystolic(Double systolic) {
            this.systolic = systolic;
        }
    }

    static class BreathingRateData {

        private Double average;

        public BreathingRateData() {
            average = null;
        }

        public Double getAverage() {
            return average;
        }

        public boolean hasData() {
            return average != null;
        }

        public void setAverage(Double average) {
            this.average = average;
        }
    }

    static class HeartRateData {

        private Double average;

        public HeartRateData() {
            average = null;
        }

        public Double getAverage() {
            return average;
        }

        public boolean hasData() {
            return average != null;
        }

        public void setAverage(Double average) {
            this.average = average;
        }
    }

    static class LumbarExtensionTrainingData {

        private Double calories;
        private Duration duration;
        private Integer repetitions;
        private Integer trainingWeight;

        public LumbarExtensionTrainingData() {
            calories = null;
            duration = null;
            repetitions = null;
            trainingWeight = null;
        }

        public Double getCalories() {
            return calories;
        }

        public Duration getDuration() {
            return duration;
        }

        public Integer getTrainingWeight() {
            return trainingWeight;
        }

        public Integer getRepetitions() {
            return repetitions;
        }

        public void setCalories(Double calories) {
            this.calories = calories;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public void setRepetitions(Integer repetitions) {
            this.repetitions = repetitions;
        }

        public void setTrainingWeight(Integer trainingWeight) {
            this.trainingWeight = trainingWeight;
        }
    }

    static class PostureData {

        private Long correct;
        private Long incorrect;

        public PostureData() {
            this.correct = null;
            this.incorrect = null;
        }

        public Long getCorrect() {
            if (correct == null && incorrect != null) {
                return 0L;
            }

            return correct;
        }

        public Long getIncorrect() {
            if (incorrect == null && correct != null) {
                return 0L;
            }

            return incorrect;
        }

        public boolean hasData() {
            return this.correct != null || this.incorrect != null;
        }

        public void setCorrect(Long correct) {
            this.correct = correct;
        }

        public void setIncorrect(Long incorrect) {
            this.incorrect = incorrect;
        }
    }

    private BloodPressureMeasurementRepository bloodPressureMeasurementRepository;
    private BreathingRateMeasurementRepository breathingRateMeasurementRepository;
    private CaloriesMeasurementRepository caloriesMeasurementRepository;
    private DistanceMeasurementRepository distanceMeasurementRepository;
    private HeartRateMeasurementRepository heartRateMeasurementRepository;
    private LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository;
    private PostureMeasurementRepository postureMeasurementRepository;
    private SampleRepository sampleRepository;
    private StepsMeasurementRepository stepsMeasurementRepository;

    private LiveData<BloodPressureMeasurementRecord> bloodPressureReadQuery;
    private LiveData<Double> breathingRateReadAverageQuery;
    private LiveData<Double> caloriesReadQuery;
    private LiveData<Double> distanceReadQuery;
    private LiveData<Double> heartRateReadAverageQuery;
    private LiveData<LumbarExtensionTrainingSummary> lumbarExtensionTrainingReadLatestQuery;
    private LiveData<List<PostureClassificationSum>> postureReadClassificationSumQuery;
    private LiveData<Integer> sampleReadCountQuery;
    private LiveData<Integer> stepsReadQuery;

    private final MediatorLiveData<ActivityData> activityData;
    private final MediatorLiveData<BloodPressureData> bloodPressureData;
    private final MediatorLiveData<BreathingRateData> breathingRateData;
    private final MediatorLiveData<Boolean> hasData;
    private final MediatorLiveData<HeartRateData> heartRateData;
    private final MediatorLiveData<LumbarExtensionTrainingData> lumbarExtensionTrainingData;
    private final MediatorLiveData<PostureData> postureData;

    private LocalDate now;

    private final Observer<BloodPressureMeasurementRecord> bloodPressureReadQueryObserver = new Observer<BloodPressureMeasurementRecord>() {
        @Override
        public void onChanged(BloodPressureMeasurementRecord value) {
            BloodPressureData data = bloodPressureData.getValue();

            if (data == null) {
                data = new BloodPressureData();
            }

            if (value != null && (!value.getDiastolic().equals(data.getDiastolic()) || !value.getSystolic().equals(data.getSystolic()))) {
                data.setDiastolic(value.getDiastolic());
                data.setSystolic(value.getSystolic());
                bloodPressureData.setValue(data);
            }
        }
    };

    private final Observer<Double> breathingRateReadAverageQueryObserver = new Observer<Double>() {
        @Override
        public void onChanged(Double value) {
            BreathingRateData data = breathingRateData.getValue();

            if (data == null) {
                data = new BreathingRateData();
            }

            if (value != null && !value.equals(data.getAverage())) {
                data.setAverage(value);
                breathingRateData.setValue(data);
            }
        }
    };

    private final Observer<Double> caloriesReadQueryObserver = new Observer<Double>() {
        @Override
        public void onChanged(Double value) {
            ActivityData data = activityData.getValue();

            if (data == null) {
                data = new ActivityData();
            }

            if (value != null && !value.equals(data.getCalories())) {
                data.setCalories(value);
                activityData.setValue(data);
            }
        }
    };

    private final Observer<Double> distanceReadQueryObserver = new Observer<Double>() {
        @Override
        public void onChanged(Double value) {
            ActivityData data = activityData.getValue();

            if (data == null) {
                data = new ActivityData();
            }

            if (value != null && !value.equals(data.getDistance())) {
                data.setDistance(value);
                activityData.setValue(data);
            }

        }
    };

    private final Observer<Double> heartRateReadAverageQueryObserver = new Observer<Double>() {
        @Override
        public void onChanged(Double value) {
            HeartRateData data = heartRateData.getValue();

            if (data == null) {
                data = new HeartRateData();
            }

            if (value != null && !value.equals(data.getAverage())) {
                data.setAverage(value);
            }
        }
    };

    private final Observer<LumbarExtensionTrainingSummary> lumbarExtensionTrainingReadLatestQueryObserver = new Observer<LumbarExtensionTrainingSummary>() {
        @Override
        public void onChanged(LumbarExtensionTrainingSummary value) {
            LumbarExtensionTrainingData data = lumbarExtensionTrainingData.getValue();

            if (data == null) {
                data = new LumbarExtensionTrainingData();
            }

            if (value != null) {
                data.setCalories(value.getCalories());
                data.setDuration(value.getDuration());
                data.setRepetitions(value.getRepetitions());
                data.setTrainingWeight(value.getWeight());
            }
        }
    };

    private final Observer<List<PostureClassificationSum>> postureReadClassificationSumQueryObserver = new Observer<List<PostureClassificationSum>>() {
        @Override
        public void onChanged(List<PostureClassificationSum> value) {
            PostureData data = postureData.getValue();

            if (data == null) {
                data = new PostureData();
            }

            for (PostureClassificationSum i : value) {
                if (i.getClassification() == PostureValue.CLASSIFICATION_CORRECT) {
                    final Long val = i.getDuration().toMillis() / 1000;

                    if (!val.equals(data.getCorrect())) {
                        data.setCorrect(val);
                    }
                } else if (i.getClassification() == PostureValue.CLASSIFICATION_INCORRECT) {
                    final Long val = i.getDuration().toMillis() / 1000;

                    if (!val.equals(data.getIncorrect())) {
                        data.setIncorrect(val);
                    }
                }
            }

            postureData.setValue(data);
        }
    };

    private final Observer<Integer> sampleReadCountQueryObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            hasData.setValue(integer > 0);
        }
    };

    private final Observer<Integer> stepsReadQueryObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer value) {
            ActivityData data = activityData.getValue();

            if (data == null) {
                data = new ActivityData();
            }

            if (value != null && !value.equals(data.getSteps())) {
                data.setSteps(value);
                activityData.setValue(data);
            }
        }
    };

    public SummaryViewModel(Application application) {
        super(application);

        this.now = null;

        this.bloodPressureMeasurementRepository = new BloodPressureMeasurementRepository(application);
        this.breathingRateMeasurementRepository = new BreathingRateMeasurementRepository(application);
        this.caloriesMeasurementRepository = new CaloriesMeasurementRepository(application);
        this.distanceMeasurementRepository = new DistanceMeasurementRepository(application);
        this.heartRateMeasurementRepository = new HeartRateMeasurementRepository(application);
        this.lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(application);
        this.postureMeasurementRepository = new PostureMeasurementRepository(application);
        this.sampleRepository = new SampleRepository(application);
        this.stepsMeasurementRepository = new StepsMeasurementRepository(application);

        this.activityData = new MediatorLiveData<>();
        this.bloodPressureData = new MediatorLiveData<>();
        this.breathingRateData = new MediatorLiveData<>();
        this.hasData = new MediatorLiveData<>();
        this.heartRateData = new MediatorLiveData<>();
        this.lumbarExtensionTrainingData = new MediatorLiveData<>();
        this.postureData = new MediatorLiveData<>();
    }

    public LiveData<ActivityData> getActivityData() {
        return activityData;
    }

    public LiveData<BloodPressureData> getBloodPressureData() {
        return bloodPressureData;
    }

    public LiveData<BreathingRateData> getBreathingRateData() {
        return breathingRateData;
    }

    public LiveData<HeartRateData> getHeartRateData() {
        return heartRateData;
    }

    public LiveData<LumbarExtensionTrainingData> getLumbarExtensionTrainingData() {
        return lumbarExtensionTrainingData;
    }

    public LiveData<PostureData> getPostureData() {
        return postureData;
    }

    public LiveData<Boolean> hasData() {
        return hasData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        bloodPressureMeasurementRepository = null;
        breathingRateMeasurementRepository = null;
        caloriesMeasurementRepository = null;
        distanceMeasurementRepository = null;
        heartRateMeasurementRepository = null;
        lumbarExtensionTrainingRepository = null;
        postureMeasurementRepository = null;
        sampleRepository = null;
        stepsMeasurementRepository = null;
    }

    public void refreshData() {
        final LocalDate now = LocalDate.now();

        if (!now.equals(this.now)) {
            updateQueries(now);
            this.now = now;
        }
    }

    private void updateQueries(LocalDate now) {
        activityData.removeSource(caloriesReadQuery);
        activityData.removeSource(distanceReadQuery);
        activityData.removeSource(stepsReadQuery);
        activityData.setValue(null);
        caloriesReadQuery = caloriesMeasurementRepository.getCaloriesAllTypes(now);
        distanceReadQuery = distanceMeasurementRepository.getDistanceAllTypes(now);
        stepsReadQuery = stepsMeasurementRepository.getStepsAllTypes(now);
        activityData.addSource(caloriesReadQuery, caloriesReadQueryObserver);
        activityData.addSource(distanceReadQuery, distanceReadQueryObserver);
        activityData.addSource(stepsReadQuery, stepsReadQueryObserver);

        bloodPressureData.removeSource(bloodPressureReadQuery);
        bloodPressureData.setValue(null);
        bloodPressureReadQuery = bloodPressureMeasurementRepository.readLatest(now);
        bloodPressureData.addSource(bloodPressureReadQuery, bloodPressureReadQueryObserver);

        breathingRateData.removeSource(breathingRateReadAverageQuery);
        breathingRateData.setValue(null);
        breathingRateReadAverageQuery = breathingRateMeasurementRepository.readAverage(now);
        breathingRateData.addSource(breathingRateReadAverageQuery, breathingRateReadAverageQueryObserver);

        heartRateData.removeSource(heartRateReadAverageQuery);
        heartRateData.setValue(null);
        heartRateReadAverageQuery = heartRateMeasurementRepository.readAverage(now);
        heartRateData.addSource(heartRateReadAverageQuery, heartRateReadAverageQueryObserver);

        lumbarExtensionTrainingData.removeSource(lumbarExtensionTrainingReadLatestQuery);
        lumbarExtensionTrainingData.setValue(null);
        lumbarExtensionTrainingReadLatestQuery = lumbarExtensionTrainingRepository.readLatest(now);
        lumbarExtensionTrainingData.addSource(lumbarExtensionTrainingReadLatestQuery, lumbarExtensionTrainingReadLatestQueryObserver);

        postureData.removeSource(postureReadClassificationSumQuery);
        postureData.setValue(null);
        postureReadClassificationSumQuery = postureMeasurementRepository.readClassificationSum(now);
        postureData.addSource(postureReadClassificationSumQuery, postureReadClassificationSumQueryObserver);

        hasData.removeSource(sampleReadCountQuery);
        sampleReadCountQuery = sampleRepository.readCount(now);
        hasData.addSource(sampleReadCountQuery, sampleReadCountQueryObserver);
    }

    // ** Not here
    public ChartMarkerView getChartViewMarker() {
        return new ChartMarkerView(getApplication().getBaseContext(), R.layout.fragment_summary_detail_line_chart_marker);
    }
}

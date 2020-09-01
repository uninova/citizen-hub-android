package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MeasurementViewModel extends AndroidViewModel {

    final MeasurementRepository measurementRepository;

    private final LiveData<List<Measurement>> allMeasurements;

    public MeasurementViewModel(@NonNull Application application) {
        super(application);
        measurementRepository = new MeasurementRepository(application);
        allMeasurements = measurementRepository.getAllMeasurementsLive();
    }

    public void insert(Measurement measurement) {
        measurementRepository.add(measurement);
    }

    public LiveData<List<Measurement>> getAllMeasurements() {
        return allMeasurements;
    }

}
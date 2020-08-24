package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MeasurementViewModel extends AndroidViewModel {
    private MeasurementRepository repository;
    private LiveData<List<Measurement>> allMeasurements;

    public MeasurementViewModel(@NonNull Application application) {
        super(application);
        repository = new MeasurementRepository(application);
        allMeasurements = repository.getAllMeasurements();
    }

    public void insert(Measurement measurement) {
        repository.insert(measurement);
    }

    public void update(Measurement measurement) {
        repository.update(measurement);
    }

    public void delete(Measurement measurement) {
        repository.delete(measurement);
    }

    public void deleteAllMeasurements() {
        repository.deleteAll();
    }

    public LiveData<List<Measurement>> getAllMeasurements() {
        return allMeasurements;
    }

}
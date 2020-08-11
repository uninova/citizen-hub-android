package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SourceViewModel extends AndroidViewModel {
    private SourceRepository repository;
    private LiveData<List<Source>> allSources;

    public SourceViewModel(@NonNull Application application) {
        super(application);
        repository = new SourceRepository(application);
        allSources = repository.getAllSources();
    }

    public void insert(Source source) {
        repository.insert(source);
    }

    public void update(Source source) {
        repository.update(source);
    }

    public void delete(Source source) {
        repository.delete(source);
    }

    public void deleteAllSources() {
        repository.deleteAll();
    }

    public LiveData<List<Source>> getAllSources() {
        return allSources;
    }

}
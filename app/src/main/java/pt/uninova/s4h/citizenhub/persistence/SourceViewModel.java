package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SourceViewModel extends AndroidViewModel {
    final SourceRepository sourceRepository;
    private final LiveData<List<Source>> allSources;

    public SourceViewModel(@NonNull Application application) {
        super(application);
        sourceRepository = new SourceRepository(application);
        allSources = sourceRepository.getAllSources();
    }

    public void insert(Source source) {
        sourceRepository.insert(source);
    }

    public void update(Source source) {
        sourceRepository.update(source);
    }

    public void delete(Source source) {
        sourceRepository.delete(source);
    }

    public void deleteAllSources() {
        sourceRepository.deleteAll();
    }

    public LiveData<List<Source>> getAllSources() {
        return allSources;
    }

}
package pt.uninova.s4h.citizenhub;

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
        allSources = sourceRepository.getAllSourcesLive();
    }

    public LiveData<List<Source>> getAllSourcesLive() {
        return allSources;
    }

}
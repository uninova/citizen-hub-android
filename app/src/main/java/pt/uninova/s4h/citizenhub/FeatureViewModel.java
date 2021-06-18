package pt.uninova.s4h.citizenhub;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.persistence.FeatureRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class FeatureViewModel extends AndroidViewModel {
    private final MutableLiveData<Feature> feature;
    private final LiveData<List<Feature>> featureList;
    final private FeatureRepository featureRepository;

    public FeatureViewModel(Application application) {
        super(application);
        featureRepository = new FeatureRepository(application);
        feature = new MutableLiveData<>();
        featureList = featureRepository.getAllLive();
    }

    public LiveData<List<Feature>> getAll() {
        return featureList;
    }

    public List<MeasurementKind> getKindsFromDevice(String device_address) {
        return featureRepository.getKindsFromDevice(device_address);
    }

    public MutableLiveData<Feature> getSelectedFeature() {
        return feature;
    }


    public void setFeature(Feature feature) {
        this.feature.postValue(feature);
    }

    public void apply(Feature feature) {
        featureRepository.add(feature);
    }

    public void delete(Feature feature) {
        featureRepository.remove(feature);
    }
}

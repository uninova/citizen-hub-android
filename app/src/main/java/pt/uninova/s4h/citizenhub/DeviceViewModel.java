package pt.uninova.s4h.citizenhub;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.persistence.FeatureRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;
import pt.uninova.util.messaging.Observer;

public class DeviceViewModel extends AndroidViewModel {
    private final MutableLiveData<Device> device;
    private List<Device> deviceList;
    final private DeviceRepository deviceRepository;

    private final MutableLiveData<Feature> feature;
    private final LiveData<List<Feature>> featureList;
    final private FeatureRepository featureRepository;


    public DeviceViewModel(Application application) {
        super(application);
        deviceRepository = new DeviceRepository(application);
        device = new MutableLiveData<>();
        deviceRepository.obtainAll(value -> deviceList = value);


        featureRepository = new FeatureRepository(application);
        feature = new MutableLiveData<>();
        featureList = featureRepository.getAllLive();
    }

    public List<FeatureListItem> getSupportedFeatures() {

        List<FeatureListItem> supportedFeaturesList = new ArrayList<>();
        for (MeasurementKind feature :)){
            if (MeasurementKind.find(feature.getId()) != null) {
                supportedFeaturesList.add(new FeatureListItem(feature));
            }
        }
        return supportedFeaturesList;
    }


    public List<FeatureListItem> getEnabledFeatures() {
        List<FeatureListItem> featureList = new ArrayList<>();
        obtainKindsFromDevice(device.getValue().getAddress(), enabledFeatures -> {

            final Set<MeasurementKind> enabledFeaturesSet = new HashSet<>(enabledFeatures);

            for (MeasurementKind feature : agentOrchestrator.getSupportedFeatures(device.getValue().getName())) {

                featureList.add(new FeatureListItem(feature, enabledFeaturesSet.contains(feature)));

            }
        });
        return featureList;

    }


    public LiveData<List<Feature>> getAll() {
        return featureList;
    }

    public void obtainKindsFromDevice(String device_address, Observer<List<MeasurementKind>> observer) {
        featureRepository.obtainKindsFromDevice(device_address, observer);
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


    public List<Device> getDevices() {
        return deviceList;
    }

    public boolean isDevicePaired(Device device) {
        List<Device> pairedDevices = deviceList;
        if (pairedDevices != null) {
            return deviceList.contains(device);
        }
        return false;
    }

    public MutableLiveData<Device> getSelectedDevice() {
        return device;
    }

    public Agent getSelectedAgent() {
        //  Agent agent = ((CitizenHubServiceBound) activity).getService().getAgentOrchestrator().getDeviceAgentMap().get(device.getValue());

        Agent agent = ((CitizenHubServiceBound) getApplication()).getService().getAgentOrchestrator().getDeviceAgentMap().get(device.getValue());
        if (agent == null) throw new NullPointerException();
        return agent;
    }

    public void setDevice(Device device) {
        this.device.postValue(device);
    }

    private void setDeviceList(List<Device> deviceList) {
        this.deviceList.post;
    }

    public void getDeviceFeatures(Device device) {
        ((CitizenHubServiceBound) getApplication()).getService().getAgentOrchestrator().getDeviceAgentMap().get(device).getSupportedMeasurements();
    }

    public void enableDeviceFeature(MeasurementKind measurementKind) {
        Objects.requireNonNull(((CitizenHubServiceBound) getApplication()).getService().getAgentOrchestrator().getDeviceAgentMap().get(device.getValue())).enableMeasurement(measurementKind);
    }

    public void apply() {
        deviceRepository.add(device.getValue());
    }

    public void delete(Device device) {
        deviceRepository.remove(device);
        //TODO
    }
}
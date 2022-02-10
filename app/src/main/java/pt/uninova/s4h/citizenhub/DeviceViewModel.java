package pt.uninova.s4h.citizenhub;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRecord;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.persistence.FeatureRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.StateKind;
import pt.uninova.util.messaging.Observer;

public class DeviceViewModel extends AndroidViewModel {

    private final DeviceRepository deviceRepository;
    private final FeatureRepository featureRepository;

    private final MutableLiveData<Device> device;
    private final MutableLiveData<Feature> feature;
    private final LiveData<List<Feature>> featureList;

    private List<Device> deviceList;

    public DeviceViewModel(Application application) {
        super(application);

        deviceRepository = new DeviceRepository(application);
        featureRepository = new FeatureRepository(application);

        device = new MutableLiveData<>();
        feature = new MutableLiveData<>();
        featureList = featureRepository.getAllLive();

        deviceList = new LinkedList<>();

        deviceRepository.obtainAll((List<DeviceRecord> value) -> {
            for (DeviceRecord i : value) {
                deviceList.add(new Device(i.getAddress(), i.getName(), i.getConnectionKind()));
            }
        });
    }

    public List<FeatureListItem> getSupportedFeatures(AgentOrchestrator agentOrchestrator) {
        final List<FeatureListItem> supportedFeaturesList = new ArrayList<>();

        final Agent agent = agentOrchestrator.getAgent(device.getValue());

        for (MeasurementKind kind : agent.getSupportedMeasurements()) {
            supportedFeaturesList.add(new FeatureListItem(kind));
        }

        return supportedFeaturesList;
    }

    public List<FeatureListItem> getEnabledFeatures(AgentOrchestrator agentOrchestrator) {
        List<FeatureListItem> featureList = new ArrayList<>();
        obtainKindsFromDevice(device.getValue().getAddress(), enabledFeatures -> {

            final Set<MeasurementKind> enabledFeaturesSet = new HashSet<>(enabledFeatures);

            for (MeasurementKind feature : getSelectedAgent(agentOrchestrator).getSupportedMeasurements()) {//(device.getValue().getName())) {

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

    public void apply(Feature feature, AgentOrchestrator agentOrchestrator, MeasurementRepository measurementRepository) {
        getSelectedAgent(agentOrchestrator).enableMeasurement(feature.getKind());
        featureRepository.add(feature);
    }

    public void delete(Feature feature, AgentOrchestrator agentOrchestrator) {
        getSelectedAgent(agentOrchestrator).disableMeasurement(feature.getKind());
        featureRepository.remove(feature);
    }

    public List<Device> getDevices() {
        return deviceList;
    }

    public boolean isDevicePaired(Device device) {
        //TODO ir buscar ao orchestrator
        List<Device> pairedDevices = deviceList;
        if (pairedDevices != null) {
            return deviceList.contains(device);
        }
        return false;
    }

    public MutableLiveData<Device> getSelectedDevice() {
        return device;
    }


    public Agent getSelectedAgent(AgentOrchestrator agentOrchestrator) {
        Agent agent = agentOrchestrator.getAgent(device.getValue());

        if (agent == null) throw new NullPointerException();
        return agent;
    }

    public void setDevice(Device device) {
        this.device.postValue(device);
    }

    private DeviceRecord toRecord(Device device) {
        return new DeviceRecord(device.getName(), device.getAddress(), device.getConnectionKind(), StateKind.ACTIVE, "");
    }

    public void apply() {
        deviceRepository.add(toRecord(device.getValue()));
        deviceList.add(device.getValue());
    }

    public void delete(Device device) {
        deviceRepository.remove(toRecord(device));
        deviceList.remove(device);
        //TODO
    }
}
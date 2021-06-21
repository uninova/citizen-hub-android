package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceConfigurationFragment extends Fragment {

    protected Button connectDevice;
    protected Button deleteDevice;
    protected Button updateDevice;
    protected TextView nameDevice;
    protected TextView addressDevice;
    protected ListView listViewFeatures;
    protected List<FeatureListItem> featuresList;
    private List<MeasurementKind> enabledFeatures;
    //    private MutableLiveData<List<MeasurementKind>> enabledFeatures;
    private FeatureViewModel featuresModel;


    protected void setupViews(View result) {
        nameDevice = result.findViewById(R.id.textConfigurationDeviceName);
        addressDevice = result.findViewById(R.id.textConfigurationDeviceAddress);
        listViewFeatures = result.findViewById(R.id.listViewFeature);
    }

    protected void setupText() {
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();

        nameDevice.setText(getString(R.string.fragment_configuration_text_view_name, device.getName()));
        addressDevice.setText(getString(R.string.fragment_configuration_text_view_address, device.getAddress()));
    }

    protected void getSupportedFeatures(String deviceName) {
        final AgentOrchestrator agentOrchestrator = new AgentOrchestrator(((CitizenHubServiceBound) requireActivity()).getService());

        featuresList = new ArrayList<>();

        for (MeasurementKind feature : agentOrchestrator.getSupportedFeatures(deviceName)) {
            if (MeasurementKind.find(feature.getId()) != null) {
                featuresList.add(new FeatureListItem(feature));
            }
        }

        listViewFeatures.setAdapter(new FeatureListAdapter(requireContext(), featuresList));
    }

    protected void loadFeatureState() {
        final AgentOrchestrator agentOrchestrator = new AgentOrchestrator(((CitizenHubServiceBound) requireActivity()).getService());

        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();
        FeatureViewModel featuresModel = new ViewModelProvider(requireActivity()).get(FeatureViewModel.class);

        enabledFeatures = featuresModel.getKindsFromDevice(device.getAddress());
        if (enabledFeatures != null && enabledFeatures.size() != 0) {
            for (MeasurementKind feature : enabledFeatures) {
                if (MeasurementKind.find(feature.getId()) != null) {
                    featuresList.add(new FeatureListItem(feature, true));
                }
            }
        } else {
            featuresList = new ArrayList<>();
            for (MeasurementKind feature : agentOrchestrator.getSupportedFeatures(device.getName())) {
                if (MeasurementKind.find(feature.getId()) != null) {
                    featuresList.add(new FeatureListItem(feature, false));
                }
            }
        }
        listViewFeatures.setAdapter(new FeatureListAdapter(requireContext(), featuresList));

    }


    protected void saveFeaturesChosen() {
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();
        final FeatureViewModel featureViewModel = new ViewModelProvider(requireActivity()).get(FeatureViewModel.class);
        for (int i = 0; i < listViewFeatures.getAdapter().getCount(); i++) {
            if (listViewFeatures.getAdapter().isEnabled(i)) {
                assert device != null;
//                featureViewModel.setFeature(new Feature(device.getAddress(),featuresList.get(i).getMeasurementKind()));
                featureViewModel.apply((new Feature(device.getAddress(), featuresList.get(i).getMeasurementKind())));
            } else {
                assert device != null;
                featureViewModel.delete(new Feature(device.getAddress(), featuresList.get(i).getMeasurementKind()));
            }
        }
    }

    protected void setFeaturesState(Agent agent) {
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();
        final FeatureViewModel featureViewModel = new ViewModelProvider(requireActivity()).get(FeatureViewModel.class);
        for (int i = 0; i < listViewFeatures.getAdapter().getCount(); i++) {
            if (listViewFeatures.getAdapter().isEnabled(i)) {
                agent.enableMeasurement(featuresList.get(i).getMeasurementKind());
                assert device != null;
//                featureViewModel.setFeature(new Feature(device.getAddress(),featuresList.get(i).getMeasurementKind()));
                featureViewModel.apply(new Feature(device.getAddress(), featuresList.get(i).getMeasurementKind()));

                // featureRepository.add(device.getAddress(), featuresList.get(i).getMeasurementKind());

            } else {
                agent.disableMeasurement(featuresList.get(i).getMeasurementKind());
                assert device != null;
                featureViewModel.delete(new Feature(device.getAddress(), featuresList.get(i).getMeasurementKind()));
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();
        featuresModel = new ViewModelProvider(requireActivity()).get(FeatureViewModel.class);
        this.featuresList = new ArrayList<>();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadFeatureState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanList();
    }

    private void onFeatureUpdate(List<Feature> features) {
        cleanList();
        for (Feature feature : features) {
            featuresList.add(new FeatureListItem(feature.getKind(), true));
        }
    }

    private void cleanList() {
        featuresList = new ArrayList<>();
    }
}

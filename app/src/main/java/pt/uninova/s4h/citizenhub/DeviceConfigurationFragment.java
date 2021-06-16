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
import pt.uninova.s4h.citizenhub.persistence.FeatureRepository;
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
        final AgentOrchestrator agentOrchestrator = new AgentOrchestrator(((CitizenHubServiceBound) getActivity()).getService());

        featuresList = new ArrayList<>();

        for (MeasurementKind feature : agentOrchestrator.getSupportedFeatures(deviceName)) {
            if (MeasurementKind.find(feature.getId()) != null) {
                featuresList.add(new FeatureListItem(feature));
            }
        }

        listViewFeatures.setAdapter(new FeatureListAdapter(requireContext(), featuresList));
    }

    protected void loadFeatureState() {
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();
        FeatureRepository featureRepository = new FeatureRepository(requireActivity().getApplication());
        List<MeasurementKind> enabledFeatures = featureRepository.getAllFeatures(device.getAddress());
        for (int i = 0; i < listViewFeatures.getAdapter().getCount(); i++) {
            listViewFeatures.setItemChecked(i, enabledFeatures.contains(listViewFeatures.getAdapter().getItem(i)));
        }
    }

    protected void setFeaturesState(Agent agent) {
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();
        FeatureRepository featureRepository = new FeatureRepository(requireActivity().getApplication());

        for (int i = 0; i < listViewFeatures.getAdapter().getCount(); i++) {
            if (listViewFeatures.getAdapter().isEnabled(i)) {
                agent.enableMeasurement(featuresList.get(i).getMeasurementKind());
                assert device != null;
                featureRepository.add(device.getAddress(), featuresList.get(i).getMeasurementKind());

            } else {
                agent.disableMeasurement(featuresList.get(i).getMeasurementKind());
                assert device != null;
                featureRepository.remove(device.getAddress(), featuresList.get(i).getMeasurementKind());
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}

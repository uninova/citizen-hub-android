package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class DeviceConfigurationFragment extends Fragment {

    protected Button connectDevice;
    protected Button deleteDevice;
    protected Button updateDevice;
    protected Button advancedDevice;
    protected Button advancedOKDevice;
    protected ViewStub deviceAdvancedSettings;
    protected View deviceAdvancedSettingsInflated;
    protected TextView nameDevice;
    protected TextView addressDevice;
    protected ListView listViewFeatures;
    private DeviceViewModel model;

    public DeviceConfigurationFragment() {
        super();
    }

    protected void setupViews(View result) {
        nameDevice = result.findViewById(R.id.textConfigurationDeviceName);
        addressDevice = result.findViewById(R.id.textConfigurationDeviceAddress);
        listViewFeatures = result.findViewById(R.id.listViewFeature);
    }

    protected void setupText() {
        final Device device = model.getSelectedDevice().getValue();

        nameDevice.setText(getString(R.string.fragment_configuration_text_view_name, device.getName()));
        addressDevice.setText(getString(R.string.fragment_configuration_text_view_address, device.getAddress()));
    }

    protected List<FeatureListItem> getSupportedFeatures() {
        final Device device = model.getSelectedDevice().getValue();
        final List<FeatureListItem> featureListItems = new LinkedList<>();
        final Agent agent = model.getAgentOrchestrator().getValue().getAgent(device);

        if (agent != null) {
            final Set<MeasurementKind> measurementKindSet = agent.getEnabledMeasurements();

            for (MeasurementKind i : agent.getSupportedMeasurements()) {
                featureListItems.add(new FeatureListItem(i, measurementKindSet.contains(i)));
            }
        }

        return featureListItems;
    }

    protected void loadSupportedFeatures() {
        FeatureListAdapter adapter = new FeatureListAdapter(requireActivity(), getSupportedFeatures());
        listViewFeatures.setAdapter(adapter);
        adapter.updateResults(getSupportedFeatures());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
    }

    protected void saveFeaturesChosen() {
        final AgentOrchestrator agentOrchestrator = model.getAgentOrchestrator().getValue();
        final Agent agent = agentOrchestrator.getAgent(model.getSelectedDevice().getValue());
        final ListAdapter adapter = listViewFeatures.getAdapter();

        for (int i = 0; i < adapter.getCount(); i++) {
            final FeatureListItem item = (FeatureListItem) listViewFeatures.getAdapter().getItem(i);

            if (item.isActive()) {
                agent.enableMeasurement(item.getMeasurementKind());
            } else {
                agent.disableMeasurement(item.getMeasurementKind());
            }
        }
    }
}

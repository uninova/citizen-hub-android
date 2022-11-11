package pt.uninova.s4h.citizenhub.ui.devices;

import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;

public class DeviceConfigurationFragment extends Fragment {

    protected TextView nameDevice;
    protected TextView addressDevice;
    protected ListView listViewFeatures;
    DeviceViewModel model;

    private MeasurementKindLocalization measurementKindLocalization;

    public DeviceConfigurationFragment() {
        super();
    }

    protected void setupViews(View result) {
        nameDevice = result.findViewById(R.id.textConfigurationDeviceNameValue);
        addressDevice = result.findViewById(R.id.textConfigurationAddressValue
        );
        listViewFeatures = result.findViewById(R.id.listViewFeature);
    }

    protected void setupText() {
        final Device device = model.getSelectedDevice().getValue();
        if (device != null) {
            nameDevice.setText(device.getName());
            addressDevice.setText(device.getAddress());
        }
    }

    protected List<FeatureListItem> getSupportedFeatures() {
        final List<FeatureListItem> featureListItems = new LinkedList<>();
        final Agent agent = model.getSelectedDeviceAgent();
        if (agent != null) {

            if (agent.getState() != 1 && agent.getEnabledMeasurements() != null) {

                for (int i : agent.getSupportedMeasurements()) {
                    featureListItems.add(new FeatureListItem(i, measurementKindLocalization.localize(i), agent.getEnabledMeasurements().contains(i)));
                }
            } else {
                final Set<Integer> measurementKindSet = agent.getEnabledMeasurements();

                for (int i : agent.getSupportedMeasurements()) {
                    featureListItems.add(new FeatureListItem(i, measurementKindLocalization.localize(i), measurementKindSet.contains(i)));
                }
            }
        }

        return featureListItems;
    }

    protected List<String> getLabelList(Agent agent) {
        List<String> labelList = new ArrayList<>();
        for (int i : agent.getSupportedMeasurements()) {
            labelList.add(measurementKindLocalization.localize(i));
        }
        return labelList;
    }

    protected void loadSupportedFeatures() {
        if (model.getSelectedDeviceAgent() != null) {
            FeatureListAdapter adapter = new FeatureListAdapter(requireActivity(), getSupportedFeatures(), model.getSelectedDeviceAgent().getState() == 1, model.getSelectedDeviceAgent());

            listViewFeatures.setAdapter(adapter);
            adapter.updateResults(getSupportedFeatures());

        } else {
            FeatureListAdapter adapter = new FeatureListAdapter(requireActivity(), getSupportedFeatures(), model.getSelectedDeviceAgent());
            listViewFeatures.setAdapter(adapter);
            adapter.updateResults(getSupportedFeatures());

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        measurementKindLocalization = new MeasurementKindLocalization(requireContext());

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

    }

    protected void saveFeaturesChosen() {
        final Agent agent = model.getSelectedDeviceAgent();
        final ListAdapter adapter = listViewFeatures.getAdapter();

        for (int i = 0; i < adapter.getCount(); i++) {
            final FeatureListItem item = (FeatureListItem) listViewFeatures.getAdapter().getItem(i);

            int k = item.getFeatureId();

            if (item.isActive()) {
                if (!agent.getEnabledMeasurements().contains(k)) {
                    agent.enableMeasurement(k);
                }
            } else {
                if (agent.getEnabledMeasurements().contains(k)) {
                    agent.disableMeasurement(k);
                }
            }
        }
    }
}

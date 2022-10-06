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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;

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

    private MeasurementKindLocalization measurementKindLocalization;

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
        final List<FeatureListItem> featureListItems = new LinkedList<>();
        final Agent agent = model.getSelectedDeviceAgent();

        if (agent != null) {

            if(agent.getState()!=1){

                for (int i : agent.getSupportedMeasurements()) {
                    featureListItems.add(new FeatureListItem(i, measurementKindLocalization.localize(i), false));
                }
            }
            else {
                final Set<Integer> measurementKindSet = agent.getEnabledMeasurements();

                for (int i : agent.getSupportedMeasurements()) {
                    featureListItems.add(new FeatureListItem(i, measurementKindLocalization.localize(i), measurementKindSet.contains(i)));
                }
            }
        }

        return featureListItems;
    }

    protected void loadSupportedFeatures() {
        FeatureListAdapter adapter = new FeatureListAdapter(requireActivity(), getSupportedFeatures());
        adapter.setSwitchClickable(model.getAttachedAgentState(model.getSelectedDevice().getValue()) == 1);
        listViewFeatures.setAdapter(adapter);
        adapter.updateResults(getSupportedFeatures());
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

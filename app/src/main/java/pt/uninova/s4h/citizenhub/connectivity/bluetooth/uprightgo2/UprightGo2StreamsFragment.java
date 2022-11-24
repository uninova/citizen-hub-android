package pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AbstractConfigurationFragment;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;
import pt.uninova.s4h.citizenhub.ui.devices.FeatureListAdapter;
import pt.uninova.s4h.citizenhub.ui.devices.FeatureListItem;

public class UprightGo2StreamsFragment extends AbstractConfigurationFragment {

    private DeviceViewModel model;
    private ListView streamsListView;
    private MeasurementKindLocalization measurementKindLocalization;

    public UprightGo2StreamsFragment(Agent agent) {
        super(agent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        measurementKindLocalization = new MeasurementKindLocalization(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_streams_listview, container, false);
        streamsListView = view.findViewById(R.id.streamsListView);
        loadSupportedFeatures();

        return view;
    }

    protected void loadSupportedFeatures() {
        if (model.getSelectedDeviceAgent() != null) {
            FeatureListAdapter adapter = new FeatureListAdapter(requireActivity(), getSupportedFeatures(), model.getSelectedDeviceAgent().getState() == 1, model.getSelectedDeviceAgent());

            streamsListView.setAdapter(adapter);
            adapter.updateResults(getSupportedFeatures());

        } else {
            FeatureListAdapter adapter = new FeatureListAdapter(requireActivity(), getSupportedFeatures(), model.getSelectedDeviceAgent());
            streamsListView.setAdapter(adapter);
            adapter.updateResults(getSupportedFeatures());

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

}

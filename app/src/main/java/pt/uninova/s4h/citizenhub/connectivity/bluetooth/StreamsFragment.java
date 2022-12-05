package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;
import pt.uninova.s4h.citizenhub.ui.devices.FeatureListAdapter;
import pt.uninova.s4h.citizenhub.ui.devices.FeatureListItem;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class StreamsFragment extends AbstractConfigurationFragment {
    private final Agent agent;
    private ListView streamsListView;
    private MeasurementKindLocalization measurementKindLocalization;
    private TextView collectedData;
    private final Observer<StateChangedMessage<Integer, ? extends Agent>> agentStateObserver = value -> {
        streamsListView.deferNotifyDataSetChanged();
        collectedData.setEnabled(value.getNewState() == 1);
        requireActivity().runOnUiThread(() -> loadSupportedFeatures(streamsListView));

    };

    public StreamsFragment(Agent agent) {
        super(agent);
        this.agent = agent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_streams_listview, container, false);
        streamsListView = view.findViewById(R.id.streamsListView);
        collectedData = view.findViewById(R.id.textConfigurationMeasurements);
        measurementKindLocalization = new MeasurementKindLocalization(requireContext());
        loadSupportedFeatures(streamsListView);
        return view;
    }


    protected void loadSupportedFeatures(ListView listView) {
        FeatureListAdapter adapter;
        if (agent != null) {
            adapter = new FeatureListAdapter(requireActivity(), getSupportedFeatures(), agent.getState() == 1, agent);

        } else {
            adapter = new FeatureListAdapter(requireActivity(), getSupportedFeatures(), agent);

        }
        listView.setAdapter(adapter);
        adapter.updateResults(getSupportedFeatures());
    }

    protected List<FeatureListItem> getSupportedFeatures() {
        final List<FeatureListItem> featureListItems = new LinkedList<>();
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


    @Override
    public void onResume() {
        super.onResume();
        agent.addStateObserver(agentStateObserver);
    }


    @Override
    public void onPause() {
        super.onPause();
        agent.removeStateObserver(agentStateObserver);
    }


}

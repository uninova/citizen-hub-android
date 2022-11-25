package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;
import pt.uninova.s4h.citizenhub.ui.devices.FeatureListAdapter;
import pt.uninova.s4h.citizenhub.ui.devices.FeatureListItem;

public class AbstractConfigurationFragment extends Fragment {

    private Agent agent;
    public MeasurementKindLocalization measurementKindLocalization;
    public AbstractConfigurationFragment(Agent agent) {
        this.agent = agent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        measurementKindLocalization = new MeasurementKindLocalization(requireContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void enableView(View view, boolean state) {
        view.setEnabled(state);
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

}

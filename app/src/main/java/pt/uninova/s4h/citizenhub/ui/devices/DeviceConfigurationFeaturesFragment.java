package pt.uninova.s4h.citizenhub.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;

public class DeviceConfigurationFeaturesFragment extends Fragment {

    private MeasurementKindLocalization measurementKindLocalization;
    private ListView labelListView;
    private Agent agent;

    public DeviceConfigurationFeaturesFragment(Agent agent) {
        this.agent = agent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_features_listview, container, false);
        labelListView = view.findViewById(R.id.listViewFeature);

        measurementKindLocalization = new MeasurementKindLocalization(requireContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        labelListView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.list_item_label, getLabelList(agent)));

    }

    protected List<String> getLabelList(Agent agent) {
        List<String> labelList = new ArrayList<>();
        for (int i : agent.getSupportedMeasurements()) {
            labelList.add(measurementKindLocalization.localize(i));
        }
        return labelList;
    }

}


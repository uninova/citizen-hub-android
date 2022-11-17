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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.hl7.fhir.utilities.json.JSONUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;

public class DeviceConfigurationFeaturesFragment extends Fragment {

    private DeviceViewModel model;
    private MeasurementKindLocalization measurementKindLocalization;
    private ListView labelListView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_listview, container, false);
        labelListView = view.findViewById(R.id.listViewFeature);

        measurementKindLocalization = new MeasurementKindLocalization(requireContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(model.getSelectedDeviceAgent()!=null){
            Navigation.findNavController(DeviceConfigurationFeaturesFragment.this.requireView()).navigate(DeviceIdentificationFragmentDirections.actionDeviceIdentificationFragmentToDeviceConfigurationStreamsFragment());
        }

            System.out.println("ONVIEWCREATEDDDDDDDD " + " " + model.getSelectedDeviceAgent());
            labelListView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.list_item_label, getLabelList(model.getConfigurationAgent().getValue())));
//            DeviceConfigurationFeaturesFragment.this.requireActivity().runOnUiThread(DeviceConfigurationFeaturesFragment.this::loadSupportedFeatures);


    }

    protected List<String> getLabelList(Agent agent) {
        List<String> labelList = new ArrayList<>();
        for (int i : agent.getSupportedMeasurements()) {
            labelList.add(measurementKindLocalization.localize(i));
        }
        return labelList;
    }
//    }
protected List<FeatureListItem> getSupportedFeatures() {
    System.out.println("SUPPORTED FEATURES " + " " + model.getSelectedAgentLiveData().getValue());

    final List<FeatureListItem> featureListItems = new LinkedList<>();
    final Agent agent = model.getConfigurationAgent().getValue();
    if (agent != null) {

        if (agent.getState() != 1 && agent.getEnabledMeasurements() != null) {
            System.out.println("AGENT !=1 " + " " + model.getSelectedAgentLiveData().getValue());

            for (int i : agent.getSupportedMeasurements()) {
                featureListItems.add(new FeatureListItem(i, measurementKindLocalization.localize(i), agent.getEnabledMeasurements().contains(i)));
            }
        } else {
            System.out.println("AGENT READY " + " " + model.getSelectedDeviceAgent());

            final Set<Integer> measurementKindSet = agent.getEnabledMeasurements();

            for (int i : agent.getSupportedMeasurements()) {
                featureListItems.add(new FeatureListItem(i, measurementKindLocalization.localize(i), measurementKindSet.contains(i)));
            }
        }
    }

    return featureListItems;
}

    protected void loadSupportedFeatures() {
//        labelListView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.list_item_label, getLabelList(model.getSelectedDeviceAgent())));
//        labelListView.deferNotifyDataSetChanged();

            FeatureListAdapter adapter = new FeatureListAdapter(requireActivity(), getSupportedFeatures(), model.getConfigurationAgent().getValue());
            labelListView.setAdapter(adapter);
            adapter.updateResults(getSupportedFeatures());

        }
    }


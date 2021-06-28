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

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceConfigurationFragment extends Fragment {

    protected Button connectDevice;
    protected Button deleteDevice;
    protected Button updateDevice;
    protected TextView nameDevice;
    protected TextView addressDevice;
    protected ListView listViewFeatures;
    protected List<FeatureListItem> featuresList;
    private AgentOrchestrator agentOrchestrator;
    private DeviceViewModel model;
    //    private MutableLiveData<List<MeasurementKind>> enabledFeatures;


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

    protected List<FeatureListItem> getSupportedFeatures() {
        return model.getSupportedFeatures();
    }

    protected List<FeatureListItem> getEnabledFeatures() {
        return model.getEnabledFeatures();
    }

//    protected List<FeatureListItem> setEnabledFeatures(){
//        for (FeatureListItem feature:getEnabledFeatures()
//             ) {
//            if(featuresList.contains(feature)){
//                featuresList.get(featuresList.indexOf(feature)).setActive(true);
//            }
//
//        }
//        return featuresList;
//    }


    public void setListViewFeaturesAdapter() {
//        List<Feature> featureListItems = model.getAll().getValue();
//        for(int i =0;i<listViewFeatures.getAdapter().getCount();i++){
//                if(featureList.contains(((FeatureListItem) listViewFeatures.getAdapter()).getMeasurementKind())){
//
//                }
//            }
//        }
        FeatureListAdapter adapter = new FeatureListAdapter(requireActivity(), getEnabledFeatures());
        listViewFeatures.setAdapter(adapter);
        adapter.updateResults(getEnabledFeatures());
    }

    protected void loadFeatureState() {
//            List<Feature> featureListItem = new LinkedList<>();
//            featureListItem.addAll(model.getAll().getValue());
//            for(int i =0;i<featureListItem.size();i++)
//            if(featureListItem.contains( ((FeatureListItem) listViewFeatures.getAdapter()).getMeasurementKind())) {
//                ((FeatureListItem) listViewFeatures.getAdapter()).setActive(true);
//                listViewFeatures.getAdapter()
//            }
        FeatureListAdapter adapter = new FeatureListAdapter(requireActivity(), getSupportedFeatures());
        listViewFeatures.setAdapter(adapter);
        adapter.updateResults(getSupportedFeatures());
    }



    protected void saveFeaturesChosen() {
        for (int i = 0; i < listViewFeatures.getAdapter().getCount(); i++) {
            System.out.println(i + " " + ((FeatureListItem) listViewFeatures.getAdapter().getItem(i)).isActive());
            if (((FeatureListItem) listViewFeatures.getAdapter().getItem(i)).isActive()) {
                assert model.getSelectedDevice() != null;
                System.out.println(i);
                //                featureViewModel.setFeature(new Feature(device.getAddress(),featuresList.get(i).getMeasurementKind()));
                model.apply((new Feature(model.getSelectedDevice().getValue().getAddress(), ((FeatureListItem) listViewFeatures.getAdapter().getItem(i)).getMeasurementKind())));

            } else {
                assert model.getSelectedDevice() != null;
                System.out.println(i + " " + "not checked");
                model.delete(new Feature(model.getSelectedDevice().getValue().getAddress(), ((FeatureListItem) listViewFeatures.getAdapter().getItem(i)).getMeasurementKind()));

            }
        }
    }

    protected void setFeaturesState() {
        for (int i = 0; i < listViewFeatures.getAdapter().getCount(); i++) {
            if (((FeatureListItem) listViewFeatures.getAdapter().getItem(i)).isActive()) {
                    //update
//                model.getSelectedAgent(requireActivity()).enableMeasurement(((FeatureListItem) listViewFeatures.getAdapter().getItem(i)).getMeasurementKind());
                assert model.getSelectedDevice() != null;
                model.apply(new Feature(model.getSelectedDevice().getValue().getAddress(), ((FeatureListItem) listViewFeatures.getAdapter().getItem(i)).getMeasurementKind()));
            } else {
                //              model.getSelectedAgent(requireActivity()).disableMeasurement(((FeatureListItem) listViewFeatures.getAdapter().getItem(i)).getMeasurementKind());
                assert model.getSelectedDevice() != null;
                model.delete(new Feature(model.getSelectedDevice().getValue().getAddress(), ((FeatureListItem) listViewFeatures.getAdapter().getItem(i)).getMeasurementKind()));
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        agentOrchestrator = ((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator();
    }

    @Override
    public void onResume() {
        super.onResume();
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

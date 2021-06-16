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
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.FeatureRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class DeviceConfigurationFragment extends Fragment {

    protected Button connectDevice;
    protected Button deleteDevice;
    protected Button updateDevice;
    protected TextView nameDevice;
    protected TextView addressDevice;
    protected ListView listViewFeatures;
    protected List<FeatureListItem> featuresList;
    final DeviceViewModel model;
    final Device device;
    final private FeatureRepository featureRepository;

    public DeviceConfigurationFragment() {
        this.model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        this.device = model.getSelectedDevice().getValue();

        this.featureRepository = new FeatureRepository(requireActivity().getApplication());

    }

    protected void setupViews(View result) {
        nameDevice = result.findViewById(R.id.textConfigurationDeviceName);
        addressDevice = result.findViewById(R.id.textConfigurationDeviceAddress);
        listViewFeatures = result.findViewById(R.id.listViewFeature);
    }

    protected void setupText() {
        nameDevice.setText(getString(R.string.fragment_configuration_text_view_name, device.getName()));
        addressDevice.setText(getString(R.string.fragment_configuration_text_view_address, device.getAddress()));
    }

    protected void setupFeatures(Agent agent) {

        featuresList = new ArrayList<>();

        for (MeasurementKind feature : agent.getSupportedMeasurements()) {
            if (MeasurementKind.find(feature.getId()) != null) {
                featuresList.add(new FeatureListItem(feature));
            }
        }

        listViewFeatures.setAdapter(new FeatureListAdapter(requireContext(), featuresList));
    }


    //
    protected void setFeaturesState(Agent agent) {
        for (int i = 0; i < listViewFeatures.getAdapter().getCount(); i++) {
            if (listViewFeatures.getAdapter().isEnabled(i)) {
                agent.enableMeasurement(featuresList.get(i).getMeasurementKind());
                featureRepository.add(device.getAddress(), featuresList.get(i).getMeasurementKind());

            } else {
                agent.disableMeasurement(featuresList.get(i).getMeasurementKind());
                featureRepository.remove(device.getAddress(), featuresList.get(i).getMeasurementKind());
            }
        }
    }

//    protected void testingFillFeatures() //TODO this is only for testing
//    {
//        featuresDevice = new HashSet<>();
//        featuresDevice.add(MeasurementKind.STEPS);
//        featuresDevice.add(MeasurementKind.HEART_RATE);
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}

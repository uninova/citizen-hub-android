package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeviceConfigurationFragment extends Fragment {

    protected Button connectDevice;
    protected Button deleteDevice;
    protected Button updateDevice;
    protected TextView nameDevice;
    protected TextView addressDevice;
    protected ListView listViewFeatures;

    protected Set<MeasurementKind> featuresDevice;
    protected List<FeatureListItem> featuresList;

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

    protected void setupFeatures() {
        featuresList = new ArrayList<>();

        for (MeasurementKind feature : featuresDevice) {
            if (MeasurementKind.find(feature.getId()) != null) {
                featuresList.add(new FeatureListItem(feature));
            }
        }

        listViewFeatures.setAdapter(new FeatureListAdapter(getContext(), featuresList));
    }

    protected void testingFillFeatures() //TODO this is only for testing
    {
        featuresDevice = new HashSet<>();
        featuresDevice.add(MeasurementKind.STEPS);
        featuresDevice.add(MeasurementKind.HEART_RATE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}

package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class DeviceConfigurationFragment extends Fragment {

    protected Button connectDevice;
    protected Button deleteDevice;
    protected Button updateDevice;
    protected TextView nameDevice;
    protected TextView addressDevice;
    ListView listViewFeatures;
    protected DeviceViewModel model;
    private Application app;
    protected Set<MeasurementKind> featuresDevice;
    protected ArrayList<FeatureListItem> featuresList;
    protected Device changedDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_device_configuration, container, false);

        return result;
    }

    protected void setupViews (View result){
        nameDevice = result.findViewById(R.id.textConfigurationDeviceName);
        addressDevice = result.findViewById(R.id.textConfigurationDeviceAddress);
        listViewFeatures = result.findViewById(R.id.listViewFeature);
    }

    protected void setupText (){
        changedDevice = DeviceListFragment.deviceForSettings;
        nameDevice.setText(getString(R.string.fragment_configuration_text_view_name, changedDevice.getName()));
        addressDevice.setText(getString(R.string.fragment_configuration_text_view_address, changedDevice.getAddress()));
    }

    protected void setupFeatures(){
        featuresList = new ArrayList<>();
        for (MeasurementKind feature : featuresDevice) {
            if (MeasurementKind.find(feature.getId()) != null)
            {
                featuresList.add(new FeatureListItem(feature));
            }
        }
        listViewFeatures.setAdapter(new FeatureListAdapter(getContext(), featuresList));
    }

    protected void testingFillFeatures() //TODO this is only for testing
    {
        featuresDevice = new HashSet<>();
        featuresDevice.add(MeasurementKind.GOOD_POSTURE);
        featuresDevice.add(MeasurementKind.STEPS);
        featuresDevice.add(MeasurementKind.HEART_RATE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Application) requireActivity().getApplication();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
    }

}

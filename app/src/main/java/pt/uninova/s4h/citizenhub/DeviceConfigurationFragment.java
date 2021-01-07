package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

//import java.sql.Array;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class DeviceConfigurationFragment extends Fragment {

    private Button addDevice;
    private Button deleteDevice;
    private TextView nameDevice;
    private TextView addressDevice;
    ListView listViewFeatures;
    private Application app;
    private DeviceViewModel model;
    private Set<MeasurementKind> featuresDevice;
    private ArrayList<FeatureListItem> featuresList;
    public static Boolean fromSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_configuration, container, false);

        addDevice = result.findViewById(R.id.buttonConfiguration);
        deleteDevice = result.findViewById(R.id.buttonDelete); //TODO new function, protected

        if(fromSearch) {
            addDevice.setText(R.string.fragment_configuration_text_view_connect);
            deleteDevice.setVisibility(View.GONE);
        }
        else {
            addDevice.setText(R.string.fragment_configuration_text_view_update);
            deleteDevice.setVisibility(View.VISIBLE);
            deleteDevice.setText(R.string.fragment_configuration_text_view_delete);
        }
        nameDevice = result.findViewById(R.id.textConfigurationDeviceName);
        addressDevice = result.findViewById(R.id.textConfigurationDeviceAddress);
        listViewFeatures = result.findViewById(R.id.listViewFeature);
        Device device = DeviceListFragment.deviceForSettings;

        nameDevice.setText(getString(R.string.fragment_configuration_text_view_name, device.getName()));
        addressDevice.setText(getString(R.string.fragment_configuration_text_view_address, device.getAddress()));

        //TODO TEMP FOR TESTING
        featuresDevice = new HashSet<>();
        featuresDevice.add(MeasurementKind.GOOD_POSTURE);
        featuresDevice.add(MeasurementKind.STEPS);
        featuresDevice.add(MeasurementKind.HEART_RATE);
        //TODO TEMP

        featuresList = new ArrayList<>();

        for (MeasurementKind feature : featuresDevice) {
            if (MeasurementKind.find(feature.getId()) != null)
            {
                featuresList.add(new FeatureListItem(feature));
            }
        }

        listViewFeatures.setAdapter(new FeatureListAdapter(getContext(), featuresList));

        addDevice.setOnClickListener(view -> {
            model.apply();

            //TODO ONLY FOR TESTING
            for (FeatureListItem feature : featuresList)
            {
                System.out.println(feature.getMeasurementKind() + " " + feature.isActive());
            }
            System.out.println(fromSearch);
            //TODO TEST

            //TODO: set features, DB?

            if (fromSearch) {
                Navigation.findNavController(getView()).navigate(DeviceConfigurationFragmentDirections.actionDeviceAddFragmentToDeviceListFragment());
            }
            else {
                Navigation.findNavController(getView()).navigate(DeviceConfigurationFragmentDirections.actionDeviceConfigurationFragmentToDeviceListFragment());
            }
        });

        deleteDevice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                model.delete(device);
                Navigation.findNavController(getView()).navigate(DeviceConfigurationFragmentDirections.actionDeviceConfigurationFragmentToDeviceListFragment());
            }
        });

        return result;
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

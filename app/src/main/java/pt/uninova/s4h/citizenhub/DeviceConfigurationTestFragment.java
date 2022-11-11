package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class DeviceConfigurationTestFragment extends Fragment {

    private LinearLayout containerLayout;
    protected TextView nameDevice;
    protected TextView addressDevice;
    DeviceViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_device_configuration_update_test, container, false);
        containerLayout = view.findViewById(R.id.layout_device_configuration_container);
        nameDevice = view.findViewById(R.id.textConfigurationDeviceNameValue);
        addressDevice = view.findViewById(R.id.textConfigurationAddressValue);

        final Device device = model.getSelectedDevice().getValue();
        setHeaderValues(device);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

//        ft.add() progressbar fragment
        model.identifySelectedDevice(agent -> {

            if (agent == null) {
                Navigation.findNavController(DeviceConfigurationTestFragment.this.requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceUnsupportedFragment());
            }
/*
            ft.replace() progressbarFragment to Listview fragment (labels)
            ft.add() connectButtonFragment
            onClick remove Connect & listview, add listview with listview (switches)
            if agent.getPairingHelper, ft.add PairingHelper
            replace/add PairingHelper for agent.getAdvancedConfiguration
                    */
        });


        return view;
    }

    private void setHeaderValues(Device device) {
        if (device != null) {
            nameDevice.setText(device.getName());
            addressDevice.setText(device.getAddress());
        }
    }

}

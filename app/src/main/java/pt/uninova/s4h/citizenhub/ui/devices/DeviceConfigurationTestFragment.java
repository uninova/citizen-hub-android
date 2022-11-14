package pt.uninova.s4h.citizenhub.ui.devices;

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

import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.data.Device;

public class DeviceConfigurationTestFragment extends Fragment {

    private LinearLayout containerLayout;

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

        final Device device = model.getSelectedDevice().getValue();

        addFragment(new DeviceConfigurationHeaderFragment());
        addDividerFragment("header_divider");
        addFragment(new DeviceConfigurationProgressBarFragment());

        //        ft.add() progressbar fragment
        model.identifySelectedDevice(agent -> {
            removeFragment(new DeviceConfigurationProgressBarFragment());
            if (agent == null) {
                Navigation.findNavController(DeviceConfigurationTestFragment.this.requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceUnsupportedFragment());
            }
            addFragment(new DeviceConfigurationFeaturesFragment());
            addFragment(new DeviceConfigurationConnectFragment());
/*
            ft.replace() progressbarFragment to Listview fragment (labels)
            ft.add() connectButtonFragment
            onClick remove Connect & listview, add listview with listview (switches)
            if agent.getPairingHelper, ft.add PairingHelper
            replace/add PairingHelper for agent.getAdvancedConfiguration
                    */
        });
        try {
            removeFragment(new DeviceConfigurationConnectFragment());
            removeFragment(new DeviceConfigurationFeaturesFragment());
        } catch (Exception e) {
            e.printStackTrace();
        }
        addFragment(new DeviceConfigurationStreamsFragment());


        return view;
    }
        private void addFragment(Fragment fragment){
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.layout_device_configuration_container, fragment);

            transaction.commitNow();
        }

        private void addDividerFragment(String dividerId){
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.layout_device_configuration_container, new DeviceConfigurationDividerFragment()).addToBackStack(dividerId);

        }

        private void removeFragment(Fragment fragment){
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment).commit();
        }


}


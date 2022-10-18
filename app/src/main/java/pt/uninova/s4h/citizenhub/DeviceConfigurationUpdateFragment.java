package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DeviceConfigurationUpdateFragment extends DeviceConfigurationFragment {

    private final Observer<StateChangedMessage<Integer, ? extends Agent>> agentStateObserver = value -> {
        listViewFeatures.deferNotifyDataSetChanged();
        requireActivity().runOnUiThread(this::loadSupportedFeatures);
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);


        final View view = inflater.inflate(R.layout.fragment_device_configuration_update, container, false);
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        updateDevice = view.findViewById(R.id.buttonConfiguration);
        advancedDevice = view.findViewById(R.id.buttonAdvancedConfigurations);


        enableAdvancedConfigurations();
        setupViews(view);
        setupText();
        loadSupportedFeatures();

        updateDevice.setOnClickListener(v -> {
            saveFeaturesChosen();
            Navigation.findNavController(requireView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());
        });

        advancedDevice.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceConfigurationAdvancedFragment()));

//        deleteDevice.setOnClickListener(v -> {
//            model.removeSelectedDevice();
//
//            Navigation.findNavController(getView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());
//        });

        return view;
    }

    protected void enableAdvancedConfigurations() {
        final DeviceViewModel model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        final Device device = model.getSelectedDevice().getValue();
        if (device.getName().equals("UprightGO2"))
            advancedDevice.setVisibility(View.VISIBLE);
        else
            advancedDevice.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        model.getSelectedDeviceAgent().addStateObserver(agentStateObserver);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.device_configuration_fragment, menu);
        MenuItem removeItem = menu.findItem(R.id.device_configuration_menu_remove_item);
        MenuItem reconnectItem = menu.findItem(R.id.device_configuration_menu_reconnect_item);
        if (model.getSelectedDeviceAgent() != null) {
            menu.removeItem(R.id.device_configuration_menu_reconnect_item);
            removeItem.setOnMenuItemClickListener((MenuItem item) -> {
                model.removeSelectedDevice();
                Navigation.findNavController(getView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());

                return true;
            });
        } else {
            menu.removeItem(R.id.accounts_fragment_menu_add_item);
            reconnectItem.setOnMenuItemClickListener((MenuItem item) -> {
                model.identifySelectedDevice(new Observer<Agent>() {
                    @Override
                    public void observe(Agent agent) {
                        model.addAgent(agent);
                    }
                });
                return false;
            });
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (model.getSelectedDeviceAgent() != null) {
            model.getSelectedDeviceAgent().removeStateObserver(agentStateObserver);
        }
    }
}

package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DeviceConfigurationUpdateFragment extends DeviceConfigurationFragment {

    private LinearLayout advancedConfigurationLayout;
    private final Observer<StateChangedMessage<Integer, ? extends Agent>> agentStateObserver = value -> {
        listViewFeatures.deferNotifyDataSetChanged();
        requireActivity().runOnUiThread(() -> {
            loadSupportedFeatures();
            setChildrenEnabled(advancedConfigurationLayout, value.getNewState() == 1);

        });

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_device_configuration_update, container, false);
        updateDevice = view.findViewById(R.id.buttonConfiguration);
        advancedDevice = view.findViewById(R.id.buttonAdvancedConfigurations);
        advancedConfigurationLayout = view.findViewById(R.id.layout_advanced_configurations_container);
        updateDevice.setVisibility(View.GONE);

        setupViews(view);
        setupText();
        loadSupportedFeatures();

        updateDevice.setOnClickListener(v -> {
            saveFeaturesChosen();
            Navigation.findNavController(requireView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());
        });


        advancedDevice.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceConfigurationAdvancedFragment()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Fragment> fragmentList = model.getSelectedDeviceAgent().getConfigurationFragments();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        if (fragmentList != null) {
            for (int i = 0; i < fragmentList.size(); i++) {
                Fragment newFragment = null;
                try {
                    newFragment = fragmentList.get(i).getClass().newInstance();

                } catch (IllegalAccessException | java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
                assert newFragment != null;
                ft.add(R.id.layout_advanced_configurations_container, newFragment);
                View divider_view = view.findViewById(R.id.divider_advanced_configuration_id);
                if(divider_view!=null)
                divider_view.setAlpha(0.5f);
            }
        }
        ft.commitNow();
    }

    @Override
    public void onResume() {
        super.onResume();

        model.getSelectedDeviceAgent().addStateObserver(agentStateObserver);
        if (model.getSelectedDeviceAgent() != null) {
            setChildrenEnabled(advancedConfigurationLayout, model.getSelectedDeviceAgent().getState() == Agent.AGENT_STATE_ENABLED);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.device_configuration_fragment, menu);
        MenuItem removeItem = menu.findItem(R.id.device_configuration_menu_remove_item);
        MenuItem reconnectItem = menu.findItem(R.id.device_configuration_menu_reconnect_item);
        reconnectItem.setVisible(false);
        MenuItem updateItem = menu.findItem(R.id.device_configuration_menu_update_item);
        updateItem.setOnMenuItemClickListener((MenuItem item) -> {
            saveFeaturesChosen();
            Navigation.findNavController(requireView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());
            return true;
        });

        if (model.getSelectedDeviceAgent().getState() == Agent.AGENT_STATE_ENABLED) {
            menu.removeItem(R.id.device_configuration_menu_reconnect_item);
            removeItem.setOnMenuItemClickListener((MenuItem item) -> {
                model.removeSelectedDevice();
                Navigation.findNavController(getView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());

                return true;
            });
        } else {
            removeItem.setOnMenuItemClickListener((MenuItem item) -> {
                model.removeSelectedDevice();
                Navigation.findNavController(getView()).navigate(DeviceConfigurationUpdateFragmentDirections.actionDeviceConfigurationUpdateFragmentToDeviceListFragment());

                return true;
            });

        }
    }

    private static void setChildrenEnabled(ViewGroup layout, boolean state) {
        layout.setEnabled(state);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                setChildrenEnabled((ViewGroup) child, state);
            } else {
                child.setEnabled(state);
                if(!child.isEnabled()) {
                    child.setAlpha(0.5f);
                }
                else child.setAlpha(1);
            }


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

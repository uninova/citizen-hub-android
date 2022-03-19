package pt.uninova.s4h.citizenhub;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestratorListener;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class DeviceListFragment extends Fragment {

    private DeviceListAdapter adapter;
    private DeviceViewModel model;

    private void buildRecycleView(View view) {
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView_devicesList);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());

        adapter = new DeviceListAdapter(item -> {
            model.selectDevice(item.getDevice());

            Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceConfigurationUpdateFragment());
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_device_list, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_list, container, false);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        model.getAgentOrchestrator().observe(getViewLifecycleOwner(), this::onAgentOrchestrator);

        Button searchDevices = result.findViewById(R.id.searchButton);

        buildRecycleView(result);

        setHasOptionsMenu(false);

        searchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConnectedDialog(result);
            }
        });

        return result;
    }

    private void showConnectedDialog(View result) {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_connection);

        Switch wearOSButton = dialog.findViewById(R.id.wearOSButton);
        Switch bluetoothButton = dialog.findViewById(R.id.bluetoothButton);
        Button submitButton = dialog.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if wearOS and BT are checked
                if (bluetoothButton.isChecked() && wearOSButton.isChecked()) {
                    Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment());
                    dialog.dismiss();
                } else if (bluetoothButton.isChecked()) {
                    Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment());
                    dialog.dismiss();
                } else if (wearOSButton.isChecked()) {
                    Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchWearosFragment());

                    dialog.dismiss();
                } else {
                    //does nothing, nothing was selected
                }
            }
        });

        dialog.show();
    }


    /*
        private void buildRecycleView(View result) {
            recyclerView = result.findViewById(R.id.recyclerView_devicesList);
            //recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            adapter = new DeviceListAdapter(deviceList);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            emptyListCheck();

            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

            adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    model.setDevice(deviceList.get(position).getDevice());
                    Navigation.findNavController(requireView()).navigate();
                }

                @Override
                public void onSettingsClick(int position) {
                    model.setDevice(deviceList.get(position).getDevice());
                    Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceConfigurationUpdateFragment());
                }
            });
        }

     */
    @Override
    public void onResume() {
        super.onResume();

        TextView noDevices = requireActivity().findViewById(R.id.fragment_device_list_no_data);

        if (adapter.getItemCount() == 0) {
            noDevices.setVisibility(View.VISIBLE);
        } else {
            noDevices.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onAgentOrchestrator(AgentOrchestrator agentOrchestrator) {
        final Activity activity = requireActivity();
        final Set<Device> deviceSet = agentOrchestrator.getDevices();

        if (deviceSet.size() > 0) {
            for (Device i : deviceSet) {
                adapter.addItem(new DeviceListItem(i, R.drawable.ic_devices_unpaired));
            }
        }

        agentOrchestrator.addListener(new AgentOrchestratorListener() {
            @Override
            public void onDeviceAdded(Device device) {
                final DeviceListItem deviceListItem = new DeviceListItem(device, R.drawable.ic_devices_unpaired);

                activity.runOnUiThread(() -> adapter.addItem(deviceListItem));
            }

            @Override
            public void onDeviceRemoved(Device device) {
                activity.runOnUiThread(() -> adapter.removeItem(device));
            }
        });
    }
}

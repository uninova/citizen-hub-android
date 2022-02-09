package pt.uninova.s4h.citizenhub;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestratorListener;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRecord;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceListFragment extends Fragment {

    public static DeviceRecord deviceRecordForSettings;
    public static MutableLiveData<DeviceListItem> asd;
    TextView noDevices;
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
        }
    };
    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<DeviceListItem> deviceList;
    private Button searchDevices;
    private DeviceViewModel model;

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_device_list, menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceList = new ArrayList<>();

        if (adapter != null)
            adapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_list, container, false);

        searchDevices = result.findViewById(R.id.searchButton);
        noDevices = result.findViewById(R.id.fragment_device_list_no_data);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        if (adapter == null) {
            buildRecycleView(result);
        }

        if (((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().getDevices().size() > 0) {
            deviceList.clear();

            for (Device device : ((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().getDevices()
            ) {
                deviceList.add(new DeviceListItem(device));
                adapter.notifyDataSetChanged();
            }
        }
        Activity activity = requireActivity();
        ((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().addListener(new AgentOrchestratorListener() {
            @Override
            public void onDeviceAdded(Device device) {
                DeviceListItem listItem = new DeviceListItem(device);

                if (!deviceList.contains(listItem)) {
                    deviceList.add(listItem);
                    Collections.sort(deviceList);
                }

                activity.runOnUiThread(() -> adapter.notifyDataSetChanged());
            }

            @Override
            public void onDeviceRemoved(Device device) {
                DeviceListItem listItem = new DeviceListItem(device);

                if (deviceList.contains(listItem)) {
                    deviceList.remove(listItem);
                }

                activity.runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });


        buildRecycleView(result);
        setHasOptionsMenu(false); //shows Action Bar menu button

        searchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConnectedDialog(result);
            }
        });

        return result;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

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


    private void cleanList() {
        deviceList = new ArrayList<>();
    }


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
                Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceConfigurationUpdateFragment());
            }

            @Override
            public void onSettingsClick(int position) {
                model.setDevice(deviceList.get(position).getDevice());
                Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceConfigurationUpdateFragment());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        emptyListCheck();

        if (adapter != null)
            adapter.notifyDataSetChanged();
        else {
            buildRecycleView(requireView());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void emptyListCheck() {
        if (deviceList.isEmpty()) {
            noDevices.setVisibility(View.VISIBLE);
        } else {
            noDevices.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}

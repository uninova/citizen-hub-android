package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.app.Dialog;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceListFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DeviceListItem> deviceList;
    private Application app;
    private View resultView;
    public static Device deviceForSettings;
    private Button searchDevices;
    TextView noDevices;

    private DeviceViewModel model;


    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_device_list, menu);
    }

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = requireActivity().getApplication();
        deviceList = new ArrayList<>();
        cleanList();
        System.out.println("ENTROUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_list, container, false);
        resultView = result;

        searchDevices = result.findViewById(R.id.searchButton);
        noDevices = result.findViewById(R.id.fragment_device_list_no_data);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

//        model.getDevices().observe(getViewLifecycleOwner(), this::onDeviceUpdate);
        if (((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().getDevicesFromMap().size() > 0) {
            System.out.println("ENTROUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
            for (Device device : ((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().getDevicesFromMap()
            ) {
//                model.setDevice(device);
//                model.apply();
                System.out.println("TAMANHO DEVICE AGENT MAP" + ((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().getDevicesFromMap().size());

                deviceList.add(new DeviceListItem(device, R.drawable.ic_watch, R.drawable.ic_settings));
                System.out.println("TAMANHO DEVICE LIST 1" + deviceList.size());
                if (adapter != null) // it works second time and later
                    adapter.notifyDataSetChanged();
                else
                    buildRecycleView(result);
//                adapter.updateResults(deviceList);

            }
        }
        //TODO não aceder à db, ir buscar ao Orchestrator
//        cleanList();
        System.out.println("TAMANHO DEVICE LIST 2" + deviceList.size());

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

        //initialize Views and Buttons
        Switch wearOSButton = dialog.findViewById(R.id.wearOSButton);
        Switch bluetoothButton = dialog.findViewById(R.id.bluetoothButton);
        Button submitButton = dialog.findViewById(R.id.submitButton);

        //submitting selection
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if wearOS and BT are checked
                if (bluetoothButton.isChecked() && wearOSButton.isChecked()) {
                    //TODO this is only showing bluetooth when both selected, because the search is not yet combined
                    Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment());
                    dialog.dismiss();
                }
                else if (bluetoothButton.isChecked()){
                    Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment());
                    dialog.dismiss();
                }
                else if (wearOSButton.isChecked()) {
                    Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchWearosFragment());

                    dialog.dismiss();
                }
                else {
                    //does nothing, nothing was selected
                }
            }
        });

        dialog.show();
    }

    private void onDeviceUpdate(List<Device> devices) {
        cleanList();
        for (Device device : devices) {
            deviceList.add(new DeviceListItem(device, R.drawable.ic_watch, R.drawable.ic_settings));
        }
        buildRecycleView(resultView);
    }

    private void cleanList() {
        deviceList = new ArrayList<>();
    }


    private void buildRecycleView(View result) {
        recyclerView = result.findViewById(R.id.recyclerView_devicesList);
//        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new DeviceListAdapter(deviceList);
        System.out.println("TAMANHO DEVICE LIST 3" + deviceList.size());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        System.out.println("Lista vazia?" + " " + deviceList.isEmpty());
        if (deviceList.isEmpty()) {
            noDevices.setVisibility(View.VISIBLE);
        } else {
            noDevices.setVisibility(View.INVISIBLE);
        }

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onSettingsClick(int position) {
                model.setDevice(deviceList.get(position).getDevice());
                Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceConfigurationUpdateFragment());
                /*deviceForSettings = new Device(deviceList.get(position).getName(),
                        deviceList.get(position).getAddress(), null, null);*/
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if (item.getItemId() == R.id.menu_fragment_device_list_search) {
            new AlertDialog.Builder(getContext())
                    .setMessage("Please select one method of connection compatible with your device.")
                    .setTitle("Method of Connection")
                    .setPositiveButton("Bluetooth", (paramDialogInterface, paramInt) ->
                        Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment()))
                    .setNegativeButton("Wear OS", (paramDialogInterface, paramInt) ->
                            Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchWearosFragment()))
                    .show();
        }*/
        return super.onOptionsItemSelected(item);
    }


}

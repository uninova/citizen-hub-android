package pt.uninova.s4h.citizenhub;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

public class DeviceListFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DeviceListItem> deviceList;
    private Application app;
    private View resultView;
    public static Device deviceForSettings;

    private DeviceViewModel model;


    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_device_list, menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Application) requireActivity().getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_list, container, false);
        resultView = result;

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        model.getDevices().observe(getViewLifecycleOwner(), this::onDeviceUpdate);

        cleanList();
        buildRecycleView(result);

        setHasOptionsMenu(true);

        return result;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
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
        recyclerView = (RecyclerView) result.findViewById(R.id.recyclerView_devicesList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new DeviceListAdapter(deviceList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onSettingsClick(int position) {
                Navigation.findNavController(getView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceDetailFragment());
                deviceForSettings = new Device(deviceList.get(position).getName(),
                        deviceList.get(position).getAddress(), null, null);
            }
        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_fragment_device_list_search) {
            new AlertDialog.Builder(getContext())
                    .setMessage("Please select one method of connection compatible with your device.")
                    .setTitle("Method of Connection")
                    .setPositiveButton("Bluetooth", (paramDialogInterface, paramInt) ->
                        Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment()))
                        //paramDialogInterface.dismiss()
                    .setNegativeButton("Wear OS", (paramDialogInterface, paramInt) ->
                            Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchWearosFragment()))
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }


}
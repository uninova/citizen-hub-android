package pt.uninova.s4h.citizenhub;

import android.app.AlertDialog;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScanner;
import pt.uninova.s4h.citizenhub.persistence.Device;

public class DeviceSearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DeviceListItem> deviceList;
    private DeviceViewModel model;
    private BluetoothScanner scanner;
    boolean bluetooth_enabled;
    LayoutInflater localInflater;
    ViewGroup localContainer;
    Boolean goBackNeeded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        localInflater = inflater;
        localContainer = container;
        LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        BluetoothManager bm = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        bluetooth_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        try {
            bluetooth_enabled = bm.getAdapter().isEnabled();
        } catch(Exception ex) {}

        if(!bluetooth_enabled)
        {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.fragment_search_dialog_bluetooth_not_enabled)
                    .setPositiveButton(R.string.fragment_search_dialog_bluetooth_open_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            getContext().startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.fragment_search_dialog_bluetooth_cancel,null)
                    .show();
        }
        else if(!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.fragment_search_dialog_location_not_enabled)
                    .setPositiveButton(R.string.fragment_search_dialog_location_open_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.fragment_search_dialog_location_cancel,null)
                    .show();
        }

        final View result = inflater.inflate(R.layout.fragment_device_search, container, false);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        model.getDevices().observe(getViewLifecycleOwner(), this::onDeviceUpdate);
        cleanList();
        buildRecycleView(result);
        scanner = new BluetoothScanner((BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE));

        if(bluetooth_enabled)
        {
            System.out.println("Searching..."); //for console
            scanner.start((address, name) -> {
                buildRecycleView(result);
                System.out.println("BT: " + address + " and " + name); //for console
                deviceList.add(new DeviceListItem(R.drawable.ic_watch_off,
                        new Device(name, address, null, null)));
                adapter.notifyItemInserted(0);
            });
        }

        return result;
    }

    private void onDeviceUpdate(List<Device> devices) {
        //not being used
    }

    private void cleanList() {
        deviceList = new ArrayList<>();
    }

    private void buildRecycleView(View result) {
        recyclerView = (RecyclerView) result.findViewById(R.id.recyclerView_searchList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new DeviceListAdapter(deviceList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                model.setDevice(deviceList.get(position).getDevice());
                Navigation.findNavController(getView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceAddConfigurationFragment());
                DeviceListFragment.deviceForSettings = new Device(deviceList.get(position).getmTextTitle(),
                        deviceList.get(position).getmTextDescription(), null, null);
            }

            @Override
            public void onSettingsClick(int position) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanner.stop();
    }

    public void onStop(){
        super.onStop();
        goBackNeeded = true;
    }

    public void onResume(){
        super.onResume();
        if (goBackNeeded) {
            scanner.stop();
            Navigation.findNavController(getView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceListFragment());
        }
    }
}
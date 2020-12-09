package pt.uninova.s4h.citizenhub;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScanner;
import pt.uninova.s4h.citizenhub.persistence.Device;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class DeviceSearchFragment extends Fragment {

    private static final int BLUETOOTH_REQ_CODE = 99;
    private static final int LOCATION_REQUEST_CODE = 100;

    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DeviceListItem> deviceList;
    private DeviceViewModel model;
    public static Device deviceForSettings;
    private BluetoothScanner scanner;
    boolean bluetooth_enabled;
    LayoutInflater localInflater;
    ViewGroup localContainer;
    Boolean goBackNeeded = false;
    Fragment fragment = this;
    boolean askOnceAgain = false;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    LocationManager lm;
    BluetoothManager bm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        localInflater = inflater;
        localContainer = container;
        lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        bm = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);

        bluetooth_enabled = false;
        checkPermissions();
        requestFeatures();


        final View result = inflater.inflate(R.layout.fragment_device_search, container, false);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        model.getDevices().observe(getViewLifecycleOwner(), this::onDeviceUpdate);
        cleanList();
        buildRecycleView(result);
        scanner = new BluetoothScanner((BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE));

        if (bluetooth_enabled) {
            System.out.println("Searching...");
            scanner.start((address, name) -> {
                buildRecycleView(result);
                System.out.println("BT: " + address + " and " + name);
                deviceList.add(new DeviceListItem(R.drawable.ic_watch_off,
                        new Device(name, address, null, null), R.drawable.ic_settings_off));
                adapter.notifyItemInserted(0);
            });
        }

        return result;
    }

    private void requestFeatures() {


        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            bluetooth_enabled = bm.getAdapter().isEnabled();
        } catch (Exception ex) {
        }

        if (!bluetooth_enabled) {
            if (!bluetooth_enabled) {
                Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                fragment.startActivityForResult(bluetoothIntent, BLUETOOTH_REQ_CODE);
            }
        } else if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(getContext())
                    .setMessage("Location function not enabled.")
                    .setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                           getContext().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
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
                Navigation.findNavController(getView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceAddFragment());

                deviceForSettings = new Device(deviceList.get(position).getmTextTitle(),
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Navigation.findNavController(requireView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentRefresh());
        } else if (resultCode == RESULT_CANCELED) {
            //user rejected turning bluetooth on
            Navigation.findNavController(requireView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceListFragment());
        }
        if (resultCode == LOCATION_REQUEST_CODE) {
                Navigation.findNavController(requireView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentRefresh());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case 101: {
                boolean required = false;
                for (int i = 0; i < permissions.length; i++) {
                    getActivity().getPackageManager();
                    if (grantResults[i] == PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                        required = true;
                    }
                }
                if (required) {
                    askOnceAgain = true;
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void checkPermissions() {

        int hasLocationPermission = ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION);
        int hasBluetoothPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH);
        int hasBluetoothAdminPermission = ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION);

        List<String> permissions = new ArrayList<>();

        if (hasBluetoothPermission != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.BLUETOOTH);
        }
        if (hasBluetoothAdminPermission != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
        }

        if (hasLocationPermission != PERMISSION_GRANTED) {
            permissions.add(ACCESS_FINE_LOCATION);
        }


        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[0]), 101);
        }

    }


    public void onStop() {
        super.onStop();
        System.out.println("GOT STOPPED");
        goBackNeeded = true;
    }

    public void onResume() {
        super.onResume();
        System.out.println("GONE BACK TO ONRESUME");


        if (askOnceAgain) {
            checkPermissions();
            askOnceAgain = false;
        }
        if (goBackNeeded) {
            scanner.stop();
          //  getActivity().getSupportFragmentManager().popBackStack();
            Navigation.findNavController(getView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceListFragment());
        }
    }
}
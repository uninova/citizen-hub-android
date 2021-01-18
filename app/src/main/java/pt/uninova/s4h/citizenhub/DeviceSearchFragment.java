package pt.uninova.s4h.citizenhub;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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

    private static final int PERMISSIONS_REQUEST_CODE = 77;
    private static final int FEATURE_BLUETOOTH_STATE = 78;

    private DeviceListAdapter adapter;
    private ArrayList<DeviceListItem> deviceList;
    private DeviceViewModel model;
    private BluetoothScanner scanner;
    LayoutInflater localInflater;
    ViewGroup localContainer;
    private LocationManager locationManager;
    private BluetoothManager bluetoothManager;
    private boolean hasStartedEnableLocationActivity = false;
    private boolean hasStartedEnableBluetoothActivity = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        localInflater = inflater;
        localContainer = container;

        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        bluetoothManager = (BluetoothManager) requireContext().getSystemService(Context.BLUETOOTH_SERVICE);

        final View result = inflater.inflate(R.layout.fragment_device_search, container, false);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        model.getDevices().observe(getViewLifecycleOwner(), this::onDeviceUpdate);

        cleanList();
        buildRecycleView(result);

        return result;
    }

    private void checkPermissions() {

        if (!hasBluetoothEnabled()) {
            enableBluetooth();

        } else if (!hasBluetoothPermissions()) {
            requestBluetoothPermissions();

        } else if (!hasLocationEnabled()) {
            enableLocation();

        } else if (!hasLocationPermissions()) {
            requestLocationPermissions();

        } else {
            startScan();

        }
    }

    private boolean hasLocationPermissions() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

    }

    private boolean hasBluetoothPermissions() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean hasBluetoothEnabled() {
        return bluetoothManager.getAdapter().isEnabled();
    }

    private void enableLocation() {
        if (hasStartedEnableLocationActivity) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.fragment_device_search_location_warning_title)
                    .setPositiveButton(R.string.fragment_device_search_location_open_settings_button, (paramDialogInterface, paramInt) -> {
                        requireContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        paramDialogInterface.dismiss();
                    })
                    .setNegativeButton(R.string.fragment_device_search_cancel_option, (dialog, which) -> Navigation.findNavController(requireView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceListFragment()))
                    .show();
        } else {
            hasStartedEnableLocationActivity = true;
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.fragment_device_search_location_not_enabled_title)
                    .setPositiveButton(R.string.fragment_device_search_location_open_settings_button, (paramDialogInterface, paramInt) -> {
                        requireContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        paramDialogInterface.dismiss();
                    })
                    .setNegativeButton(R.string.fragment_device_search_cancel_option, (paramDialogInterface, paramInt) -> checkPermissions())
                    .show();
        }
    }

    private void enableBluetooth() {
        if (hasStartedEnableBluetoothActivity) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.fragment_device_search_bluetooth_warning_title)
                    .setPositiveButton(R.string.fragment_device_search_bluetooth_open_settings_button, (paramDialogInterface, paramInt) -> {
                        requireContext().startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                        paramDialogInterface.dismiss();
                    })
                    .setNegativeButton(R.string.fragment_device_search_cancel_option, (dialog, which) -> Navigation.findNavController(requireView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceListFragment()))
                    .show();

        } else {
            hasStartedEnableBluetoothActivity = true;
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.fragment_device_search_cancel_option)
                    .setPositiveButton(R.string.fragment_device_search_bluetooth_open_settings_button, (paramDialogInterface, paramInt) -> {
                        requireContext().startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                        paramDialogInterface.dismiss();
                    })
                    .setNegativeButton(R.string.fragment_device_search_cancel_option, (paramDialogInterface, paramInt) -> checkPermissions())
                    .show();
        }
    }

    private void requestBluetoothPermissions() {

        requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                PERMISSIONS_REQUEST_CODE);
    }

    private void requestLocationPermissions() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {

            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void startScan() {
        scanner = new BluetoothScanner((BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE));
        scanner.start((address, name) -> {
            buildRecycleView(requireView());
            Device device = new Device(name, address, null, null);
            if (!model.isDevicePaired(device)) {
                deviceList.add(new DeviceListItem(device, R.drawable.ic_watch_off, R.drawable.ic_settings_off));
                adapter.notifyItemInserted(0);
            }
        });
    }


    private void onDeviceUpdate(List<Device> devices) {

    }

    private void cleanList() {
        deviceList = new ArrayList<>();
    }

    private void buildRecycleView(View result) {
        RecyclerView recyclerView = result.findViewById(R.id.recyclerView_searchList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new DeviceListAdapter(deviceList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                model.setDevice(deviceList.get(position).getDevice());
                Navigation.findNavController(requireView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceAddFragment());

                deviceForSettings = new Device(deviceList.get(position).getName(),
                        deviceList.get(position).getAddress(), null, null);
            }

            @Override
            public void onSettingsClick(int position) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (scanner != null) {
            scanner.stop();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FEATURE_BLUETOOTH_STATE) {
            checkPermissions();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (scanner != null) {
            scanner.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPermissions();

    }
}
package pt.uninova.s4h.citizenhub;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScanner;
import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class DeviceSearchFragment extends Fragment {

    private DeviceListAdapter adapter;
    private DeviceViewModel model;

    private BluetoothScanner scanner;

    private void buildRecycleView(View view) {
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView_searchList);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());

        adapter = new DeviceListAdapter(item -> {
            model.selectDevice(item.getDevice());

            Navigation.findNavController(requireView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceAddConfigurationFragment());
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            boolean granted = true;

            for (boolean i : result.values()) {
                granted = granted && i;
            }

            if (granted)
                startScan();
        });

        final String[] permissions;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions = new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION};
        } else {
            permissions = new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION};
        }

        requestPermissionLauncher.launch(permissions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_search, container, false);

        buildRecycleView(view);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (scanner != null) {
            scanner.stop();
            scanner = null;
        }
    }

    /*
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
                    .setMessage(R.string.fragment_device_search_bluetooth_not_enabled_title)
                    .setPositiveButton(R.string.fragment_device_search_bluetooth_open_settings_button, (paramDialogInterface, paramInt) -> {
                        requireContext().startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                        paramDialogInterface.dismiss();
                    })
                    .setNegativeButton(R.string.fragment_device_search_cancel_option, (paramDialogInterface, paramInt) -> checkPermissions())
                    .show();
        }
    }

     */

    private void startScan() {
        scanner = new BluetoothScanner((BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE));

        scanner.start((address, name) -> {
            final Device device = new Device(address, name == null ? address : name, ConnectionKind.BLUETOOTH);

            if (model.getAgentOrchestrator().getValue().getAgent(device) == null) {
                adapter.addItem(new DeviceListItem(device, R.drawable.ic_devices_unpaired));
            }
        });
    }
}
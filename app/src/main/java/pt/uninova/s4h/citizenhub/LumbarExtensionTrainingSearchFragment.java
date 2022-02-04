package pt.uninova.s4h.citizenhub;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScanner;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScannerListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.medx.MedXAgent;
import pt.uninova.s4h.citizenhub.data.MedXTrainingValue;
import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTraining;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public class LumbarExtensionTrainingSearchFragment extends Fragment {

    private static final UUID LUMBARTRAINING_UUID_SERVICE = UUID.fromString("5a46791b-516e-48fd-9d29-a2f18d520aec");
    private static final UUID LUMBARTRAINING_UUID_CHARACTERISTIC = UUID.fromString("38fde8b6-9664-4b8e-8b3a-e52b8809a64c");
    private static final int PERMISSIONS_REQUEST_CODE = 77;
    private static final int FEATURE_BLUETOOTH_STATE = 78;
    LayoutInflater localInflater;
    ViewGroup localContainer;
    private DeviceListAdapter adapter;
    private ArrayList<DeviceListItem> deviceItemList;
    private boolean alreadyConnected = false;
    private DeviceViewModel model;
    private BluetoothScanner scanner;
    private BluetoothScannerListener listener;
    private LocationManager locationManager;
    private BluetoothManager bluetoothManager;
    private boolean hasStartedEnableLocationActivity = false;
    private boolean hasStartedEnableBluetoothActivity = false;
    private BluetoothGatt gatt;
    private BluetoothConnection connection;
    private ProgressBar simpleProgressBar;
    private LumbarExtensionTrainingRepository lumbarRepository;
    //Delete after testing
    Button TESTBUTTON;


    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            if (!alreadyConnected && getView() != null) {
                buildRecycleView(requireView());

                Device device = new Device(result.getDevice().getAddress(), result.getDevice().getName(), ConnectionKind.BLUETOOTH);

                if (!model.isDevicePaired(device)) {
                    DeviceListItem deviceListItem = new DeviceListItem(device, R.drawable.ic_devices_unpaired, R.drawable.ic_settings_off);

                    if (deviceItemList.size() > 0) {
                        for (DeviceListItem deviceItem : deviceItemList) {
                            if (!deviceItem.getDevice().equals(device)) {
                                deviceItemList.add(deviceListItem);
                                adapter.notifyItemInserted(0);
                            }
                        }
                    } else {
                        deviceItemList.add(deviceListItem);
                        adapter.notifyItemInserted(0);
                    }

                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        localInflater = inflater;
        localContainer = container;


        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        bluetoothManager = (BluetoothManager) requireContext().getSystemService(Context.BLUETOOTH_SERVICE);
        final View result = inflater.inflate(R.layout.fragment_device_search, container, false);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        cleanList();

        buildRecycleView(result);

        return result;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        simpleProgressBar = requireView().findViewById(R.id.progressBar);
        lumbarRepository = new LumbarExtensionTrainingRepository(requireActivity().getApplication());
//
//        TESTBUTTON = requireView().findViewById(R.id.TESTEBUTTON);
//        TESTBUTTON.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(LumbarExtensionTrainingSearchFragment.this.requireView()).navigate(LumbarExtensionTrainingSearchFragmentDirections.actionLumbarExtensionTrainingSearchFragmentToLumbarExtensionTrainingFragment());
//
//            }
//        });
    }

    private void startFilteredScan() {
        scanner.startWithFilter(listener, new ParcelUuid(LUMBARTRAINING_UUID_SERVICE), scanCallback);

    }

    private void startScan() {
        System.out.println("LumbarExtensionTrainingSearchFragment.TryStartScan");

        if (!hasBluetoothEnabled()) {
            enableBluetooth();

        } else if (!hasBluetoothPermissions()) {
            requestBluetoothPermissions();

        } else if (!hasLocationEnabled()) {
            enableLocation();

        } else if (!hasLocationPermissions()) {
            requestLocationPermissions();

        } else {
            scanner = new BluetoothScanner(bluetoothManager);
            startFilteredScan();

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
                    .setNegativeButton(R.string.fragment_device_search_cancel_option, (paramDialogInterface, paramInt) -> startScan())
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
                    .setNegativeButton(R.string.fragment_device_search_cancel_option, (paramDialogInterface, paramInt) -> startScan())
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
            startScan();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startScan();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (scanner != null) {
            scanner.stop();
        }
        if (connection != null)
            connection.close();

    }


    @Override
    public void onStop() {
        super.onStop();
        if (scanner != null) {
            scanner.stop();
        }
        if (connection != null)
            connection.close();
    }


    @Override
    public void onResume() {
        super.onResume();
        startScan();

    }

    private void buildRecycleView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_searchList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new DeviceListAdapter(deviceItemList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final Device device = deviceItemList.get(position).getDevice();

                model.setDevice(device);

                simpleProgressBar.setVisibility(View.VISIBLE);

                if (scanner != null) {
                    scanner.stop();
                }

                final BluetoothConnection bluetoothConnection = new BluetoothConnection();
                final BluetoothDevice bluetoothDevice = bluetoothManager.getAdapter().getRemoteDevice(device.getAddress());

                bluetoothConnection.addConnectionStateChangeListener((state) -> {
                    if (state.getOldState() == BluetoothConnectionState.CONNECTED && state.getNewState() == BluetoothConnectionState.READY) {
                        final MedXAgent agent = new MedXAgent(bluetoothConnection);

                        agent.addSampleObserver((sample) -> {
                            agent.disable();
                            bluetoothConnection.close();

                            MedXTrainingValue val = (MedXTrainingValue) sample.getMeasurements()[0].getValue();
                            int duration = (int) val.getDuration().toMillis() / 1000;

                            lumbarRepository.add(new LumbarExtensionTraining(sample.getTimestamp().toEpochMilli(), duration, val.getScore(), val.getRepetitions(), val.getWeight()));

                            requireActivity().runOnUiThread(() -> Navigation.findNavController(LumbarExtensionTrainingSearchFragment.this.requireView()).navigate(LumbarExtensionTrainingSearchFragmentDirections.actionLumbarExtensionTrainingSearchFragmentToLumbarExtensionTrainingFragment()));
                        });

                        agent.enable();
                        agent.enableMeasurement(MeasurementKind.LUMBAR_EXTENSION_TRAINING);
                    }
                });

                bluetoothDevice.connectGatt(requireContext(), false, bluetoothConnection, BluetoothDevice.TRANSPORT_LE);
            }

            @Override
            public void onSettingsClick(int position) {
                model.setDevice(deviceItemList.get(position).getDevice());
                onItemClick(position);
            }
        });
    }

    private void cleanList() {
        deviceItemList = new ArrayList<>();
    }
}

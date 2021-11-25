package pt.uninova.s4h.citizenhub;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScanner;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScannerListener;
import pt.uninova.util.messaging.Observer;

public class LumbarExtensionTrainingSearchFragment extends Fragment {

    private static final UUID LUMBARTRAINING_UUID_SERVICE = UUID.fromString("5a46791b-516e-48fd-9d29-a2f18d520aec");
    private static final UUID LUMBARTRAINING_UUID_CHARACTERISTIC = UUID.fromString("38fde8b6-9664-4b8e-8b3a-e52b8809a64c");
    //    private static final UUID LUMBARTRAINING_UUID_DESCRIPTOR = UUID.fromString("5a46791b");
    public final static UUID UUID_SERVICE_HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_HEART_RATE_DATA = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");

    private static final int PERMISSIONS_REQUEST_CODE = 77;
    private static final int FEATURE_BLUETOOTH_STATE = 78;
    LayoutInflater localInflater;
    ViewGroup localContainer;
    private DeviceListAdapter adapter;
    private ArrayList<DeviceListItem> deviceList;
    private DeviceViewModel model;
    private BluetoothScanner scanner;
    private BluetoothScannerListener listener;
    private LocationManager locationManager;
    private BluetoothManager bluetoothManager;
    private boolean hasStartedEnableLocationActivity = false;
    private boolean hasStartedEnableBluetoothActivity = false;
    private BluetoothGatt gatt;
    private BluetoothConnection connection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        localInflater = inflater;
        localContainer = container;

        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        bluetoothManager = (BluetoothManager) requireContext().getSystemService(Context.BLUETOOTH_SERVICE);
        scanner = new BluetoothScanner(bluetoothManager);
        final View result = inflater.inflate(R.layout.fragment_device_search, container, false);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        cleanList();
        buildRecycleView(result);
        startFilteredScan();
        return result;
    }


    private void startFilteredScan() {
        connection = new BluetoothConnection();
        scanner.startWithFilter(listener, new ParcelUuid(UUID_SERVICE_HEART_RATE), scanCallback);

    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);


            try {
                bluetoothManager.getAdapter().getRemoteDevice(result.getDevice().getAddress()).connectGatt(getContext(), true, connection);

            } catch (Exception e) {
                e.printStackTrace();

            }

            connection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>>() {
                @Override
                public void onChanged(StateChangedMessage<BluetoothConnectionState, BluetoothConnection> value) {

                    if (value.getNewState() == BluetoothConnectionState.READY) {
                        connection.removeConnectionStateChangeListener(this);

                        final String name = connection.getDevice().getName();
                        final String address = connection.getDevice().getAddress();

                        connection.enableNotifications(UUID_SERVICE_HEART_RATE, UUID_CHARACTERISTIC_HEART_RATE_DATA);
                    }
                }

            });
        }


    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (scanner != null) {
            scanner.stop();
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

    }

    private void buildRecycleView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_searchList);
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
            }

            @Override
            public void onSettingsClick(int position) {
                model.setDevice(deviceList.get(position).getDevice());
            }
        });
    }

    private void cleanList() {
        deviceList = new ArrayList<>();
    }
}

//MedX0009

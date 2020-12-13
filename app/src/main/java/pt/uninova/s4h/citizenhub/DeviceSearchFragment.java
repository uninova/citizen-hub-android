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
import android.widget.Toast;

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
import java.util.Objects;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScanner;
import pt.uninova.s4h.citizenhub.persistence.Device;

import static android.app.Activity.RESULT_OK;

public class DeviceSearchFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 77;
    private static final int FEATURE_BLUETOOTH_STATE = 78;
    private static final int FEATURE_LOCATION_STATE = 79;

    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DeviceListItem> deviceList;
    private DeviceViewModel model;
    public static Device deviceForSettings;
    private BluetoothScanner scanner;
    private boolean bluetooth_permission = false;
    private boolean location_permission = false;
    boolean bluetooth_enabled;
    boolean gps_enabled;
    LayoutInflater localInflater;
    ViewGroup localContainer;
    private LocationManager lm;
    private BluetoothManager bm;
    private boolean goBackNeeded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TUDOPRONTO", "oncreate motherfucker");

    }

    private void checkPermissions() {

        getPermissionBluetooth();
        getPermissionBluetoothAdmin();
        getPermissionLocation();
        if (bluetooth_permission && location_permission) {
            isLocationOn();
            isBluetoothOn();
        }

    }

    private void getPermissionBluetooth() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.BLUETOOTH)) {
            }

            requestPermissions(new String[]{Manifest.permission.BLUETOOTH},
                    PERMISSIONS_REQUEST_CODE);
        }
        bluetooth_permission = true;
    }

    private void getPermissionBluetoothAdmin() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {


            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.BLUETOOTH_ADMIN)) {
            }
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    PERMISSIONS_REQUEST_CODE);
        }
        bluetooth_permission = true;
    }

    private void getPermissionLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            }
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE);
        }
        location_permission = true;
    }


    private void isBluetoothOn() {
        try {
            bluetooth_enabled = bm.getAdapter().isEnabled();
        } catch(Exception ex) {}
           if(!bluetooth_enabled){
               Log.d("TUDOPRONTO", "a checkar bluetooth");
               Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
               requireActivity().startActivityForResult(intent, FEATURE_BLUETOOTH_STATE);
           }

        }


    private void isLocationOn() {
        try {
           gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex){}
            if(!gps_enabled){
            Log.d("TUDOPRONTO", "a checkar localização");
            new AlertDialog.Builder(getContext())
                    .setMessage("Location function not enabled.")
                    .setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            paramDialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d("TUDOPRONTO", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

                Toast.makeText(requireContext(), "permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_SHORT).show();
                Log.d("TUDOPRONTO", "permission denied");

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        localInflater = inflater;
        localContainer = container;

        lm = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        bm = (BluetoothManager) requireContext().getSystemService(Context.BLUETOOTH_SERVICE);
        checkPermissions();

        final View result = inflater.inflate(R.layout.fragment_device_search, container, false);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        model.getDevices().observe(getViewLifecycleOwner(), this::onDeviceUpdate);
        cleanList();
        buildRecycleView(result);


        return result;


    }

    private void startScan() {
        scanner = new BluetoothScanner((BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE));
        System.out.println("Searching...");
        scanner.start((address, name) -> {
            buildRecycleView(requireView());
            System.out.println("BT: " + address + " and " + name);
            deviceList.add(new DeviceListItem(R.drawable.ic_watch_off,
                    new Device(name, address, null, null), R.drawable.ic_settings_off));
            adapter.notifyItemInserted(0);
        });
    }


    private void onDeviceUpdate(List<Device> devices) {
        //not being used
    }

    private void cleanList() {
        deviceList = new ArrayList<>();
    }

    private void buildRecycleView(View result) {
        recyclerView = result.findViewById(R.id.recyclerView_searchList);
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
                Navigation.findNavController(requireView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceAddFragment());

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
        if (scanner != null) {
            scanner.stop();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FEATURE_BLUETOOTH_STATE:
            case FEATURE_LOCATION_STATE: {
                if (resultCode == RESULT_OK) {
                } else {
                    Toast.makeText(requireContext(), "Bluetooth on failed", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }
@Override
    public void onStop() {
        super.onStop();
        System.out.println("GOT STOPPED");
        goBackNeeded = true;
    }

@Override
    public void onResume() {
        super.onResume();
        if (goBackNeeded){
            scanner.stop();
            //   requireActivity().getSupportFragmentManager().popBackStack();
        }
       // startScan();

        System.out.println("GONE BACK TO ONRESUME");
        startScan();
    }

    }

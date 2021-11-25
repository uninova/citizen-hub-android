package pt.uninova.s4h.citizenhub;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothManager;
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
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScanner;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScannerListener;
import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.StateKind;

public class LumbarExtensionTrainingSearchFragment extends Fragment {

    private static final UUID LUMBARTRAINING_UUID_SERVICE = UUID.fromString("5a46791b-516e-48fd-9d29-a2f18d520aec");
    private static final UUID LUMBARTRAINING_UUID_CHARACTERISTIC = UUID.fromString("38fde8b6-9664-4b8e-8b3a-e52b8809a64c");
    private static final UUID LUMBARTRAINING_UUID_DESCRIPTOR = UUID.fromString("5a46791b");
    private static final UUID LUMBARTRAINING_UUID_DESCRIPTOR_VALUE = UUID.fromString("LumbarTraining");

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        localInflater = inflater;
        localContainer = container;

        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        bluetoothManager = (BluetoothManager) requireContext().getSystemService(Context.BLUETOOTH_SERVICE);

        scanner = new BluetoothScanner(bluetoothManager);
        listener=null;
        final View result = inflater.inflate(R.layout.fragment_device_search, container, false);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);


        return result;
    }

    private void startFilteredScan(){
        scanner.startWithFilter(listener, new ParcelUuid(LUMBARTRAINING_UUID_SERVICE ));
        }
    }

    //MedX0009

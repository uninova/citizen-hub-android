package pt.uninova.s4h.citizenhub;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BluetoothFragment extends Fragment {

    public static final String[] PERMISSIONS = new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION};
    public static final String[] PERMISSIONS_S = new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN};

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if (adapter == null) {
            onBluetoothUnsupported();
        } else {
            final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean granted = true;

                for (boolean i : result.values()) {
                    granted = granted && i;
                }

                if (granted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        onBluetoothAllowed();
                    } else {
                        final LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

                        if (locationManager == null) {
                            onLocationUnsupported();
                        } else {
                            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                onBluetoothAllowed();
                            } else {
                                onLocationDisabled();
                            }
                        }
                    }

                } else {
                    onBluetoothDenied();
                }
            });

            final String[] permissions;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions = PERMISSIONS_S;
            } else {
                permissions = PERMISSIONS;
            }

            if (adapter.isEnabled()) {
                requestPermissionLauncher.launch(permissions);
            } else {
                final Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                ActivityResultLauncher<Intent> enableBluetoothLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        requestPermissionLauncher.launch(permissions);
                    } else {
                        onBluetoothDisabled();
                    }
                });

                enableBluetoothLauncher.launch(intent);
            }

        }
    }

    protected abstract void onBluetoothAllowed();

    protected abstract void onBluetoothDenied();

    protected abstract void onBluetoothDisabled();

    protected abstract void onBluetoothUnsupported();

    protected abstract void onLocationDisabled();

    protected abstract void onLocationUnsupported();
}

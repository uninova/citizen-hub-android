package pt.uninova.s4h.citizenhub.ui.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;

public class BluetoothDisabledFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_bluetooth_disabled, container, false);

        Button openLocationSettings = result.findViewById(R.id.fragment_bluetooth_disabled_button);
        openLocationSettings.setOnClickListener(
                v -> {
                    Intent bluetoothSettings = new Intent();
                    bluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                    bluetoothSettings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    bluetoothSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(bluetoothSettings);
                });


        return result;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isBluetoothEnabled()) {
            Navigation.findNavController(requireView()).navigate(pt.uninova.s4h.citizenhub.ui.bluetooth.BluetoothDisabledFragmentDirections.actionBluetoothDisabledFragmentToDeviceSearchFragment());
        }

    }

    public static Boolean isBluetoothEnabled() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.isEnabled();
    }


}

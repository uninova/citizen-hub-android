package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class DeviceAddFragment extends Fragment {

    private Button addDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_add, container, false);

        addDevice = result.findViewById(R.id.button_add_device);

        addDevice.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(DeviceAddFragmentDirections.actionDeviceAddFragmentToDeviceListFragment());
        });

        return result;
    }



}
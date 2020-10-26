package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.Device;

public class DeviceDetailFragment extends Fragment {

    private DeviceViewModel model;
    private Application app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_device_detail, container, false);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        TextView detailText = result.findViewById(R.id.text_detail);
        Device device = DeviceListFragment.deviceForSettings;

        Button button = result.findViewById(R.id.button_deviceDetail_forgetDevice);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                model.delete(device);
                Navigation.findNavController(getView()).navigate(DeviceDetailFragmentDirections.actionDeviceDetailFragmentToDeviceListFragment());
            }
        });

        detailText.setText("Name: " + device.getName() + "\n" + "Address: " + device.getAddress());

        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Application) requireActivity().getApplication();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
    }

}
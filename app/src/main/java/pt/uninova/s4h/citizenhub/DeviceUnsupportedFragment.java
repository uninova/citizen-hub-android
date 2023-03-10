package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class DeviceUnsupportedFragment extends Fragment {
    private DeviceViewModel model;
    private Button unsupportedDeviceContinue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_unsupported, container, false);
        final Device device = model.getSelectedDevice().getValue();

        unsupportedDeviceContinue = view.findViewById(R.id.buttonUnsupportedDeviceContinue);

        TextView nameDevice = view.findViewById(R.id.textUnsupportedDevice);
        TextView deviceAddress = view.findViewById(R.id.textUnsupportedDeviceAddress);
        nameDevice.setText(getString(R.string.fragment_configuration_text_view_name, device.getName()));
        deviceAddress.setText(getString(R.string.fragment_configuration_text_view_address, device.getAddress()));
        unsupportedDeviceContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(DeviceUnsupportedFragment.this.requireView()).navigate(DeviceUnsupportedFragmentDirections.actionDeviceUnsupportedFragmentToDeviceListFragment());

            }
        });

        return view;
    }
}

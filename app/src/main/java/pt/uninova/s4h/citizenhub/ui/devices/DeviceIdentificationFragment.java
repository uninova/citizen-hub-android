package pt.uninova.s4h.citizenhub.ui.devices;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.data.Device;

public class DeviceIdentificationFragment extends Fragment {

    private LinearLayout containerLayout;

    DeviceViewModel model;
    Handler uiHandler = new Handler(Looper.getMainLooper());
    private ListView listViewFeature;
    private TextView nameDevice;
    private TextView addressDevice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.fragment_device_configuration_update_test, container, false);

        containerLayout = view.findViewById(R.id.layout_device_configuration_container);
        final Device device = model.getSelectedDevice().getValue();
        nameDevice = view.findViewById(R.id.textConfigurationDeviceNameValue);
        addressDevice = view.findViewById(R.id.textConfigurationAddressValue);
        setHeaderValues(model.getSelectedDevice().getValue());
        System.out.println(device.getName());
        Fragment progressBar = new DeviceConfigurationProgressBarFragment();
        addFragment(progressBar);


        //        ft.add() progressbar fragment
        model.identifySelectedDevice((Agent agent) -> {
            model.getConfigurationAgent().postValue(agent);
            System.out.println("IDENTIFY AGENTTTTTTTTTTTTT" + agent);
            removeFragment(progressBar);
            if (agent == null) {
                Navigation.findNavController(DeviceIdentificationFragment.this.requireView()).navigate(DeviceIdentificationFragmentDirections.actionDeviceIdentificationFragmentToDeviceUnsupportedFragment());
            } else {
                if (model.getSelectedDeviceAgent() != null) {
                    DeviceIdentificationFragment.this.requireActivity().runOnUiThread(() -> {
                        Navigation.findNavController(DeviceIdentificationFragment.this.requireView()).navigate(DeviceIdentificationFragmentDirections.actionDeviceIdentificationFragmentToDeviceConfigurationStreamsFragment());
                    });
                } else {
                    DeviceIdentificationFragment.this.requireActivity().runOnUiThread(() -> {
                        addFragment(new DeviceConfigurationFeaturesFragment());
                        addFragment(new DeviceConfigurationConnectFragment());
                    });
                }
            }

        });

        return view;
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.layout_device_configuration_container, fragment);

        transaction.commitNow();
    }

    private void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment).commit();
    }

    private void setHeaderValues(Device device) {
        if (device != null) {
            nameDevice.setText(device.getName());
            addressDevice.setText(device.getAddress());
        }
    }

}


//TODO Novo fragmento ao carregar no connect, porque só ai é que temos addAgent, por as configs como estavam e depois criar globalIdentifier com um campo de escrita
//TODO guardar referencia dos "new Fragment" para depois poder removê-los sem problema
//TODO criar um novo viewmodel para os agentes temporarios que vêm do deviceSearch para o test e depois para o features


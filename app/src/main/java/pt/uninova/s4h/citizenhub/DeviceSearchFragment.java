package pt.uninova.s4h.citizenhub;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScanner;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;

public class DeviceSearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DeviceListItem> deviceList;
    private DeviceViewModel model;
    public static Device deviceForSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_device_search, container, false);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        model.getDevices().observe(getViewLifecycleOwner(), this::onDeviceUpdate);

        cleanList();
        buildRecycleView(result);

        BluetoothScanner scanner = new BluetoothScanner((BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE));
        scanner.start((address, name) -> {
                    buildRecycleView(result);
                    System.out.println("BT: " + address + " and " + name);
                    deviceList.add(new DeviceListItem(R.drawable.ic_watch_off,
                            new Device(name, address, null, null), R.drawable.ic_settings_off));
                    adapter.notifyItemInserted(0);
        });
        return result;
    }

    private void onDeviceUpdate(List<Device> devices) {
        //not being used
    }

    private void cleanList(){
        deviceList = new ArrayList<>();
    }

    private void buildRecycleView(View result){
        recyclerView = (RecyclerView) result.findViewById(R.id.recyclerView_searchList);
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
                Navigation.findNavController(getView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceAddFragment());
                deviceForSettings = new Device (deviceList.get(position).getmTextTitle(),
                        deviceList.get(position).getmTextDescription(), null,null);
            }

            @Override
            public void onSettingsClick(int position) {
            }
        });
    }
}
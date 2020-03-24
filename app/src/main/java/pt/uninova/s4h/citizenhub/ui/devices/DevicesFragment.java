package pt.uninova.s4h.citizenhub.ui.devices;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gigamole.library.PulseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pt.uninova.s4h.citizenhub.ui.Home;
import pt.uninova.s4h.citizenhub.ui.R;

public class DevicesFragment extends ListFragment {

    private DevicesViewModel galleryViewModel;
    private ArrayList<String> mDeviceList = new ArrayList<>(); //to show
    private Activity mActivity;
    private ArrayAdapter mAdapter;
    private ListView mListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(DevicesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        //root = inflater.inflate(R.layout.backup_list_devices,container,false);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        ((Home) getActivity()).setActionBarTitle("Connected Devices");
        Home.fab.show();

        // Get the application context
        mActivity = getActivity();

        return root;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.i("info","got in broadcast receiver");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device.getName() + "\n" + device.getAddress());
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                getListView().setAdapter(new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1, mDeviceList));
            }
        }
    };

    public BroadcastReceiver  mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<BluetoothDevice> DEVICE_LIST = intent.getParcelableArrayListExtra("DEVICE_LIST");
            if (DEVICE_LIST != null) {
                for (int i = 0; i < DEVICE_LIST.size(); i++) {
                    String deviceName = DEVICE_LIST.get(i).getName();
                    String deviceAddress = DEVICE_LIST.get(i).getAddress();
                    if (!mDeviceList.contains(deviceName + "\n" + deviceAddress)) {
                        mDeviceList.add(deviceName + "\n" + deviceAddress);
                    }
                }
                // Initialize an array adapter
                mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_search_details,mDeviceList){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        ((Home) getActivity()).setActionBarTitle("Select a Device");
                        // Get the view
                        LayoutInflater inflater = mActivity.getLayoutInflater();
                        View itemView = inflater.inflate(R.layout.device_search_details,null,true);

                        // Get current device name
                        TextView deviceName = itemView.findViewById(R.id.device_name);
                        TextView deviceDetails = itemView.findViewById(R.id.device_details);
                        String deviceNameString = (String) getListView().getAdapter().getItem(position);
                        String[] deviceNameStringSplitted = deviceNameString.split("\n");
                        deviceName.setText(deviceNameStringSplitted[0]);
                        deviceDetails.setText(deviceNameStringSplitted[1]);
                        return itemView;
                    }
                };

                getListView().setAdapter(mAdapter);

                getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String deviceList[]=mDeviceList.get(i).split("\n");
                        Log.d("device",deviceList[0]);
                        Log.d("device", deviceList[1]);
                        BluetoothDevice device = Home.mBluetoothManager.getAdapter().getRemoteDevice(deviceList[1]);
                        device.createBond();

                        Home.fab.show();
                        ((Home) getActivity()).setActionBarTitle("Connected Devices");
                        getListView().setAdapter(null);

                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                        ArrayList<String> s = new ArrayList<>();
                        for(BluetoothDevice bt : pairedDevices)
                            s.add(bt.getName());
                        mDeviceList.clear();mDeviceList.addAll(s);

                        getListView().setAdapter(new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_list_item_1, mDeviceList));
                    }
                });

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,  new IntentFilter("IntentFilterSendData"));
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }
}
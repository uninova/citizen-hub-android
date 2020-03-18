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
import java.util.Set;

import pt.uninova.s4h.citizenhub.ui.Home;
import pt.uninova.s4h.citizenhub.ui.R;

public class DevicesFragment extends ListFragment {

    private DevicesViewModel galleryViewModel;
    private ArrayList<String> mDeviceList = new ArrayList<>(); //to show
    private Context mContext;
    private Activity mActivity;
    private ConstraintLayout mCLayout;
    private ArrayAdapter mAdapter;
    private ListView mListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(DevicesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        //root = inflater.inflate(R.layout.list_devices,container,false);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        Home.fab.show();

        /** to list bonded
         IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
         getActivity().registerReceiver(mReceiver, filter);
         **/


        // Get the application context
        mContext = getContext();
        mActivity = getActivity();

        // Get the widget reference from XML layout
        mCLayout = root.findViewById(R.id.cL);
        mListView = root.findViewById(R.id.list_view_2);

        return root;
    }

    private void getBluetoothPairedDevices(final ArrayList<String> deviceList){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getActivity(), "This device not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableAdapter, 0);
            }
            Set<BluetoothDevice> all_devices = bluetoothAdapter.getBondedDevices();
            if (all_devices.size() > 0) {
                for (BluetoothDevice currentDevice : all_devices) {
                    deviceList.add("Device Name: "+currentDevice.getName() + "\nDevice Address: " + currentDevice.getAddress());
                    mListView.setAdapter(new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_1, deviceList));
                }
            }
        }
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        //getBluetoothPairedDevices(mDeviceList); //gets BONDED DEVICES //TODO filtrar
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
                mListView.setAdapter(new ArrayAdapter<>(context,
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
                mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_devices,mDeviceList){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        // Get the view
                        Log.i("FSL","i'm here");
                        LayoutInflater inflater = mActivity.getLayoutInflater();
                        View itemView = inflater.inflate(R.layout.list_devices,null,true);
                        PulseView pulseView = itemView.findViewById(R.id.pv);
                        pulseView.startPulse();

                        // Get current device name
                        TextView deviceName = itemView.findViewById(R.id.device_name);
                        String deviceNameString = (String) mListView.getAdapter().getItem(position);
                        String[] deviceNameStringSplitted = deviceNameString.split("\n");
                        deviceName.setText(deviceNameStringSplitted[0]);
                        //for address, use deviceNameStringSplitted[1]
                        return itemView;
                    }
                };

                mListView.setAdapter(mAdapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("info_new", "got here.");
                    }
                });

            }
        }
    };

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String deviceList[]=mDeviceList.get(position).split("\n");
        Log.d("device",deviceList[0]);
        Log.d("device", deviceList[1]);
        BluetoothDevice device = Home.mBluetoothManager.getAdapter().getRemoteDevice(deviceList[1]);
        device.createBond();
    }



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
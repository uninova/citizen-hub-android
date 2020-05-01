package pt.uninova.s4h.citizenhub.ui.devices;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

import datastorage.DeviceDbHelper;
import pt.uninova.s4h.citizenhub.ui.Home;
import pt.uninova.s4h.citizenhub.ui.R;

import static datastorage.DeviceContract.DeviceEntry.TABLE_NAME;

public class DevicesFragment extends ListFragment {
    public ArrayList<String> mDeviceList = new ArrayList<>(); //to show
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.i("info", "got in broadcast receiver");
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
    public ArrayList<String> mDeviceList_aux = new ArrayList<>(); //to show
    OnDataPass dataPasser;
    DeviceDbHelper deviceDbHelper;
    private DevicesViewModel galleryViewModel;
    private Activity mActivity;
    private ArrayAdapter mAdapter;
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //clear list and adapter
            mDeviceList.clear();
            getListView().setAdapter(null);

            ArrayList<BluetoothDevice> DEVICE_LIST = intent.getParcelableArrayListExtra("DEVICE_LIST");
            if (DEVICE_LIST != null) {
                for (int i = 0; i < DEVICE_LIST.size(); i++) {
                    String deviceName = DEVICE_LIST.get(i).getName();
                    String deviceAddress = DEVICE_LIST.get(i).getAddress();
                    //if device is not already connected in the DB

                    //if device is already on the list
                    if (!mDeviceList.contains(deviceName + "\n" + deviceAddress)) {
                        mDeviceList.add(deviceName + "\n" + deviceAddress);
                    }
                }
                // Initialize an array adapter
                mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_search_details, mDeviceList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        ((Home) getActivity()).setActionBarTitle("Select a Device");
                        Home.foundDevice = true;
                        // Get the view
                        LayoutInflater inflater = mActivity.getLayoutInflater();
                        View itemView = inflater.inflate(R.layout.device_search_details, null, true);

                        // Get current device name
                        TextView deviceName = itemView.findViewById(R.id.device_name);
                        TextView deviceDetails = itemView.findViewById(R.id.device_details);
                        ImageView deviceImage = itemView.findViewById(R.id.device_image);
                        deviceImage.setImageResource(R.drawable.ic_device_miband2_disabled);
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
                        String deviceList[] = mDeviceList.get(i).split("\n");
                        Log.i("device", deviceList[0]);
                        Log.i("device", deviceList[1]);

                        if (Home.mBluetoothManager.getAdapter().getBondedDevices().contains(Home.mBluetoothManager.getAdapter().getRemoteDevice(deviceList[1]))) {
                            passData(Home.mBluetoothManager.getAdapter().getRemoteDevice(deviceList[1]));
                        } else {
                            BluetoothDevice device = Home.mBluetoothManager.getAdapter().getRemoteDevice(deviceList[1]);
                            if (device.createBond()) {
                                passData(device);
                            }
                        }
                        clearList();
                        showConnectedList();
                    }
                });

            }
        }
    };
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

        //show previously connected devices


        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        mDeviceList.clear();
        getListView().setAdapter(null);
        deviceDbHelper = new DeviceDbHelper(getContext());
        viewData(deviceDbHelper, mDeviceList);

        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_search_details, mDeviceList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ((Home) getActivity()).setActionBarTitle("Connected Devices");
                Home.fab.show();
                // Get the view
                LayoutInflater inflater = mActivity.getLayoutInflater();
                View itemView = inflater.inflate(R.layout.device_search_details, null, true);

                // Get current device name
                TextView deviceName = itemView.findViewById(R.id.device_name);
                TextView deviceDetails = itemView.findViewById(R.id.device_details);
                ImageView deviceImage = itemView.findViewById(R.id.device_image);
                deviceImage.setImageResource(R.drawable.ic_device_miband2);
                String deviceNameString = (String) getListView().getAdapter().getItem(position);
                String[] deviceNameStringSplitted = deviceNameString.split("\n");
                deviceName.setText(deviceNameStringSplitted[0]);
                deviceDetails.setText(deviceNameStringSplitted[1]);
                return itemView;
            }
        };
        getListView().setAdapter(mAdapter);
    }

    public void showConnectedList() {
        deviceDbHelper = new DeviceDbHelper(getContext());
        viewData(deviceDbHelper, mDeviceList);

        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_search_details, mDeviceList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ((Home) getActivity()).setActionBarTitle("Connected Devices");
                Home.fab.show();
                // Get the view
                LayoutInflater inflater = mActivity.getLayoutInflater();
                View itemView = inflater.inflate(R.layout.device_search_details, null, true);

                // Get current device name
                TextView deviceName = itemView.findViewById(R.id.device_name);
                TextView deviceDetails = itemView.findViewById(R.id.device_details);
                ImageView deviceImage = itemView.findViewById(R.id.device_image);
                deviceImage.setImageResource(R.drawable.ic_device_miband2);
                String deviceNameString = (String) getListView().getAdapter().getItem(position);
                String[] deviceNameStringSplitted = deviceNameString.split("\n");
                deviceName.setText(deviceNameStringSplitted[0]);
                deviceDetails.setText(deviceNameStringSplitted[1]);
                return itemView;
            }
        };
        getListView().setAdapter(mAdapter);

    }

    public void clearList() {
        //clear list and adapter
        mDeviceList.clear();
        getListView().setAdapter(null);
    }


    public Cursor ViewData(DeviceDbHelper deviceDbHelper) {
        SQLiteDatabase db = deviceDbHelper.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    private void viewData(DeviceDbHelper deviceDbHelper, ArrayList<String> mDeviceList) {
        Cursor cursor = ViewData(deviceDbHelper);
        if (cursor.getCount() == 0) {
            Log.i("DATABASESS", "No devices to show");
        } else {
            while (cursor.moveToNext()) {
                Log.i("DATABASESS", "Valores da tabela:" + cursor.getString(1) + cursor.getString(2));
                mDeviceList.add(cursor.getString(1) + "\n" + cursor.getString(2));
            }
        }
    }

    public void passData(BluetoothDevice device) {
        dataPasser.onDataPass(device);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    /**
     * Register's the receiver
     */

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("IntentFilterSendData"));

    }

    /**
     * Unregisters the receiver
     */

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    /**
     * Interface to send a whole device to activity
     */
    public interface OnDataPass {
        public void onDataPass(BluetoothDevice device);
    }

}
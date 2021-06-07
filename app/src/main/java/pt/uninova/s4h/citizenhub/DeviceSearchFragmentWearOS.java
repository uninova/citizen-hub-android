package pt.uninova.s4h.citizenhub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pt.uninova.s4h.citizenhub.persistence.Device;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class DeviceSearchFragmentWearOS extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 77;
    private static final int FEATURE_BLUETOOTH_STATE = 78;

    private DeviceListAdapter adapter;
    private ArrayList<DeviceListItem> deviceList;
    private DeviceViewModel model;
    public static Device deviceForSettings;
    LayoutInflater localInflater;
    ViewGroup localContainer;
    private GoogleApiClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        localInflater = inflater;
        localContainer = container;

        final View result = inflater.inflate(R.layout.fragment_device_search, container, false);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        model.getDevices().observe(getViewLifecycleOwner(), this::onDeviceUpdate);

        cleanList();
        buildRecycleView(result);

        //Check if WearOS is installed
        checkWearOSPackage(getActivity().getPackageManager());

        //Retrieve Nodes
        retrieveDeviceNode();



        return result;
    }

    private void checkWearOSPackage(PackageManager packageManager){
        try {
            packageManager.getPackageInfo("com.google.android.wearable.app", PackageManager.GET_META_DATA);
            System.out.println("WearOS App is installed!");
        } catch (PackageManager.NameNotFoundException e) {
            //android wear app is not installed
            System.out.println("WearOS App is not installed!");
            new AlertDialog.Builder(getContext())
                    .setMessage("WearOS App is not installed on your device. Please install and try again.")
                    .setTitle("WearOS App not found.")
                    .setPositiveButton("OK", (paramDialogInterface, paramInt) ->
                            Navigation.findNavController(requireView()).navigate(DeviceSearchFragmentWearOSDirections.actionDeviceSearchWearosFragmentToDeviceListFragment()))
                    .show();
        }
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        if (client == null)
            client = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .build();
        return client;
    }

    private void retrieveDeviceNode() {
        final GoogleApiClient client = getGoogleApiClient(getContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.blockingConnect(1000, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    //TODO: showing multiple devices is not tested yet
                    for (Node node : nodes)
                    {
                        String nodeId = node.getId();
                        String nodeName = node.getDisplayName();
                        System.out.println("node found success!" + " " + nodeId + " " + nodeName);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addItem(nodeName,nodeId);
                            }
                        });
                    }
                } else {
                    System.out.println("nodes no success!");
                }
                client.disconnect();
            }
        }).start();
    }

    private void addItem(String nodeName, String nodeId) {
        buildRecycleView(requireView());
        Device device = new Device(nodeName, nodeId, null, null);
        deviceList.add(new DeviceListItem(device, R.drawable.ic_watch_off, R.drawable.ic_settings_off));
        adapter.notifyItemInserted(0);
    }

    private void onDeviceUpdate(List<Device> devices) {

    }

    private void cleanList() {
        deviceList = new ArrayList<>();
    }

    private void buildRecycleView(View result) {
        RecyclerView recyclerView = result.findViewById(R.id.recyclerView_searchList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new DeviceListAdapter(deviceList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                model.setDevice(deviceList.get(position).getDevice());
                Navigation.findNavController(requireView()).navigate(DeviceSearchFragmentWearOSDirections.actionDeviceSearchFragmentToDeviceAddConfigurationFragment());

                DeviceListFragment.deviceForSettings = new Device(deviceList.get(position).getName(),
                        deviceList.get(position).getAddress(), null, null);
            }

            @Override
            public void onSettingsClick(int position) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

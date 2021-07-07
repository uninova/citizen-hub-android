package pt.uninova.s4h.citizenhub;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceListFragment extends Fragment
//        implements AgentListener
{


    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DeviceListItem> deviceList;
    private Application app;
    private View resultView;
    public static Device deviceForSettings;
    private Button searchDevices;
    TextView noDevices;
    public static MutableLiveData<DeviceListItem> asd;
    private DeviceViewModel model;
    Device device;
    private MutableLiveData<List<DeviceListItem>> listLiveData;
//    private Dispatcher<StateChangedMessage<AgentListEvent>> eventMessageDispatcher;

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_device_list, menu);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = requireActivity().getApplication();
        deviceList = new ArrayList<>();
//        cleanList();
        System.out.println("ONCREATEEEEEEEEEEEEEEEEEE");

        System.out.println(" DEVICE_LIST_TAMANHO" + "..........................." + deviceList.size());
        if (adapter != null)
            adapter.notifyDataSetChanged();
//        eventMessageDispatcher = new Dispatcher<>();

        //        asd = new
//        asd.observe(this, new Observer<DeviceListItem>() {
//            @Override
//            public void onChanged(DeviceListItem deviceListItem) {
//            deviceList.add(deviceListItem);
//            adapter.notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_list, container, false);
        resultView = result;

        searchDevices = result.findViewById(R.id.searchButton);
        noDevices = result.findViewById(R.id.fragment_device_list_no_data);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
//        listLiveData= new MutableLiveData<>();
        if (adapter == null) {
            buildRecycleView(result);
        }

        if (((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().getDeviceAgentMap().size() > 0) {


            System.out.println("ENTROUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
            for (Device device : ((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().getDevicesFromMap()
            ) {
                deviceList.add(new DeviceListItem(device));
                adapter.notifyDataSetChanged();

            }
        }
        Activity activity = requireActivity();
        ((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().addAgentEventListener(value -> {
            System.out.println("AGENT_TAMANHO" + "-----------------------------------" + value.getDeviceList().size());
            System.out.println(" DEVICE_LIST_TAMANHO" + "-----------------------------------" + deviceList.size());
            deviceList.clear();
            for (Device device : value.getDeviceList()) {
                deviceList.add(new DeviceListItem(device));
            }

            activity.runOnUiThread(() -> adapter.notifyDataSetChanged());

        });
//                listLiveData.getValue().add(new DeviceListItem(device));
//
//
////                listLiveData.postValue(deviceList);
////                model.setDevice(device);
////                model.apply();
//                    System.out.println("TAMANHO DEVICE AGENT MAP" + ((CitizenHubServiceBound) requireActivity()).getService().getAgentOrchestrator().getDevicesFromMap().size());
//
//                    deviceList.add(new DeviceListItem(device, R.drawable.ic_watch, R.drawable.ic_settings));
//                    System.out.println("TAMANHO DEVICE LIST 1" + deviceList.size());
//                    if (adapter != null) // it works second time and later
//                        adapter.notifyDataSetChanged();
//                }
//            }

//            AgentNotification agentNotification = new AgentNotification();


//        model.getDevices().observe(getViewLifecycleOwner(), this::onDeviceUpdate);
//
//            agentNotification.agentAddEvent(device).observe(getViewLifecycleOwner(), new Observer<DeviceListItem>() {
//                @Override
//                public void onChanged(DeviceListItem deviceListItem) {
//                    if (deviceListItem.getDevice() != null && deviceListItem.getAddress() != null) {
//                        device = deviceListItem.getDevice();
//                        deviceList.add(new DeviceListItem(device));
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            });
//
//            agentNotification.agentRemoveEvent(device).observe(getViewLifecycleOwner(), new Observer<DeviceListItem>() {
//                @Override
//                public void onChanged(DeviceListItem deviceListItem) {
//                    if (deviceListItem.getDevice() != null && deviceListItem.getAddress() != null) {
//                        device = deviceListItem.getDevice();
//                        deviceList.remove(new DeviceListItem(device));
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            });


        //TODO não aceder à db, ir buscar ao Orchestrator
//        cleanList();
        System.out.println("TAMANHO DEVICE LIST 2" + deviceList.size());
        buildRecycleView(result);
        setHasOptionsMenu(false); //shows Action Bar menu button

        searchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConnectedDialog(result);
            }
        });

        return result;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

    }

    private void showConnectedDialog(View result) {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_connection);

        //initialize Views and Buttons
        Switch wearOSButton = dialog.findViewById(R.id.wearOSButton);
        Switch bluetoothButton = dialog.findViewById(R.id.bluetoothButton);
        Button submitButton = dialog.findViewById(R.id.submitButton);

        //submitting selection
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if wearOS and BT are checked
                if (bluetoothButton.isChecked() && wearOSButton.isChecked()) {
                    //TODO this is only showing bluetooth when both selected, because the search is not yet combined
                    Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment());
                    dialog.dismiss();
                } else if (bluetoothButton.isChecked()) {
                    Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment());
                    dialog.dismiss();
                } else if (wearOSButton.isChecked()) {
                    Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchWearosFragment());

                    dialog.dismiss();
                } else {
                    //does nothing, nothing was selected
                }
            }
        });

        dialog.show();
    }

    private void onDeviceUpdate(List<Device> devices) {
        cleanList();
        for (Device device : devices) {
            deviceList.add(new DeviceListItem(device, R.drawable.ic_watch, R.drawable.ic_settings));
        }
        buildRecycleView(resultView);
    }

    private void cleanList() {
        deviceList = new ArrayList<>();
    }


    private void buildRecycleView(View result) {
        recyclerView = result.findViewById(R.id.recyclerView_devicesList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new DeviceListAdapter(deviceList);
        System.out.println("TAMANHO DEVICE LIST 3" + deviceList.size());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        System.out.println("Lista vazia?" + " " + deviceList.isEmpty());
        emptyListCheck();

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onSettingsClick(int position) {
                model.setDevice(deviceList.get(position).getDevice());
                Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceConfigurationUpdateFragment());
                /*deviceForSettings = new Device(deviceList.get(position).getName(),
                        deviceList.get(position).getAddress(), null, null);*/
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        emptyListCheck();

        if (adapter != null)
            adapter.notifyDataSetChanged();
        else {
            buildRecycleView(requireView());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void emptyListCheck() {
        if (deviceList.isEmpty()) {
            noDevices.setVisibility(View.VISIBLE);
        } else {
            noDevices.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if (item.getItemId() == R.id.menu_fragment_device_list_search) {
            new AlertDialog.Builder(getContext())
                    .setMessage("Please select one method of connection compatible with your device.")
                    .setTitle("Method of Connection")
                    .setPositiveButton("Bluetooth", (paramDialogInterface, paramInt) ->
                        Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment()))
                    .setNegativeButton("Wear OS", (paramDialogInterface, paramInt) ->
                            Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchWearosFragment()))
                    .show();
        }*/
        return super.onOptionsItemSelected(item);
    }

}

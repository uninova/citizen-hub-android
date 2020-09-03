package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.os.Bundle;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.DailySummary;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;

import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBinder;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceListFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DeviceListItem> deviceList;
    private Application app;

    private DeviceViewModel model;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_device_list, menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Application) requireActivity().getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_list, container, false);

        final DeviceRepository repo = new DeviceRepository(this.getActivity().getApplication());

        //String name, String address, String type, String state
        Device device = new Device("a","a","a","a");
        Device device2 = new Device("b","b","b","b");
        //System.out.println(device.getAddress());
        repo.add(device);repo.add(device2);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        model.getDevices().observe(getViewLifecycleOwner(), this::onDeviceUpdate); //when is it?

        //createList();
        //buildRecycleView(result);

        setHasOptionsMenu(true);

        return result;
    }

    private void onDeviceUpdate(List<Device> devices) {
        deviceList = new ArrayList<>();
        //deviceList.addAll(devices);
        for(Device device : devices) {
            System.out.println(device.getName());
        }
        //caloriesTextView.setText(getString(R.string.fragment_summary_text_view_calories_text, dailySummary.getSumCalories()));
        //buildRecycleView(result);
    }


    private void createList(){
        deviceList = new ArrayList<>(); //TODO remove this, testing
        DeviceRepository repo = new DeviceRepository(app);
        DeviceViewModel deviceViewModel = new DeviceViewModel(app);
        Device device1 = new Device("HexoTest", "39:42:45:69", "Health", "on");
        Device device2 = new Device("ChineseHexo", "39:42:45:69", "Unknown", "off");
        Device device3 = new Device("KBZ", "39:42:45:69", "Health", "on");

        repo.add(device1);
        repo.add(device2);
        repo.add(device3);

        for(int i=0; i<10; i++) {


            deviceList.add(new DeviceListItem(R.drawable.ic_watch, "Watch1", "It's good.", R.drawable.ic_settings));
            deviceList.add(new DeviceListItem(R.drawable.ic_watch_off, "Watch2", "It's bad.", R.drawable.ic_settings_off));
            deviceList.add(new DeviceListItem(R.drawable.ic_watch, "Watch3", "It's meh.", R.drawable.ic_settings));
        }
    }

    private void buildRecycleView(View result){
        recyclerView = (RecyclerView) result.findViewById(R.id.recyclerView_devicesList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new DeviceListAdapter(deviceList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //insertItem(position); //TODO remove this, testing
                //removeItem(position); //TODO remove this, testing
                onoffItem(position);
            }

            @Override
            public void onSettingsClick(int position) {
                Navigation.findNavController(getView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceDetailFragment());
            }
        });
    }

    public void insertItem(int position){
        DeviceRepository repo = new DeviceRepository(app);
        Device deviceTest = new Device();
        repo.add(deviceTest);
        deviceList.add(position, new DeviceListItem(R.drawable.ic_about_fragment, "This is a new watch", "Hello World", R.drawable.ic_settings)); //TODO change this, testing
        adapter.notifyItemInserted(position);
    }

    public void removeDevice(Device device){
        DeviceRepository repo = new DeviceRepository(app);
        repo.remove(device);
    }

    public void insertDevice(Device device){
        DeviceRepository repo = new DeviceRepository(app);
        repo.add(device);
    }

    public void removeItem(int position){

        deviceList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void onoffItem(int position){ //TODO this is for testing
        if (deviceList.get(position).getmImageResource() == R.drawable.ic_watch) {
            deviceList.get(position).changeImageResource(R.drawable.ic_watch_off);
            deviceList.get(position).changeImageSettings(R.drawable.ic_settings_off);
        }
        else {
            deviceList.get(position).changeImageResource(R.drawable.ic_watch);
            deviceList.get(position).changeImageSettings(R.drawable.ic_settings);
        }
        adapter.notifyItemChanged(position);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            removeItem(position);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_fragment_device_list_search) {
            Navigation.findNavController(getView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment());
//            Intent intent = new Intent(this, CitizenHubService.class);
//
           // getActivity().startService(new Intent(getActivity(),CitizenHubService.class));
            getService().StartScan();

        }
        return super.onOptionsItemSelected(item);
    }

   public CitizenHubService getService (){
       CitizenHubServiceBound activity = (CitizenHubServiceBound) getActivity();
       assert activity != null;
       return activity.getService();

   }

}
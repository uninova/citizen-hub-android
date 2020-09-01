package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeviceSearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DeviceListItem> deviceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_device_search, container, false);

        createList();

        buildRecycleView(result);

        return result;
    }

    private void createList(){
        deviceList = new ArrayList<>(); //TODO remove this, testing
        deviceList.add(new DeviceListItem(R.drawable.ic_watch_off, "Watch1", "It's good.", R.drawable.ic_settings_off));
        deviceList.add(new DeviceListItem(R.drawable.ic_watch_off, "Watch2", "It's bad.", R.drawable.ic_settings_off));
        deviceList.add(new DeviceListItem(R.drawable.ic_watch_off, "Watch3", "It's meh.", R.drawable.ic_settings_off));

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
                //insertItem(position); //TODO remove this, testing
                //removeItem(position); //TODO remove this, testing
                //onoffItem(position);
                Navigation.findNavController(getView()).navigate(DeviceSearchFragmentDirections.actionDeviceSearchFragmentToDeviceAddFragment());
            }

            @Override
            public void onSettingsClick(int position) {
                //Navigation.findNavController(getView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceDetailFragment());
            }
        });
    }



}
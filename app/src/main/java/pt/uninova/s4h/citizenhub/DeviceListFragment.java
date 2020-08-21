package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_device_list, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_list, container, false);

        ArrayList<DeviceListItem> devicesList = new ArrayList<>();
        int deviceCounter = 1;
        for(int i=0; i<10; i++) {
            devicesList.add(new DeviceListItem(R.drawable.ic_device_miband2, "Watch1", "It's good.", String.valueOf(deviceCounter++)));
            devicesList.add(new DeviceListItem(R.drawable.ic_device_miband2_disabled, "Watch2", "It's bad.", String.valueOf(deviceCounter++)));
            devicesList.add(new DeviceListItem(R.drawable.ic_device_miband2, "Watch3", "It's meh.", String.valueOf(deviceCounter++)));
        }


        recyclerView = (RecyclerView) result.findViewById(R.id.recyclerView_devicesList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new DeviceListAdapter(devicesList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        setHasOptionsMenu(true);

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_fragment_device_list_search) {
            Navigation.findNavController(getView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceSearchFragment());
        }

        return super.onOptionsItemSelected(item);
    }
}
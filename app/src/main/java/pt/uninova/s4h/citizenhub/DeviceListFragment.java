package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class DeviceListFragment extends Fragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_device_list, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_list, container, false);

        TextView deviceName = result.findViewById(R.id.text_view_title);
        TextView deviceDetails = result.findViewById(R.id.text_view_description);
        deviceName.setText("deviceNameStringSplitted[0]");
        deviceDetails.setText("deviceNameStringSplitted[1]");

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
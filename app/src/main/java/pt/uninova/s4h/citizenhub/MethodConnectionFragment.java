package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodConnectionFragment extends Fragment {

    List<String> connectionMethodsAvailable = new ArrayList<>(Arrays.asList("Bluetooth", "WearOS"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_connection_method, container, false);

        ListView listConnectionMethods = result.findViewById(R.id.listViewConnectionMethods);

        ArrayList<String> connectionMethods = new ArrayList<>(connectionMethodsAvailable);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                connectionMethods);
        listConnectionMethods.setAdapter(arrayAdapter);

        listConnectionMethods.setOnItemClickListener((adapterView, view, i, l) -> {
            if (connectionMethods.get(i).equals("WearOS"))
                Navigation.findNavController(requireView()).navigate(MethodConnectionFragmentDirections.actionDeviceConnectionMethodFragmentToDeviceSearchWearosFragment());
            if (connectionMethods.get(i).equals("Bluetooth"))
                Navigation.findNavController(requireView()).navigate(MethodConnectionFragmentDirections.actionDeviceConnectionMethodFragmentToDeviceSearchFragment());
        });

        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

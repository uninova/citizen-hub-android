package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.Device;

public class DeviceDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_device_detail, container, false);

        TextView detailText = result.findViewById(R.id.text_detail);
        Device device = DeviceListFragment.deviceForSettings;

        detailText.setText("Name: " + device.getName() + "\n" + "Address: " + device.getAddress());

        return result;
    }

}
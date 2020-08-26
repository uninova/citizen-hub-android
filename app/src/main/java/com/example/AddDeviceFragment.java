package com.example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceDao;
import pt.uninova.s4h.citizenhub.persistence.DeviceViewModel;

public class AddDeviceFragment extends Fragment {
    public static final String EXTRA_TITLE =
            "pt.uninova.s4h.citizenhub.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "pt.uninova.s4h.citizenhub.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "pt.uninova.s4h.citizenhub.EXTRA_PRIORITY";
    DeviceViewModel deviceViewModel;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private CitizenHubDatabase db;
    private DeviceDao deviceDAO;

    public static AddDeviceFragment getInstance() {
        Bundle bundle = new Bundle();
        AddDeviceFragment fragment = new AddDeviceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.example_activity_add_device, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editTextTitle = requireView().findViewById(R.id.edit_text_title);
        editTextDescription = requireView().findViewById(R.id.edit_text_description);
        numberPickerPriority = requireView().findViewById(R.id.number_picker_priority);
        //  numberPickerPriority.setMinValue(1);
        // numberPickerPriority.setMaxValue(10);
        Button buttonAddDevice = getView().findViewById(R.id.submit);
        buttonAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDevice();
            }
        });

        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        // setTitle("Add Device");
    }

    private void saveDevice() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(getActivity(), "Please insert a name and address", Toast.LENGTH_SHORT).show();
            return;
        }
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        Device device = new Device(title, description, String.valueOf(priority), "state");
        deviceViewModel.insert(device);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveDevice();

        return super.onOptionsItemSelected(item);
    }

}
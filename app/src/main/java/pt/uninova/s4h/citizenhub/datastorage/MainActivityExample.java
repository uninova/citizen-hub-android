package pt.uninova.s4h.citizenhub.datastorage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import pt.uninova.s4h.citizenhub.ui.R;

//Todo Useless test example class, delete in future
//Room DB structure: https://prnt.sc/tiejdz
//https://www.youtube.com/watch?v=reSPN7mgshI
//https://developer.android.com/training/data-storage/room/creating-views

public class MainActivityExample extends AppCompatActivity {
    private DeviceViewModel deviceViewModel;
    public static final int ADD_DEVICE_REQUEST = 37;
    CitizenDatabaseClient db;
    DeviceDAO deviceDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddDevice = findViewById(R.id.button_add_device);
        buttonAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityExample.this,AddDeviceActivity.class);
                startActivityForResult(intent,ADD_DEVICE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        DeviceAdapter adapter = new DeviceAdapter();
        recyclerView.setAdapter(adapter);

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        deviceViewModel.getAllDevices().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(@Nullable List<Device> devices) {
               adapter.setDevices(devices);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_DEVICE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddDeviceActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddDeviceActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddDeviceActivity.EXTRA_PRIORITY, 1);
            Device device = new Device(title, description,String.valueOf(priority), "state");
            DeviceRepository deviceRepository = new DeviceRepository(getApplication());
          deviceRepository.insert(device);
            Toast.makeText(this, "Device saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Device not saved", Toast.LENGTH_SHORT).show();
        }
        }
    }


package pt.uninova.s4h.citizenhub.datastorage;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import pt.uninova.s4h.citizenhub.ui.R;

//Todo Useless test example class, delete in future
//Room DB structure: https://prnt.sc/tiejdz
//https://www.youtube.com/watch?v=reSPN7mgshI

public class MainActivityExample extends AppCompatActivity {
    private DeviceViewModel deviceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        deviceViewModel.getAllDevices().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(@Nullable List<Device> devices) {
                //update RecyclerView
                Toast.makeText(getApplicationContext(), "onChanged", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
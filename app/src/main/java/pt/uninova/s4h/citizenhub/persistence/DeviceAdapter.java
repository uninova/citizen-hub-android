package pt.uninova.s4h.citizenhub.persistence;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {
    private List<Device> devices = new ArrayList<>();

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.example_device_item, parent, false);
        return new DeviceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
        Device currentDevice = devices.get(position);

    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    class DeviceHolder extends RecyclerView.ViewHolder {


        public DeviceHolder(View itemView) {
            super(itemView);

        }
    }
}

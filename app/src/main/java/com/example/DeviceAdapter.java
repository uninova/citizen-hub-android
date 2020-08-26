package com.example;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pt.uninova.s4h.citizenhub.persistence.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {
    int layoutId;
    private List<Device> devices = new ArrayList<>();

    public DeviceAdapter(int layoutId) {
        super();
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
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

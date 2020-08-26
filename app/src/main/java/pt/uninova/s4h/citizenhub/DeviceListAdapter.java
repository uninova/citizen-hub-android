package pt.uninova.s4h.citizenhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {

    private ArrayList<DeviceListItem> devicesList;

    public DeviceListAdapter(ArrayList<DeviceListItem> listDevices) {
        devicesList = listDevices;
    }

    @NonNull
    @Override
    public DeviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_device_item, parent, false);
        DeviceListViewHolder hol = new DeviceListViewHolder(v);
        return hol;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListViewHolder holder, int position) {
        DeviceListItem currentItem = devicesList.get(position);
        holder.textTitle.setText(currentItem.getmTextTitle());
        holder.textDescription.setText(currentItem.getmTextDescription());
        holder.image.setImageResource(currentItem.getmImageResource());
        holder.textNumber.setText(currentItem.getmTextNumber());
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    public static class DeviceListViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView textTitle;
        public TextView textDescription;
        public TextView textNumber;

        public DeviceListViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_device);
            textTitle = itemView.findViewById(R.id.text_view_title);
            textDescription = itemView.findViewById(R.id.text_view_description);
            textNumber = itemView.findViewById(R.id.text_device);
        }
    }
}

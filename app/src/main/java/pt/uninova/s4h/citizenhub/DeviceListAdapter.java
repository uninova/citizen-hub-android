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
    private OnItemClickListener listener;

    public DeviceListAdapter(ArrayList<DeviceListItem> listDevices) {
        devicesList = listDevices;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onSettingsClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listen){listener = listen;}

    @NonNull
    @Override
    public DeviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_device_list_item,parent,false);
        DeviceListViewHolder hol = new DeviceListViewHolder(v, listener);
        return hol;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListViewHolder holder, int position) {
        DeviceListItem currentItem = devicesList.get(position);
        holder.textTitle.setText(currentItem.getName());
        holder.textDescription.setText(currentItem.getAddress());
        holder.image.setImageResource(currentItem.getImageResource());
        holder.imageSettings.setImageResource(currentItem.getImageSettings());
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    public static class DeviceListViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView textTitle;
        public TextView textDescription;
        public ImageView imageSettings;

        public DeviceListViewHolder(@NonNull View itemView, final OnItemClickListener listen) {
            super(itemView);
            image = itemView.findViewById(R.id.image_device);
            textTitle =itemView.findViewById(R.id.text_view_title);
            textDescription =itemView.findViewById(R.id.text_view_description);
            imageSettings = itemView.findViewById(R.id.image_view_settings);

            itemView.setOnClickListener(view -> {
                if(listen != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listen.onItemClick(position);
                    }
                }
            });

            imageSettings.setOnClickListener(view -> {
                if(listen != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listen.onSettingsClick(position);
                    }
                }
            });
        }
    }
}

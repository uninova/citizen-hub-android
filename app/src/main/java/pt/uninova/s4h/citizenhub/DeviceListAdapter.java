package pt.uninova.s4h.citizenhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import pt.uninova.s4h.citizenhub.data.Device;


public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {

    public interface OnItemClickListener {

        void onItemClick(DeviceListItem item);

    }

    public static class DeviceListViewHolder extends RecyclerView.ViewHolder {

        private final OnItemClickListener listener;

        public DeviceListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            this.listener = listener;
        }

        public void setItem(DeviceListItem item) {
            final ImageView imageView = itemView.findViewById(R.id.image_method);
            final TextView titleTextView = itemView.findViewById(R.id.text_view_title);
            final TextView descriptionTextView = itemView.findViewById(R.id.text_view_description);

            imageView.setImageResource(item.getImageResource());
            descriptionTextView.setText(item.getDevice().getAddress());
            titleTextView.setText(item.getDevice().getName());

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    private final List<DeviceListItem> deviceListItems;
    private final OnItemClickListener listener;

    public DeviceListAdapter(OnItemClickListener listener) {
        this.deviceListItems = new LinkedList<>();
        this.listener = listener;
    }

    public void addItem(DeviceListItem deviceListItem) {
        deviceListItems.add(deviceListItem);
        notifyItemInserted(deviceListItems.size() - 1);
    }

    public void clear() {
        deviceListItems.clear();
    }

    @Override
    public int getItemCount() {
        return deviceListItems.size();
    }

    @Override
    public void onBindViewHolder(DeviceListViewHolder holder, int position) {
        holder.setItem(deviceListItems.get(position));
    }

    @Override
    public DeviceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_device_list_item, parent, false);

        return new DeviceListViewHolder(view, listener);
    }

    public void removeItem(Device device) {
        for (DeviceListItem i : deviceListItems) {
            if (device.equals(i.getDevice())) {
                deviceListItems.remove(i);
                return;
            }
        }
    }
}

package pt.uninova.s4h.citizenhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

class FeatureListAdapter extends BaseAdapter {

    private final List<FeatureListItem> data;
    private static LayoutInflater inflater = null;
    private FeatureListAdapter.OnCheckedChangeListener listener;

    public void setOnItemClickListener(FeatureListAdapter.OnCheckedChangeListener listen) {
        listener = listen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_item_feature, null);
        Switch nameSwitch = vi.findViewById(R.id.switchFeature);
        final FeatureListItem feature = data.get(position);
        if (feature.isActive()) {
            data.get(position).setActive(true);
        }
        nameSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(position).setActive(isChecked);
            }
        });
        TextView text = vi.findViewById(R.id.textFeature);
        String lastString = capitalizeString(data.get(position).getMeasurementKind().toString().toLowerCase().replace("_", " "));
        text.setText(lastString);

        return vi;
    }

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public FeatureListAdapter(Context context, List<FeatureListItem> data) {
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }


    public Object getItem(int position) {
        return data.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(int position);
    }

    public static String capitalizeString(String string) {
        String[] arr = string.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
        }
}
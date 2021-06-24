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
    CompoundButton.OnCheckedChangeListener switchListener;

    public void setOnItemClickListener(FeatureListAdapter.OnCheckedChangeListener listen) {
        listener = listen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_item_feature, null);
        Switch nameSwitch = vi.findViewById(R.id.switchFeature);
        switchListener = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // System.out.println(isChecked + " "+ position+ " "+data.get(position).isActive());
                data.get(position).setActive(isChecked);
                notifyDataSetChanged();
                // System.out.println(isChecked + " "+ position+ " "+data.get(position).isActive());
            }
        };
        nameSwitch.setOnCheckedChangeListener(null);
        // System.out.println(position+" "+ data.get(position).getMeasurementKind()+" "+data.get(position).isActive());
        nameSwitch.setChecked(data.get(position).isActive());
        nameSwitch.setOnCheckedChangeListener(switchListener);

        TextView text = vi.findViewById(R.id.textFeature);
        String lastString = capitalizeString(data.get(position).getMeasurementKind().toString().toLowerCase().replace("_", " "));
        text.setText(lastString);

        return vi;
    }


    public FeatureListAdapter(Context context, List<FeatureListItem> data) {
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateResults(List<FeatureListItem> results) {
        data.clear();
        data.addAll(results);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
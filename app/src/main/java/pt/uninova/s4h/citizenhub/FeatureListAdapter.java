package pt.uninova.s4h.citizenhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class FeatureListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private final List<FeatureListItem> data;
    CompoundButton.OnCheckedChangeListener switchListener;
    private static SwitchCompat nameSwitch;

    public FeatureListAdapter(Context context, List<FeatureListItem> data) {
        this.data = data;
        Collections.sort(this.data, Comparator.comparing(FeatureListItem::getLabel));
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_item_feature, null);

        nameSwitch = vi.findViewById(R.id.switchFeature);

        switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(position).setActive(isChecked);
                FeatureListAdapter.this.notifyDataSetChanged();
            }
        };

        nameSwitch.setOnCheckedChangeListener(null);
        nameSwitch.setChecked(data.get(position).isActive());
        nameSwitch.setOnCheckedChangeListener(switchListener);

        TextView text = vi.findViewById(R.id.textFeature);
        text.setText(data.get(position).getLabel());

        return vi;
    }

    public SwitchCompat getNameSwitch() {
        return nameSwitch;
    }

    public void updateResults(List<FeatureListItem> results) {
        if (data != null) {
            data.clear();
        }
        if (results != null) {
            data.addAll(results);
            Collections.sort(data, Comparator.comparing(FeatureListItem::getLabel));
            this.notifyDataSetChanged();
        }
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

}
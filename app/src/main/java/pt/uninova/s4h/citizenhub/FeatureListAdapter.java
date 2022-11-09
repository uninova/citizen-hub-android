package pt.uninova.s4h.citizenhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;

class FeatureListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private final List<FeatureListItem> data;
    CompoundButton.OnCheckedChangeListener switchListener;
    private final boolean isEnabled;
    LinearLayout layoutFeature;
    Agent agent;
    private MeasurementKindLocalization measurementKindLocalization;


    public FeatureListAdapter(Context context, List<FeatureListItem> data, boolean isSwitchEnabled, Agent agent) {
        this.data = data;
        Collections.sort(this.data, Comparator.comparing(FeatureListItem::getLabel));
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isEnabled = isSwitchEnabled;
        measurementKindLocalization = new MeasurementKindLocalization(context);
        this.agent=agent;
    }

    public FeatureListAdapter(Context context, List<FeatureListItem> data, Agent agent) {
        this.data = data;
        Collections.sort(this.data, Comparator.comparing(FeatureListItem::getLabel));
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        measurementKindLocalization = new MeasurementKindLocalization(context);
        isEnabled = true;
        this.agent = agent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_item_feature, null);
        layoutFeature = vi.findViewById(R.id.layoutFeature);
        SwitchCompat nameSwitch = vi.findViewById(R.id.switchFeature);
        switchListener = (buttonView, isChecked) -> {
            data.get(position).setActive(isChecked);

            for (int i = 0; i < data.size(); i++) {

            final FeatureListItem item = (FeatureListItem) data.get(position);

            int k = item.getFeatureId();

            if (item.isActive()) {
                if (!agent.getEnabledMeasurements().contains(k)) {
                    agent.enableMeasurement(k);
                }
            } else {
                if (agent.getEnabledMeasurements().contains(k)) {
                    agent.disableMeasurement(k);
                }
            }
            }

            FeatureListAdapter.this.notifyDataSetChanged();
        };

        nameSwitch.setOnCheckedChangeListener(null);
        nameSwitch.setChecked(data.get(position).isActive());
        nameSwitch.setOnCheckedChangeListener(switchListener);

        TextView text = vi.findViewById(R.id.textFeature);
        text.setText(data.get(position).getLabel());
        nameSwitch.setClickable(isEnabled);

        if (!isEnabled) {
            layoutFeature.setAlpha(0.5f);
        }
        return vi;
    }


    public void updateResults(List<FeatureListItem> results) {

        if (data != null) {
            data.clear();
        }
        data.addAll(results);
        Collections.sort(data, Comparator.comparing(FeatureListItem::getLabel));

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

}
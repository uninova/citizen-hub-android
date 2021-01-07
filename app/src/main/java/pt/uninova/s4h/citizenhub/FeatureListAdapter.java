package pt.uninova.s4h.citizenhub;

import android.content.Context;
import android.text.method.TextKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.stream.Stream;

import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

class FeatureListAdapter extends BaseAdapter {

    private ArrayList<FeatureListItem> data;
    private static LayoutInflater inflater = null;

    public FeatureListAdapter(Context context, ArrayList<FeatureListItem> data) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_item_feature, null);

        Switch nameSwitch = vi.findViewById(R.id.switchFeature);
        nameSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    data.get(position).setActive(true);
                } else {
                    data.get(position).setActive(false);
                }
            }
        });
        TextView text = (TextView) vi.findViewById(R.id.textFeature);
        String lastString = capitalizeString(data.get(position).getMeasurementKind().toString().toLowerCase().replace("_", " "));
        text.setText(lastString);

        return vi;
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
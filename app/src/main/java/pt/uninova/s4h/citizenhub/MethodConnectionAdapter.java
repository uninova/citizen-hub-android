package pt.uninova.s4h.citizenhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import java.util.ArrayList;

public class MethodConnectionAdapter extends ArrayAdapter<String> {

    public MethodConnectionAdapter(@NonNull Context context, ArrayList<String> methods) {
        super(context, 0, methods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String name = getItem(position);
        if (convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_device_connection_method_list_item, parent, false);

        ImageView methodImage = convertView.findViewById(R.id.image_method);
        TextView methodName = convertView.findViewById(R.id.text_view_title_method);

        if(name.equals("Bluetooth")) {
            methodImage.setImageResource(R.drawable.ic_bluetooth);
            methodName.setText("Bluetooth");
            View finalConvertView = convertView;
            convertView.setOnClickListener(view -> Navigation.findNavController(finalConvertView)
                    .navigate(MethodConnectionFragmentDirections.actionDeviceConnectionMethodFragmentToDeviceSearchFragment()));
        }
        else if (name.equals("WearOS")){
            methodImage.setImageResource(R.drawable.ic_wearos);
            methodName.setText("WearOS");
            View finalConvertView = convertView;
            convertView.setOnClickListener(view -> Navigation.findNavController(finalConvertView)
                    .navigate(MethodConnectionFragmentDirections.actionDeviceConnectionMethodFragmentToDeviceSearchWearosFragment()));
        }

        return convertView;
    }
}

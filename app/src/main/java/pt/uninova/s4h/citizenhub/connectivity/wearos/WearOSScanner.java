package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.content.Context;
import android.util.Log;
import android.view.contentcapture.ContentCaptureCondition;

import androidx.annotation.WorkerThread;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pt.uninova.util.Pair;

public class WearOSScanner {
    private final Map<String,String> devices;
    private WearOSScannerListener listener;
    private static final String TAG = "WearOSScanner";
    private List<Pair<String,String>> deviceList;

    private Context context;

    public WearOSScanner(Context context) {
        devices = new HashMap<>();
        listener = null;
        deviceList = new ArrayList<>();
        this.context = context;
        Log.d(TAG, "Entered"  );

    }

    private void addDevice(String address, String name) {

        if (!devices.containsKey(address)) {
            devices.put(address, name);
            listener.onDeviceFound(address, name);
        }
    }

    public void clearDevices() {
        devices.clear();
    }

    public boolean foundDevice(String address) {
        return devices.containsKey(address);
    }

    public boolean isScanning() {
        return listener != null;
    }

    public synchronized void start(WearOSScannerListener listener) {
        if (!isScanning()) {
            this.listener = listener;
            getWearOsDevices();
        }
    }

    public synchronized void stop() {
        if (isScanning()) {
           // scanner.stopScan(this);
            listener = null;
        }
    }

    @WorkerThread
    public void getWearOsDevices() {

        List<Pair<String,String>> list =new ArrayList<>();


        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(context).getConnectedNodes();

            nodeListTask.addOnSuccessListener(nodes ->
            {for (Node node : nodes) {
                addDevice(node.getId(), node.getDisplayName());
            }}
                );

    }

}

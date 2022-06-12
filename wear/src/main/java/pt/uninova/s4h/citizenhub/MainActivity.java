package pt.uninova.s4h.citizenhub;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.data.HeartRateMeasurement;
import pt.uninova.s4h.citizenhub.data.SnapshotMeasurement;
import pt.uninova.s4h.citizenhub.data.StepsSnapshotMeasurement;
import pt.uninova.s4h.citizenhub.db.DataBaseHelper;
import pt.uninova.s4h.citizenhub.ui.ScreenSlidePagerAdapter;
import pt.uninova.s4h.citizenhub.ui.ZoomOutPageTransformer;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends FragmentActivity {

    public static String nodeIdString;
    private static final String citizenHubPath = "/citizenhub_";
    public static int stepsTotal = 0;
    public static double heartRate = 0;
    public static SensorManager sensorManager;
    public static Sensor stepsSensor, heartSensor;
    private ViewPager2 viewPager;
    static MutableLiveData<String> listenHeartRate = new MutableLiveData<>();
    static MutableLiveData<String> listenSteps = new MutableLiveData<>();
    static MutableLiveData<Boolean> protocolHeartRate = new MutableLiveData<>();
    static MutableLiveData<Boolean> protocolSteps = new MutableLiveData<>();
    static MutableLiveData<Boolean> protocolPhoneConnected = new MutableLiveData<>();
    public static SensorEventListener stepsListener, heartRateListener;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);

        permissionRequest();
        sensorsManager();

        heartRateListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
                    listenHeartRate.setValue(getString(R.string.show_data_heartrate, event.values[0]));
                    long timestamp = new Date().getTime();
                    new SendMessage(citizenHubPath + nodeIdString,event.values[0] + "," + new Date().getTime() + "," + MeasurementKind.HEART_RATE.getId()).start();
                    HeartRateMeasurement heartRateMeasurement = new HeartRateMeasurement((int)event.values[0]);
                    if(!DataBaseHelper.addMeasurement(heartRateMeasurement, dataBaseHelper, timestamp))
                        System.out.println("Failed to insert value in DB.");
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

        stepsListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                    listenSteps.setValue(getString(R.string.show_data_steps, (stepsTotal+=event.values[0])));
                    long timestamp = new Date().getTime();
                    new SendMessage(citizenHubPath + nodeIdString,stepsTotal + "," + new Date().getTime() + "," + MeasurementKind.STEPS.getId()).start();
                    StepsSnapshotMeasurement stepsSnapshotMeasurement = new StepsSnapshotMeasurement(SnapshotMeasurement.TYPE_STEPS_SNAPSHOT, stepsTotal);
                    if(!DataBaseHelper.addMeasurement(stepsSnapshotMeasurement, dataBaseHelper, timestamp))
                        System.out.println("Failed to insert value in DB.");
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

        viewPager = findViewById(R.id.viewPager);
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        viewPager.setCurrentItem(2);
        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);

        new SendMessage(citizenHubPath + nodeIdString,"Ready");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(() -> {
            try {
                Node localNode = Tasks.await(Wearable.getNodeClient(getApplicationContext()).getLocalNode());
                nodeIdString = localNode.getId();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void permissionRequest() {
        if (checkSelfPermission(Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.BODY_SENSORS},
                    21);
            Log.d("Permissions", "REQUESTED");
        } else {
            Log.d("Permissions", "ALREADY GRANTED");
        }
    }

    private void sensorsManager() {
        sensorManager = ((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        heartSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    public static class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("WearOSHeartRateProtocol")){
                protocolHeartRate.setValue(Boolean.parseBoolean(intent.getStringExtra("WearOSHeartRateProtocol")));
                if (Boolean.parseBoolean(intent.getStringExtra("WearOSHeartRateProtocol")))
                    protocolPhoneConnected.setValue(true);
                else
                    protocolPhoneConnected.setValue(false);
            }
            if (intent.hasExtra("WearOSStepsProtocol")){
                protocolSteps.setValue(Boolean.parseBoolean(intent.getStringExtra("WearOSStepsProtocol")));
                if (Boolean.parseBoolean(intent.getStringExtra("WearOSStepsProtocol")))
                    protocolPhoneConnected.setValue(true);
                else
                    protocolPhoneConnected.setValue(false);
            }
            if (intent.hasExtra("WearOSAgent")){
                protocolPhoneConnected.setValue(Boolean.parseBoolean(intent.getStringExtra("WearOSAgent")));
            }
        }
    }

    class SendMessage extends Thread {
        String path;
        String message;
        SendMessage(String p, String msg) {
            path = p;
            message = msg;
        }
        public void run() {
            Task<List<Node>> nodeListTask = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                Task<Node> t = Wearable.getNodeClient(getApplicationContext()).getLocalNode();
                Node n = Tasks.await(t);
                System.out.println("Node associated: " + n.getId() + " Message: " + message);
                List<Node> nodes = Tasks.await(nodeListTask);
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(), path, message.getBytes());
                    try {
                        Integer result = Tasks.await(sendMessageTask);
                    } catch (ExecutionException | InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            } catch (ExecutionException | InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}
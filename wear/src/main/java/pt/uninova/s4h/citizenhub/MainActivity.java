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
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import persistence.MeasurementKind;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/*** Wear works with Bluetooth, ensure phone connectivity & permissions ***/

public class MainActivity extends WearableActivity implements SensorEventListener {

    private String nodeIdString;
    private String citizenHubPath = "/citizenhub_";
    private int stepsTotal = 0;
    private double heartRate = 0;
    private TextView textHeartRate, textSteps, textInfoPhone, textInfoProtocols;
    private Switch switchHeartRate, switchSteps;
    private SensorManager mSensorManager;
    private Sensor mStepSensor, mHeartSensor;
    Boolean wearOSHeartRateProtocol = false, wearOSStepsProtocol = false, wearOSAgent = false;
    private int counter = 0;

    private void sensorsManager() {
        mSensorManager = ((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        mHeartSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textSteps = findViewById(R.id.textSteps);
        textHeartRate = findViewById(R.id.textHearRate);
        textInfoPhone = findViewById(R.id.textInfo);
        textInfoProtocols = findViewById(R.id.textInfo2);

        textSteps.setText(getString(R.string.show_data_steps, stepsTotal));
        textHeartRate.setText(getString(R.string.show_data_heartrate, heartRate));
        textInfoPhone.setText(getString(R.string.show_data_phone_not_connected));
        textInfoProtocols.setText(getString(R.string.show_data_protocols_no_data));

        switchHeartRate = findViewById(R.id.switchHeartRate);
        switchSteps = findViewById(R.id.switchSteps);

        switchHeartRate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Date now = new Date();
            MeasurementKind kind = MeasurementKind.HEART_RATE;
            String msg = kind.getId() + String.valueOf(isChecked) + now;
            String dataPath = citizenHubPath + nodeIdString;
            new SendMessage(dataPath, msg).start();
        });

        switchSteps.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Date now = new Date();
            MeasurementKind kind = MeasurementKind.STEPS;
            String msg = kind.getId() + String.valueOf(isChecked) + now;
            String dataPath = citizenHubPath + nodeIdString;
            new SendMessage(dataPath, msg).start();
        });

        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);

        permissionRequest();
        sensorsManager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread() {
            @Override
            public void run() {
                try {
                    Node localNode = Tasks.await(Wearable.getNodeClient(getApplicationContext()).getLocalNode());
                    nodeIdString = localNode.getId();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
                    mSensorManager.registerListener(MainActivity.this, mStepSensor, mSensorManager.SENSOR_DELAY_NORMAL);
                }
                if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null) {
                    mSensorManager.registerListener(MainActivity.this, mHeartSensor, mSensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        }.start();
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        Date now = new Date();
        double value = event.values[0];
        MeasurementKind kind = MeasurementKind.UNKNOWN;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_HEART_RATE:
                kind = MeasurementKind.HEART_RATE;
                textHeartRate.setText(getString(R.string.show_data_heartrate, value));
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                kind = MeasurementKind.STEPS;
                textSteps.setText(getString(R.string.show_data_steps, (stepsTotal+= value)));
                System.out.println(value + " " + now.getTime() + " " + ++counter);
                break;
        }
        String msg = value + "," + now.getTime() + "," + kind.getId();
        String dataPath = citizenHubPath + nodeIdString;
        new SendMessage(dataPath, msg).start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("WearOSHeartRateProtocol")){
                wearOSHeartRateProtocol = Boolean.parseBoolean(intent.getStringExtra("WearOSHeartRateProtocol"));
            }
            if (intent.hasExtra("WearOSStepsProtocol")){
                wearOSStepsProtocol = Boolean.parseBoolean(intent.getStringExtra("WearOSStepsProtocol"));
            }
            if (intent.hasExtra("WearOSAgent")){
                wearOSAgent = Boolean.parseBoolean(intent.getStringExtra("WearOSHeartRateProtocol"));
            }
            setScreenInfoText();
        }
    }

    private void setScreenInfoText(){
        if (wearOSAgent)
            textInfoPhone.setText(R.string.show_data_phone_connected);
        else
            textInfoPhone.setText(R.string.show_data_phone_not_connected);
        if (wearOSStepsProtocol && wearOSHeartRateProtocol) {
            textInfoPhone.setText(R.string.show_data_phone_connected);
            textInfoProtocols.setText(R.string.show_data_protocols_heartrate_steps);
        }
        else if (wearOSHeartRateProtocol)
        {
            textInfoPhone.setText(R.string.show_data_phone_connected);
            textInfoProtocols.setText(R.string.show_data_protocols_heartrate);
        }
        else if (wearOSStepsProtocol)
        {
            textInfoPhone.setText(R.string.show_data_phone_connected);
            textInfoProtocols.setText(R.string.show_data_protocols_steps);
        }
        else{
            textInfoProtocols.setText(R.string.show_data_protocols_no_data);
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
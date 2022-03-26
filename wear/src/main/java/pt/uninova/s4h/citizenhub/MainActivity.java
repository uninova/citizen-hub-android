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

public class MainActivity extends WearableActivity implements SensorEventListener {


    String onClickMessage, nodeIdString;
    double heartRate = 0, steps = 0;
    String citizenhubPath = "/citizenhub_path_";
    String checkConnectionPath = "checkConnection";
    int stepsTotal = 0;
    private TextView textHeartRate, textSteps, textInfoPhone, textInfoProtocols;
    private SensorManager mSensorManager;
    private Sensor mStepSensor, mHeartSensor;
    private boolean connected = false;
    Boolean wearOSHeartRateProtocol = false, wearOSStepsProtocol = false, wearOSAgent = false;

    private void sensorsManager() {
        mSensorManager = ((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        mHeartSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for testing only. Keeps screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textSteps = findViewById(R.id.textSteps);
        textHeartRate = findViewById(R.id.textHearRate);
        textInfoPhone = findViewById(R.id.textInfo);
        textInfoProtocols = findViewById(R.id.textInfo2);

        textSteps.setText("Steps: " + stepsTotal);
        textHeartRate.setText("Heart Rate: " + heartRate);
        textInfoPhone.setText("Phone not connected");
        textInfoProtocols.setText("Not sending data");

        //Register the local broadcast receiver//
        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);

        //request permissions
        permissionRequest();

        sensorsManager();
        connected = false;
    }

    public boolean getInfo(String msg) {
        new GetLocalNode().start();

        if (msg.equals("Check Connection")) {
            String datapath = citizenhubPath + checkConnectionPath;
            new SendMessage(datapath, msg).start();

        } else {
            String newMsg = toCsvFormat(msg);
            String[] msgArray = newMsg.split(",");
            if (msgArray[3].equals("STEPS")) {
                stepsTotal += (int) Double.parseDouble(msgArray[1]);
                System.out.println((int) Double.parseDouble(msgArray[1]));
                textSteps.setText("Steps: " + stepsTotal);
            }
            String datapath = citizenhubPath + nodeIdString;
            new SendMessage(datapath, newMsg).start();
        }
        return connected;
    }

    public String toCsvFormat(String msg) {
        String newmsg = msg.replace("ID=", "");
        newmsg = newmsg.replace(" VALUE=", ",");
        newmsg = newmsg.replace(" TIME_STAMP=", ",");
        newmsg = newmsg.replace(" KIND=", ",");
        newmsg = newmsg.replace(" IS_SENT=", ",");
        return newmsg;
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

    private int counter = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        Date now = new Date();
        double value = event.values[0];
        MeasurementKind kind = MeasurementKind.UNKNOWN;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_HEART_RATE:
                kind = MeasurementKind.HEART_RATE;
                textHeartRate.setText("Heart Rate: " + value + " bpm");
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                kind = MeasurementKind.STEPS;
                textSteps.setText("Steps: " + (stepsTotal += value));
                System.out.println(value + " " + now.getTime() + " " + ++counter);
                break;
        }
        String msg = "";
        msg += value;
        msg += ",";
        msg += now.getTime();
        msg += ",";
        msg += kind.getId();
        String datapath = citizenhubPath + nodeIdString;
        new SendMessage(datapath, msg).start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("Wear Main Activity", "onAccuracyChanged - accuracy: " + accuracy);
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("WearOSHeartRateProtocol")){
                String onMessageReceived = intent.getStringExtra("WearOSHeartRateProtocol");
                if (onMessageReceived.equals("enabled")){
                    wearOSHeartRateProtocol = true;}
                else{wearOSHeartRateProtocol = false;}
            }
            if (intent.hasExtra("WearOSStepsProtocol")){
                String onMessageReceived = intent.getStringExtra("WearOSStepsProtocol");
                if (onMessageReceived.equals("enabled")){
                    wearOSStepsProtocol = true;}
                else{ wearOSStepsProtocol = false;}
            }
            if (intent.hasExtra("WearOSAgent")){
                String onMessageReceived = intent.getStringExtra("WearOSAgent");
                if (onMessageReceived.equals("enabled")){
                    wearOSAgent = true;}
                else{wearOSAgent = false;}
            }
            setScreenInfoText();

            if  (intent.hasExtra("message")){
                String onMessageReceived = intent.getStringExtra("message");
                connected = true;
                if (onMessageReceived.equals("Connected")) {
                    connected = true;
                }
                else if (onMessageReceived.equals("HeartRate")) {
                    onClickMessage = "You have a Heart Rate of: " + heartRate + ".";
                    getInfo(onClickMessage);
                }
                else if (onMessageReceived.equals("Steps")) {
                    onClickMessage = "You have walked a total of " + steps + " steps today.";
                    getInfo(onClickMessage);
                }
            }
        }
    }

    private void setScreenInfoText(){
        if (wearOSAgent) {
            textInfoPhone.setText("Phone connected");
        }
        else {
            textInfoPhone.setText("Phone not connected");
        }
        if (wearOSStepsProtocol && wearOSHeartRateProtocol) {
            textInfoPhone.setText("Phone connected");
            textInfoProtocols.setText("Sending: Heart Rate, Steps");
        }
        else if (wearOSHeartRateProtocol)
        {
            textInfoPhone.setText("Phone connected");
            textInfoProtocols.setText("Sending: Heart Rate");
        }
        else if (wearOSStepsProtocol)
        {
            textInfoPhone.setText("Phone connected");
            textInfoProtocols.setText("Sending: Steps");
        }
        else{
            textInfoProtocols.setText("Not sending data");
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

    class GetLocalNode extends Thread {
        GetLocalNode() {}
        public void run() {
            Task<Node> nodeTask = Wearable.getNodeClient(getApplicationContext()).getLocalNode();
            try {
                Node node = Tasks.await(nodeTask);
                nodeIdString = node.getId();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
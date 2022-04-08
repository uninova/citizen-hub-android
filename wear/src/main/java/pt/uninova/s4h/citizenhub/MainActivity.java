/*package com.smart4health.wearos;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import 	androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.Node;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends WearableActivity implements SensorEventListener {


    private TextView infoTextView;
    Button stepsButton, heartRateButton;
    String onClickMessage;
    private SensorManager mSensorManager;
    private Sensor mStepSensor, mHeartSensor;
    int heartRate=0, steps=0;

    private void sensorsManager(){
        mSensorManager = ((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        mHeartSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for testing only. Keeps screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        infoTextView =  findViewById(R.id.textView);
       // heartRateButton =  findViewById(R.id.heartRateOnClick);
       // stepsButton = findViewById(R.id.stepsOnClick);

        //Register the local broadcast receiver//
        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);

        permissionRequest();
        sensorsManager();

        //Create an OnClickListener//
      /*  heartRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMessage = "" + heartRate ;
                infoTextView.setText(onClickMessage);
                getInfo(onClickMessage);
            }
        });
        stepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMessage = "" + steps;
                infoTextView.setText(onClickMessage);
                getInfo(onClickMessage);
            }
        });*/
/*
    }
    public void getInfo(String msg){
        //Make sure you’re using the same path value//
        String datapath = "/my_path";
        new SendMessage(datapath, msg).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            mSensorManager.registerListener(this, mStepSensor, mSensorManager.SENSOR_DELAY_NORMAL);
        }
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null){
            mSensorManager.registerListener(this, mHeartSensor, mSensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void permissionRequest(){

        if (checkSelfPermission(Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.BODY_SENSORS},
                    21);
            Log.d("Permissions","REQUESTED");
        }
        else{
            Log.d("Permissions","ALREADY GRANTED");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
           heartRate  = (int)event.values[0];
            Log.i("HR","HEART RATE COUNT: " + heartRate);

        }
        else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            steps = (int)event.values[0];
            Log.i("STEPS","SETPS COUNT: " + steps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("Wear Main Activity", "onAccuracyChanged - accuracy: " + accuracy);

    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String onMessageReceived = intent.getStringExtra("message");
            Log.i("onReceive", onMessageReceived);

            if((onMessageReceived.equals("HeartRate"))){
                Log.i("onMessageReceived", "just got here " + onMessageReceived);
                onClickMessage = "You have a Heart Rate of: " + heartRate + ".";
                getInfo(onClickMessage);
            }else if(onMessageReceived.equals("Steps")){
                onClickMessage = "You have walked a total of " + steps + " steps today.";
                getInfo(onClickMessage);
            }

            String messageToHandheld = "Sending msg to handheld";

            infoTextView.setText(onClickMessage);
        }
    }
    class SendMessage extends Thread {
        String path;
        String message;
        //Constructor///
        SendMessage(String p, String m) {
            path = p;
            message = m;
        }

//Send the message via the thread. This will send the message to all the currently-connected devices//

        public void run() {
            //Get all the nodes//
            Task<List<Node>> nodeListTask =
                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                //Block on a task and get the result synchronously//
                List<Node> nodes = Tasks.await(nodeListTask);
                //Send the message to each device//
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(), path, message.getBytes());
                    try {
                        Integer result = Tasks.await(sendMessageTask);

                    } catch (ExecutionException exception) {
                        //TO DO//
                    } catch (InterruptedException exception) {
                        //TO DO//
                    }
                }
            } catch (ExecutionException exception) {
                //TO DO//
            } catch (InterruptedException exception) {
                //TO DO//
            }
        }
    }
}
*/
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
import persistence.DataBaseHelper;
import persistence.Measurement;
import persistence.MeasurementKind;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends WearableActivity implements SensorEventListener {


    String onClickMessage, nodeIdString;
    double heartRate = 0, steps = 0;
    Measurement measurement;
    DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
    String citizenhubPath = "/citizenhub_path_";
    String checkConnectionPath = "checkConnection";
    int stepsTotal = 0;
    private TextView textHeartRate, textSteps;
    private SensorManager mSensorManager;
    private Sensor mStepSensor, mHeartSensor;
    private boolean connected = false;

    private void sensorsManager() {
        mSensorManager = ((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        mHeartSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR); //MUDEI DE STEP_COUNTER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for testing only. Keeps screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textSteps = findViewById(R.id.textSteps);
        textHeartRate = findViewById(R.id.textHearRate);

        textSteps.setText(getString(R.string.main_activity_steps) + stepsTotal);
        textHeartRate.setText(getString(R.string.main_activity_heart_rate) + heartRate);

        //Register the local broadcast receiver//
        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);

        //request permissions
        permissionRequest();

        sensorsManager();
        connected = false;

        //stepsTotal = 0;

    }

    public boolean getInfo(String msg) {
        //Make sure you’re using the same path value//
        new GetLocalNode().start();

        if (msg.equals(getString(R.string.main_activity_check_connection_message))) {
            String datapath = citizenhubPath + checkConnectionPath;
            new SendMessage(datapath, msg).start();

        } else {
            String newMsg = toCsvFormat(msg);
            String[] msgArray = newMsg.split(",");
            System.out.println("AQUI TOU EU:::" + msgArray[0] + " " + msgArray[1] + " " + msgArray[2] + " " + msgArray[3] + " " + msgArray[4]);
            //stepsTotal += Integer.getInteger(msgArray[0]);
            if (msgArray[3].equals("STEPS")) {
                stepsTotal += (int) Double.parseDouble(msgArray[1]);
                System.out.println((int) Double.parseDouble(msgArray[1]));
                textSteps.setText(getString(R.string.main_activity_steps) + stepsTotal);
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
                textHeartRate.setText(getString(R.string.main_activity_heart_rate) + value + getString(R.string.main_activity_heart_rate_units));
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                kind = MeasurementKind.STEPS;
                textSteps.setText(getString(R.string.main_activity_steps) + (stepsTotal += value));
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

        /*

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String connectionCheck = "Check Connection";

        if (!connected) {
            getInfo(connectionCheck);
        }

        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {

            if (connected) {
                heartRate = (double) event.values[0];
                //Log.i("HR", "HEART RATE COUNT: " + heartRate + " Connected = " + connected + " Size: " + dataBaseHelper.getUnsentSize());
                textHeartRate.setText("Last HR: " + heartRate);
                if (dataBaseHelper.getUnsentSize() <= 2) {
                    try {
                        measurement = new Measurement(-1, "HEART_RATE", heartRate, false, timestamp);

                    } catch (Exception e) {
                        //Toast.makeText(MainActivity.this, "Error saving HeartRate data", Toast.LENGTH_SHORT).show();
                    }
                    boolean success = dataBaseHelper.addOne(measurement);
                    //Toast.makeText(MainActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();

                }
                while (dataBaseHelper.getUnsentSize() > 2) {
                    Log.i("HR", "DEBUG CONTENT VALUES " + dataBaseHelper.getUnsent().toString());
                    String unsentMsg = "" + dataBaseHelper.getUnsent();
                    getInfo(unsentMsg);
                }
            } else {
                heartRate = (double) event.values[0];
                //Log.i("HR", "HEART RATE COUNT: " + heartRate + " Connected = " + connected);
                textHeartRate.setText("Last HR: " + heartRate);
                try {
                    measurement = new Measurement(-1, "HEART_RATE", heartRate, false, timestamp);

                } catch (Exception e) {
                    // Toast.makeText(MainActivity.this, "Error saving HeartRate data", Toast.LENGTH_SHORT).show();
                }
                Boolean success = dataBaseHelper.addOne(measurement);
                //Toast.makeText(MainActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
            }

        } else if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {


            if (connected) {
                steps = (double) event.values[0];
                Log.i("STEPS", "STEPS COUNT: " + steps + " Connected = " + connected + " Size: " + dataBaseHelper.getUnsentSize());

                if (dataBaseHelper.getUnsentSize() <= 2) {
                    try {
                        measurement = new Measurement(-1, "STEPS", steps, false, timestamp);

                    } catch (Exception e) {
                        //Toast.makeText(MainActivity.this, "Error saving Steps data", Toast.LENGTH_SHORT).show();
                    }
                    boolean success = dataBaseHelper.addOne(measurement);
                    //Toast.makeText(MainActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();


                }
                while (dataBaseHelper.getUnsentSize() > 2) {
                    Log.i("Steps", "DEBUG CONTENT VALUES " + dataBaseHelper.getUnsent().toString());
                    String unsentMsg = "" + dataBaseHelper.getUnsent();
                    getInfo(unsentMsg);
                }
            } else {
                steps = (double) event.values[0];
                //stepsTotal += steps;

                System.out.println("Steps COUNT: " + steps + " Connected = " + connected);
                //textSteps.setText("Last Steps: " + steps);
                try {
                    measurement = new Measurement(-1, "STEPS", steps, false, timestamp);

                } catch (Exception e) {
                    //Toast.makeText(MainActivity.this, "Error saving Steps data", Toast.LENGTH_SHORT).show();
                }
                Boolean success = dataBaseHelper.addOne(measurement);
                //Toast.makeText(MainActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
            }
        }
        connected = false;

         */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("Wear Main Activity", "onAccuracyChanged - accuracy: " + accuracy);

    }

    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String onMessageReceived = intent.getStringExtra("message");
            // Log.i("onReceive", onMessageReceived);
            connected = true;
            if ((onMessageReceived.equals("Connected"))) {
                connected = true;
            }
            if ((onMessageReceived.equals("HeartRate"))) {
                Log.i("onMessageReceived", "just got here " + onMessageReceived);
                onClickMessage = getString(R.string.main_activity_heart_rate_message) + heartRate + ".";
                //System.out.println("BROADCAST RECEIVER" + heartRate);
                getInfo(onClickMessage);
            } else if (onMessageReceived.equals("Steps")) {
                onClickMessage = getString(R.string.main_activity_steps_message) + steps + getString(R.string.main_activity_steps_message_2);
                //System.out.println("BROADCAST RECEIVER" + heartRate);
                getInfo(onClickMessage);
            }
            //infoTextView.setText(onClickMessage);
        }
    }

    class SendMessage extends Thread {
        String path;
        String message;

        //Constructor///

        SendMessage(String p, String msg) {
            path = p;
            message = msg;
        }

//Send the message via the thread. This will send the message to all the currently-connected devices//

        public void run() {
            //System.out.println("I'm on the Run");
            //Get all the nodes//
            Task<List<Node>> nodeListTask = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();


            try {
                Task<Node> t = Wearable.getNodeClient(getApplicationContext()).getLocalNode();
                Node n = Tasks.await(t);
                System.out.println(n.getId() + " " + message);
                //Block on a task and get the result synchronously//
                List<Node> nodes = Tasks.await(nodeListTask);
                //System.out.println(nodes.size());
                //Send the message to each device//
                for (Node node : nodes) {
                    //System.out.println(node.getId());
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(), path, message.getBytes());

                    try {
                        Integer result = Tasks.await(sendMessageTask);

                    } catch (ExecutionException exception) {
                        //TO DO//
                    } catch (InterruptedException exception) {
                        //TO DO//
                    }
                }
            } catch (ExecutionException exception) {
                //TO DO//

            } catch (InterruptedException exception) {
                //TO DO//
            }
            // connected = false;
        }
    }

    class GetLocalNode extends Thread {


        GetLocalNode() {

        }


        public void run() {

            //Get all the nodes//

            Task<Node> nodeTask = Wearable.getNodeClient(getApplicationContext()).getLocalNode();

            try {
                //Block on a task and get the result synchronously//
                Node node = Tasks.await(nodeTask);
                nodeIdString = node.getId();


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

    }
}
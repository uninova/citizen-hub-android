package pt.uninova.s4h.citizenhub.ui.posture;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import datastorage.DatabaseHelperInterface;
import datastorage.MeasurementsContract;
import kbz.s4h.S4HDCM.BLELibrary;
import pt.uninova.s4h.citizenhub.ui.Home;
import pt.uninova.s4h.citizenhub.ui.R;

import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_CHARACTERISTIC_NAME;
import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_MEASUREMENT_VALUE;
import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_SOURCE_UUID;
import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_TIMESTAMP;
import static pt.uninova.s4h.citizenhub.ui.Home.homecontext;

public class PostureFragment extends Fragment {

    private PostureViewModel galleryViewModel;
    String current_back_posture = "";

    Boolean sensor = false; //put true when sensor is on
    JSONObject sensor_output;
    Boolean sensor_run_once = false;
    Button update;
    Button start;
    Button connect;
    Button disconnect;
    TextView back_posture_text;
    ImageView back_posture_image;
    Boolean run_connect = false;
    Boolean run_start = false;
    private boolean isLanguageInvalid = false;
    public static boolean opened = false;
    public static boolean finished = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(PostureViewModel.class);
        View root = inflater.inflate(R.layout.fragment_postureposition, container, false);

        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        Home.fab.hide();

        back_posture_text = root.findViewById(R.id.textView_PP);
        back_posture_image = root.findViewById(R.id.imageView_PP);
        back_posture_text.setText("Please connect the S4H device.");
        //back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.connect_device));
        back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_smart_shirt));
        update = root.findViewById(R.id.button_update);
        start = root.findViewById(R.id.button_start);
        connect = root.findViewById(R.id.button_connect);
        disconnect = root.findViewById(R.id.button_disconnect);

        //on create
        run_connect = false;
        run_start = false;
        sensor_run_once = false;
        finished = true;

        //initialize object
        Home.ble = new BLELibrary(getContext());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(run_connect && run_start && !finished)
                {
                    try {
                        updateButton(v);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!finished) {
                       new AsyncCaller().execute();
                    }
                }
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!run_connect){
                    back_posture_text.setText("Click connect first.");
                    return;
                }
                run_start = true;
                if(Home.ble.isSensorConnected()) {
                    back_posture_text.setText("Sensor is ready, Update Posture.");
                    Toast.makeText(getContext(), "Sensor connected", Toast.LENGTH_LONG).show();
                    //back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.connected));
                    back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_smart_shirt));
                    Home.ble.startRAW();
                }
                else{
                    back_posture_text.setText("Please connect the S4H device.");
                    //back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.connect_device));
                    back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_smart_shirt));
                }
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_start = false;
                if(Home.ble.isSensorConnected()) {
                    back_posture_text.setText("Sensor is connected, Click Start.");
                    Toast.makeText(getContext(), "Sensor connected", Toast.LENGTH_LONG).show();
                    //back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.connected));
                    back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_smart_shirt));
                    finished = false;
                    run_connect = true;
                    return;
                }
                back_posture_text.setText("Please connect the S4H device.");
                //back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.connect_device));
                back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_smart_shirt));
                Home.ble.pairNewDevice();
        /*if(sensor_run_once)
            Home.ble.pairNewDevice();//continuous light means connected
        else
            Home.ble.startScan();*/
                Toast.makeText(getContext(), "Sensor connecting...\nDon't Forget to click the Agent Button", Toast.LENGTH_LONG).show();
            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Home.ble.isSensorConnected()) {
                    back_posture_text.setText("Please connect the S4H device.");
                    //back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.connect_device));
                    back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_smart_shirt));
                }
                else {
                    Home.ble.shutDownDevice();
                    setBackPosture("disconnected");
                    Toast.makeText(getContext(), "Sensor disconnected", Toast.LENGTH_LONG).show();
                    run_connect = false;
                    run_start = false;
                    sensor_run_once = false;
                    finished = true;
                }
            }
        });
        return root;
    }

    public void saveData (int value, String characteristicName) {
        SQLiteOpenHelper SQLiteOpenHelper = new DatabaseHelperInterface(homecontext);
        SQLiteDatabase db = SQLiteOpenHelper.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        String timestamp = cal.getTime().toString();
        String values = String.valueOf(value);
        String name = characteristicName;
        String uuid = "00002a37-0000-1000-8000-00805f9b34fa";

        ContentValues Cvalues = new ContentValues();
        Cvalues.put(COLUMN_TIMESTAMP, timestamp);
        Cvalues.put(COLUMN_MEASUREMENT_VALUE, values);
        Cvalues.put(COLUMN_CHARACTERISTIC_NAME, name);
        Cvalues.put(COLUMN_SOURCE_UUID, uuid);

        db.insertWithOnConflict("measurements", null, Cvalues, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d("TABLEWTF", String.valueOf(Home.SQLiteOpenHelper.getReadableDatabase().query(MeasurementsContract.MeasureEntry.TABLE_NAME, null, null, null, null, null, null)));
    }

    public void test(BLELibrary ble){ //don't mind this.
        //Posture sensor
        //Instantiate
        ble = new BLELibrary(getContext());
        //Pair with device
        ble.pairNewDevice(); //or Home.ble.startScan();
        //Collect data
        ble.startRAW();
        //Stop collection
        //Home.ble.stopRecording();
        //Get collected data
        JSONObject bleResult = ble.getRAWJSON();
        try {
            Log.i("Home.ble test", bleResult.getString("BODYPOSITION"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        public void updateButton(View view) throws JSONException {
            if(!run_connect){
                back_posture_text.setText("Click connect first.");
                return;
            }
            if(!run_start){
                back_posture_text.setText("Click start first.");
                return;
            }
            if(Home.ble.isSensorConnected()) {
                sensor_output = Home.ble.getRAWJSON();

                back_posture_text.setText("Sensor is ready, Update Posture.");
                //Toast.makeText(getContext(), "Sensor connected", Toast.LENGTH_LONG).show();
                //back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.connected));
                back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_smart_shirt));

                current_back_posture = sensor_output.getString("POSTURE");
                //Log.i("debuga", current_back_posture + " " + sensor_output.getString("POSTURE") + " " + "Ola");
                //structure: {"SESSION":1,"SAMPLE":1,"ACCX":1.0,,"ACCY":2.0,,"ACCZ":9.0," AIFROMRAW":120, "BODYPOSITION": SEATED, "POSTURE": BAD}
                //use AIFROMRAW as a Activity Index: 0-80 still | 130 average for slow walking | average for walking/moderate activity |
                //780 average for running, this can be calibrated for each person (asking it to do these things in the beginning, but
                //this feature is for later
                setBackPosture(current_back_posture);
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) * 1000L);
                //cal.add(Calendar.DATE, -3); //to add previous days, for DailyReport debug
                String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
                //AppendFileAll(current_back_posture + "H " + date + "T" + "\n"); //dump to local file
                //GOOD is 1, BAD is 0
                if (current_back_posture.equals("GOOD"))
                    saveData(1, "BackPosture");
                else
                    saveData(0, "BackPosture");
                Log.i("debuga", sensor_output.toString() + " " + current_back_posture + " " + sensor_output.getString("POSTURE"));
            }
            else{
                back_posture_text.setText("Please connect the S4H device.");
                //back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.connect_device));
                back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_smart_shirt));
            }
        }

        private class AsyncCaller extends AsyncTask<Void, Void, Void>
        {
            @Override
            protected Void doInBackground(Void... params) {
                //this method will be running on background thread so don't update UI frome here
                //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (!finished) {
                    update.performClick();
                }
                //Log.d("debug", String.valueOf(finished));
                //this method will be running on UI thread
            }
        }

        public void setBackPosture(String state){
            if (state.matches("GOOD"))
            {
                back_posture_text.setText("Your current posture is Good!");
                back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_good_posture));
            }
            else if (state.matches("BAD"))
            {
                back_posture_text.setText("Your current posture is Bad!");
                back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_bad_posture));
            }
            else
            {
                back_posture_text.setText("Please connect the S4H device.");
                //back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.connect_device));
                back_posture_image.setImageDrawable(getResources().getDrawable(R.drawable.img_smart_shirt));
            }
        }

        /*public Boolean AppendFileAll(String msg) {
            // file with all measurements
            FileInputStream fis = null;
            BufferedReader reader = null;
            try {
                fis = openFileInput("s4h_bp_all.txt");
                reader = new BufferedReader(new InputStreamReader(fis));
                String line = null;
                String last = null;

                while ((line = reader.readLine()) != null) {
                    last = line;
                }
                //Log.i("bodyposition", last.substring(last.indexOf("H"),last.lastIndexOf("T")-3));
                //Log.i("bodyposition", msg.substring(msg.indexOf("H"),msg.lastIndexOf("T")-3));

                if(!(last.indexOf("H")>0))
                {
                    try {
                        FileOutputStream fileout=openFileOutput("s4h_bp_all.txt", MODE_APPEND);
                        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                        outputWriter.append(msg);
                        Log.i("bodyposition", msg);
                        outputWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                if (last.substring(0,last.lastIndexOf("H")).equals("STANDING"))
                {
                    Log.i("bodyposition", "Evitei duplicado no ficheiro por estar em pé");
                    reader.close();
                    return false;
                }
                else if(last.substring(last.indexOf("H"),last.lastIndexOf("T")-3).equals(msg.substring(msg.indexOf("H"),msg.lastIndexOf("T")-3)))
                {
                    Log.i("bodyposition", "Evitei duplicado no ficheiro por tempo");
                    reader.close();
                    return false;
                }
                else if(last.substring(0,last.lastIndexOf("H")).equals(msg.substring(0,msg.lastIndexOf("H"))))
                {
                    Log.i("bodyposition", "Evitei duplicado no ficheiro por não mudar posição");
                    reader.close();
                    return false;
                }
                else{
                    try {
                        FileOutputStream fileout=openFileOutput("s4h_bp_all.txt", MODE_APPEND);
                        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                        outputWriter.append(msg);
                        Log.i("bodyposition", msg);
                        outputWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }*/
}
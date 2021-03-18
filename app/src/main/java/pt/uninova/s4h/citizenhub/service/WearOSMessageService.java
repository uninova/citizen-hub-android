package pt.uninova.s4h.citizenhub.service;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uninova.s4h.citizenhub.MainActivity;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSAgent;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnection;
import pt.uninova.util.Pair;


public class WearOSMessageService extends FragmentActivity implements MessageClient.OnMessageReceivedListener {

    private String nodeIdString, mobileIDString;
    private static final String TAG = "WearOSMessageService";
    private Map<String,WearOSConnection> connectionMap = new HashMap<>();
    String citizenhubPath = "/citizenhub_path_";
    String checkConnectionPath = "checkConnection";
    Context appContext;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Entered Stop"  );
        Wearable.getMessageClient(appContext).removeListener(this);
    }



    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        new GetConnectedNode("wear", appContext).start();
        String datapath = citizenhubPath + nodeIdString;

        if(connectionMap.get(nodeIdString)!= null){
            WearOSConnection wearOSConnection = connectionMap.get(nodeIdString);
            new SendMessage(citizenhubPath+mobileIDString,"Connected",appContext).start();

            if (messageEvent.getPath().equals(citizenhubPath+checkConnectionPath)) {
                new  GetConnectedNode("mobile", appContext).start();
                new SendMessage(citizenhubPath+mobileIDString,"Connected",appContext).start();

            }else if(messageEvent.getPath().equals(datapath)){
                wearOSConnection.enable();
                String message = new String(messageEvent.getData());
                Log.d("MessageService", "message: "+ message);

                if (message.equals("")){
                    //do nothing
                }else{
                    String[] newMessage = message.split(",");
                    wearOSConnection.onCharacteristicChanged(newMessage);
                }
            }else {
                Wearable.getMessageClient(appContext).removeListener(this);
            }
        }
    }

    public String getAddress(WearOSConnection connection){
        String deviceAddress = connection.getAddress();
        return deviceAddress;
    }


    //lista de wearOsconnection para cada id || Connection recebe dispatcher fica a observar o que é o dispatcher
    //enviar sinal para receber dados || mecanismo para passar dados para a connection ( metodo de adicionar observer )
    public WearOSConnection connect(String address, CitizenHubService service){
        Log.d(TAG, "Entered connect with address " + address);
        appContext = service;
        Wearable.getMessageClient(service).addListener(this);
        WearOSConnection wearOSConnection = new WearOSConnection(address);
        connectionMap.put(address, wearOSConnection);

        return wearOSConnection;
    }

    class SendMessage extends Thread {
        String path;
        String message;
        Context context;


        SendMessage(String p, String m, Context c) {
            path = p;
            message = m;
            context = c;
        }


        public void run() {

            Task<List<Node>> wearableList = Wearable.getNodeClient(context).getConnectedNodes();
            try {

                List<Node> nodes = Tasks.await(wearableList);
                for (Node node : nodes) {

                    if(connectionMap.containsKey(node.getId())) {
                        Task<Integer> sendMessageTask =
                                Wearable.getMessageClient(context).sendMessage(nodeIdString, path, message.getBytes());
                        try {
                            Integer result = Tasks.await(sendMessageTask);

                        } catch (ExecutionException exception) {
                            //TO DO: Handle the exception//

                        } catch (InterruptedException exception) {

                        }
                    }

                }
            } catch (ExecutionException exception) {
                //TO DO: Handle the exception//

            } catch (InterruptedException exception) {
                //TO DO: Handle the exception//
            }
        }
    }

    class GetConnectedNode extends Thread {
        String type;
        Context context;


        GetConnectedNode(String t, Context c) {
            type=t;
            context = c;

        }

        public void run() {
            if(type.equals("mobile")){

                Task<Node> nodeTask = Wearable.getNodeClient(context).getLocalNode();

                try {
                    //Block on a task and get the result synchronously//
                    Node node = Tasks.await(nodeTask);
                    mobileIDString = node.getId();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }else if(type.equals("wear")){

                //Get all the nodes//
                Task<List<Node>> nodeTaskList = Wearable.getNodeClient(context).getConnectedNodes();

                try {
                    //Block on a task and get the result synchronously//
                    List<Node> nodes = Tasks.await(nodeTaskList);
                    for (Node node : nodes) {
                        nodeIdString = node.getId();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

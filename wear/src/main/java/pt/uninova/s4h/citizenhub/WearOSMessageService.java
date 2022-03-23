package pt.uninova.s4h.citizenhub;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageEvent;
import 	androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WearOSMessageService extends WearableListenerService {
    private String nodeIdString;
    String citizenhubPath = "/citizenhub_path_";
    String checkConnectionPath = "checkConnection";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
      //  Log.d("MessageService", "entered " );

        String message2 = new String(messageEvent.getData());

        new GetConnectedNode().start();
        String datapath = citizenhubPath + nodeIdString;

        String connectPath = citizenhubPath + checkConnectionPath;
      //  Log.d("MessageService", "mobile_id: " + nodeIdString);

        if (messageEvent.getPath().equals(datapath)) {
            final String message = new String(messageEvent.getData());
        //Broadcast the received data layer messages//
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }

    class GetConnectedNode extends Thread {


        GetConnectedNode() {

        }

//Send the message via the thread. This will send the message to all the currently-connected devices//

        public void run() {

            nodeIdString = null;
            Task<List<Node>> nodeTaskList = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();

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



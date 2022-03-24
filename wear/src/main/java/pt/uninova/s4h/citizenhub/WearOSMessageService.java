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

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        new GetConnectedNode().start();
        if(messageEvent.getPath().equals(citizenhubPath+"WearOSHeartRateProtocol"))
        {
            final String message = new String(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("WearOSHeartRateProtocol", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        if(messageEvent.getPath().equals(citizenhubPath+"WearOSStepsProtocol"))
        {
            final String message = new String(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("WearOSStepsProtocol", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        if(messageEvent.getPath().equals(citizenhubPath+"WearOSAgent"))
        {
            final String message = new String(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("WearOSAgent", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        if (messageEvent.getPath().equals(citizenhubPath + nodeIdString)) {
            final String message = new String(messageEvent.getData());
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
        GetConnectedNode() {}
        public void run() {
            nodeIdString = null;
            Task<List<Node>> nodeTaskList = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                List<Node> nodes = Tasks.await(nodeTaskList);
                for (Node node : nodes) {
                    nodeIdString = node.getId();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}



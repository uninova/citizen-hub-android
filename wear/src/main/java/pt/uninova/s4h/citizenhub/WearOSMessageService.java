package pt.uninova.s4h.citizenhub;

import android.content.Intent;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Arrays;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class WearOSMessageService extends WearableListenerService {
    String citizenHubPath = "/citizenhub_";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if(messageEvent.getPath().equals(citizenHubPath+"WearOSHeartRateProtocol"))
        {
            final String message = new String(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("WearOSHeartRateProtocol", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        if(messageEvent.getPath().equals(citizenHubPath+"WearOSStepsProtocol"))
        {
            final String message = new String(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("WearOSStepsProtocol", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        if(messageEvent.getPath().equals(citizenHubPath+"WearOSAgent"))
        {
            final String message = new String(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("WearOSAgent", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }
}

package pt.uninova.s4h.citizenhub;

import android.content.Intent;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class WearOSMessageService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String message = new String(messageEvent.getData());
        Intent messageIntent = new Intent();
        messageIntent.setAction(Intent.ACTION_SEND);
        messageIntent.putExtra(messageEvent.getPath(), message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
    }
}
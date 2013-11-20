package dk.redweb.Red_App.Network.PushMessages;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.EXTRA;
import dk.redweb.Red_App.ViewControllers.System.PushMessageForwarder.PushMessageForwarder;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/22/13
 * Time: 15:21
 */
public class GcmIntentService extends IntentService{

    private NotificationManager _NotificationManager;

    private int _notificationId;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle

            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String messageid = extras.getString("messageid");

                // Post notification of received message.
                sendNotification(messageid, extras.getString("title"), extras.getString("content"), extras.getString("type"));
                MyLog.i("Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String messageId, String title, String content, String type) {
        _notificationId = Integer.parseInt(messageId);

        _NotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent pushMessageForwarder = new Intent(this, PushMessageForwarder.class);
        pushMessageForwarder.putExtra(EXTRA.TYPE, type);
        pushMessageForwarder.putExtra(EXTRA.MESSAGEID, messageId);

        PendingIntent contentIntent = PendingIntent.getActivity(this, _notificationId, pushMessageForwarder, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.default_icon) //TODO make this configurable
                        .setContentTitle(title)
                        .setContentText(content);

        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        long[] pattern = {500,2000,500};
        builder.setVibrate(pattern);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        _NotificationManager.notify(_notificationId, builder.build());
    }
}

package dk.redweb.Red_App.ViewControllers.Contest.BikeTracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import dk.redweb.Red_App.MyLog;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/12/13
 * Time: 13:16
 */
public class BikeContestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        MyLog.v("BikeContestReceiver");

        context.startService(new Intent(context, BikePingLocation.class));
    }
}

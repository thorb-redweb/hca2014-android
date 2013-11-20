package dk.redweb.Red_App.Network.PushMessages;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import dk.redweb.Red_App.Interfaces.Delegate_uploadRegistrationAttributes;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.Network.ServerInterface;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.XmlHandling.XmlStore;

import java.io.IOException;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/31/13
 * Time: 9:21
 */
public class PushMessageInitializationHandling {

    private Activity _activity;
    private Delegate_uploadRegistrationAttributes _delegate;
    private ServerInterface _sv;
    private XmlStore _xml;

    private GoogleCloudMessaging gcm;

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public PushMessageInitializationHandling(Activity activity, Delegate_uploadRegistrationAttributes delegate){
        _activity = activity;
        _delegate = delegate;
        RedEventApplication app = (RedEventApplication)_activity.getApplication();
        _sv = app.getServerInterface();
        _xml = app.getXmlStore();
        gcm = GoogleCloudMessaging.getInstance(activity);
    }

    public static boolean checkInitialization(Activity activity){
        if (checkPlayServices(activity)) {
            String regid = getRegistrationId(activity);

            if (regid.isEmpty()) {
                return false;
            }
            return true;
        } else {
            MyLog.i("No valid Google Play Services APK found.");
        }
        return false;
    }

    private static boolean checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                MyLog.i("This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            MyLog.i("Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            MyLog.i("App version changed.");
            return "";
        }
        return registrationId;
    }

    private static SharedPreferences getGcmPreferences(Context context) {
        return context.getSharedPreferences(RedEventApplication.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    public static void removeRegistrationId(Context context){
        final SharedPreferences prefs = getGcmPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.commit();
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void initializePushService(String username){
        registerInBackground(username);
    }

    private void registerInBackground(final String username) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result;
                try {
                    result = doTheRegistration(username);
                } catch (IOException ex) {
                    //Repeat the call once, and if that doesn't work either, throw an error message
                    try {
                        result = doTheRegistration(username);
                    } catch (IOException e) {
                        result = "Error: " + e.getMessage();
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result.length() < 6 || result.substring(0,6).equals("Error:")){
                    _delegate.errorOccured(result);
                    return;
                }
                MyLog.v("Regid saved to device, " + result);
            }
        }.execute(null, null, null);
    }

    private String doTheRegistration(String username) throws IOException {
        if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(_activity.getApplicationContext());
        }
        String regid = gcm.register(_xml.projectId);

        String result = "Registration ID = " + regid;

        storeRegistrationIdOnServer(regid, username);

        // Persist the regID - no need to register again.
        storeRegistrationIdOnDevice(_activity.getApplicationContext(), regid);

        return result;
    }

    private void storeRegistrationIdOnServer(String regid, String username){
        _sv.uploadRegistrationAttributes(_delegate, regid, username);
    }

    private void storeRegistrationIdOnDevice(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        MyLog.i("Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
}

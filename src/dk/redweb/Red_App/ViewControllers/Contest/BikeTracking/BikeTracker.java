package dk.redweb.Red_App.ViewControllers.Contest.BikeTracking;

import android.app.Dialog;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import dk.redweb.Red_App.Interfaces.Delegate_biketracker;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.ViewControllers.Map.BaseMapFragmentActivity;
import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/29/13
 * Time: 14:39
 */
public class BikeTracker implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{

    private RedEventApplication _app;
    private FragmentActivity _activity;
    private Delegate_biketracker _delegate;
    private LocationClient _locationClient;

    private List<Location> _locations;
    private double _distance;
    private boolean _newReadingSinceLastDistanceCheck;
    private double _averagespeed;
    private boolean _newReadingSinceLastAverageSpeedCheck;
    private double _topspeed;
    private boolean _newReadingSinceLastTopSpeedCheck;

    private final static DecimalFormat decimalFormatter = new DecimalFormat("#.#");

    private boolean _isErrorDialogShowing = false;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static String KEY_UPDATES_ON = "KEY_UPDATES_ON";

    public BikeTracker(Delegate_biketracker delegate, RedEventApplication app, FragmentActivity activity){
        _delegate = delegate;
        _app = app;
        _activity = activity;

        if(servicesConnected()){
            _locationClient = new LocationClient(_activity, this, this);
        }
        _locations = new ArrayList<Location>();
    }

    public void insertTestData(){
        long time = new DateTime().getMillis();

        Location locationA = new Location("test");
        locationA.setLatitude(55.38442);
        locationA.setLongitude(10.44131);
        locationA.setTime(time + (2 * 60 * 1000));
        onLocationChanged(locationA);
    }

    public String getTopSpeed(){
        if(_newReadingSinceLastTopSpeedCheck){
            Location lastLoc = null;
            _topspeed = 0.0;
            for (Location loc : _locations){
                if (lastLoc != null){
                    long startTime = lastLoc.getTime();
                    long stopTime = loc.getTime();
                    double elapsedTime = stopTime - startTime;
                    double timeInHours = elapsedTime / (1000.0 * 60.0 * 60.0);

                    double distance = calculateDistanceBetweenTwoLocations(lastLoc, loc);
                    double distanceInKm = distance / 1000;

                    double speed =  distanceInKm/timeInHours;

                    if(speed > _topspeed){
                        _topspeed = speed;
                    }
                }
                lastLoc = loc;
            }
            _newReadingSinceLastTopSpeedCheck = false;
        }
        return decimalFormatter.format(_topspeed);
    }

    public String getAverageSpeed(){
        if(_newReadingSinceLastAverageSpeedCheck){
            if(_newReadingSinceLastDistanceCheck){
                getTotalDistanceTravelled();
            }

            long startTime = _locations.get(0).getTime();
            long stopTime = _locations.get(_locations.size()-1).getTime();
            double elapsedTime = stopTime - startTime;
            double timeInHours = elapsedTime / (1000.0 * 60.0 * 60.0);
            if(timeInHours == 0.0){
                return decimalFormatter.format(0.0);
            }

            double distanceInKm = _distance / 1000;
            _averagespeed =  distanceInKm/timeInHours;
            _newReadingSinceLastAverageSpeedCheck = false;
        }
        return decimalFormatter.format(_averagespeed);
    }

    public String getTotalDistanceTravelled(){
        if(_newReadingSinceLastDistanceCheck){
            Location lastLoc = null;
            _distance = 0.0;
            for (Location loc : _locations){
                if (lastLoc != null){
                    _distance += calculateDistanceBetweenTwoLocations(lastLoc, loc);
                }
                lastLoc = loc;
            }
            _newReadingSinceLastDistanceCheck = false;
        }
        return decimalFormatter.format(_distance);
    }

    private double calculateDistanceBetweenTwoLocations(Location a, Location b) {
        double pk = (180/3.14169);
        double lat_a = a.getLatitude();
        double lng_a = a.getLongitude();
        double lat_b = b.getLatitude();
        double lng_b = b.getLongitude();

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)*Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
    }

    public void startLocationClient(){
        MyLog.v("BikeTracker location client connecting");
        _locationClient.connect();
    }

    public void stopLocationClient(){
        if(servicesConnected()){
            MyLog.v("BikeTracker location client disconnecting");
            _locationClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = _locationClient.getLastLocation();
        onLocationChanged(location);
    }

    @Override
    public void onLocationChanged(Location location) {
        MyLog.v("Position received: " + location.getLatitude() + "," + location.getLongitude());
        _locations.add(location);
        _newReadingSinceLastDistanceCheck = true;
        _newReadingSinceLastAverageSpeedCheck = true;
        _newReadingSinceLastTopSpeedCheck = true;

        _delegate.averageSpeedUpdated(getAverageSpeed());
        _delegate.distanceUpdated(getTotalDistanceTravelled());
        _delegate.topSpeedUpdated(getTopSpeed());
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(_activity, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()){
            try{
                connectionResult.startResolutionForResult(_activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                MyLog.e("SendIntentException in onConnectionFailed", e);
            }
        }
        else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    protected boolean servicesConnected(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(_app);
        if(ConnectionResult.SUCCESS == resultCode){
            MyLog.d("Google Play Services is available");
            return true;
        }
        else
        {
            showErrorDialog(resultCode);
            return false;
        }
    }

    private void showErrorDialog(int errorCode)
    {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, _activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);

        if(errorDialog != null && !_isErrorDialogShowing){
            _isErrorDialogShowing = true;
            BaseMapFragmentActivity.ErrorDialogFragment errorFragment = new BaseMapFragmentActivity.ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(_activity.getSupportFragmentManager(), "Location Updates");
        }
    }
}

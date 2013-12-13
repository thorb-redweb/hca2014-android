package dk.redweb.Red_App.ViewControllers.Contest.BikeTracking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.MyLog;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/12/13
 * Time: 13:17
 */
public class BikePingLocation extends Service implements LocationListener{

    private LocationManager _locationManager;
    private Location _location;

    private boolean _running;

    @Override
    public void onLocationChanged(Location location) {
        MyLog.v("BikePingLocation:onLocationChanged");

        _location = location;

        new SubmitLocationTask(BikePingLocation.this).execute();
    }

    @Override
    public void onCreate() {
        MyLog.v("BikePingLocation:onCreate");
        _running = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        MyLog.v("BikePingLocation:onStart");

        if(!_running){
            _locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, this);
            _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000,
                    300f, this);
        }
        else {
            MyLog.v("No new locationmanager");
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        MyLog.v("BikePingLocation:onDestroy");
        _locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        MyLog.v("BikePingLocation:onStatusChanged: " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        MyLog.v("BikePingLocation:onProviderEnabled");
        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, this);
        _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000,
                300f, this);
    }

    @Override
    public void onProviderDisabled(String provider) {
        MyLog.v("BikePingLocation:onProviderDisabled");
        Toast.makeText(
                getApplicationContext(),
                "Attempted to ping your location, and GPS was disabled.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private class SubmitLocationTask extends AsyncTask<String, Void, Boolean> {

        private Service _service;

        public SubmitLocationTask(Service service) {
            _service = service;
        }

        @Override
        protected Boolean doInBackground(final String... args) {
            try {
                if (_location.getAccuracy() < 50f && _location.getAccuracy() != 0.0) {
                    BikeContestDatabase db = new BikeContestDatabase(_service);
                    db.addLocation(_location);

                    stopSelf();
                }

                return true;
            } catch (Exception e) {

                return false;
            }
        }
    }
}

package dk.redweb.Red_App.ViewControllers.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.StaticNames.EXTRA;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.Views.NavBarBox;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/19/13
 * Time: 1:57 PM
 */
public class BaseMapFragmentActivity extends FragmentActivity implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    protected RedEventApplication _app;
    protected DbInterface _db;

    protected String name;
    protected String _childname;
    protected XmlNode _page;

    protected GoogleMap _googleMap;

    private LocationClient _locationClient;
    private LocationRequest _locationRequest;

    SharedPreferences _sharedPrefs;
    SharedPreferences.Editor _editor;

    protected LatLng _standardCenter = new LatLng(55.3904767, 10.438474700000029);
    protected float _standardZoom = 12;
    protected LatLng _userLatLng;

    boolean _updatesRequested;

    boolean _isErrorDialogShowing = false;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static String KEY_UPDATES_ON = "KEY_UPDATES_ON";

    public static class ErrorDialogFragment extends DialogFragment{
        private Dialog _dialog;

        public ErrorDialogFragment(){
            super();
            _dialog = null;
        }

        public void setDialog(Dialog dialog){
            _dialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            return _dialog;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        _app = (RedEventApplication)getApplication();
        _db = _app.getDbInterface();

        Bundle extras = getIntent().getExtras();
        XmlNode page = (XmlNode)extras.getSerializable(EXTRA.PAGE);
        _page = page;

        try {
            name = page.getStringFromNode(PAGE.NAME);
        } catch (NoSuchFieldException e) {
            MyLog.e("NoSuchFieldException for 'name' in baseActivity:Constructor", e);
        }
        if(page.hasChild(PAGE.CHILD)){
            try {
                _childname = page.getStringFromNode(PAGE.CHILD);
            } catch (NoSuchFieldException e) {
                MyLog.e("NoSuchFieldException for 'child' in baseActivity:Constructor", e);
            }
        }

        _sharedPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        _editor = _sharedPrefs.edit();
        _editor.putBoolean(KEY_UPDATES_ON, true);
        _editor.commit();

        if(_page.hasChild(PAGE.ZOOM))
        {
            try {
                _standardZoom = _page.getFloatFromNode(PAGE.ZOOM);
            } catch (NoSuchFieldException e) {
                MyLog.e("Exception in BaseMapFragmentActivity:onCreate", e);
            }
        }
        if(_page.hasChild(PAGE.LATITUDE) && _page.hasChild(PAGE.LONGITUDE))
        {
            try {
                double latitude = _page.getDoubleFromNode(PAGE.LATITUDE);
                double longitude = _page.getDoubleFromNode(PAGE.LONGITUDE);
                _standardCenter = new LatLng(latitude, longitude);
            } catch (NoSuchFieldException e) {
                MyLog.e("Exception in BaseMapFragmentActivity:onCreate", e);
            }
        }

        _updatesRequested = false;

        if(servicesConnected()){
            _locationRequest = LocationRequest.create();
            _locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            _locationRequest.setInterval(15000);
            _locationRequest.setFastestInterval(1000);

            _locationClient = new LocationClient(this, this, this);
        } else{
            MyLog.e("Google Play Services not connected");
        }
    }

    public void onAfterContentViewCreated(){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        _googleMap = null;//((SupportMapFragment) fragmentManager.findFragmentById(R.id.map_googlemap)).getMap();
    }

    @Override
    public void onStart(){
        super.onStart();

        if(servicesConnected()){
            _locationClient.connect();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(_sharedPrefs.contains(KEY_UPDATES_ON)){
            _updatesRequested = _sharedPrefs.getBoolean(KEY_UPDATES_ON, false);
        } else {
            _editor.putBoolean(KEY_UPDATES_ON, false);
            _editor.commit();
        }

        NavBarBox navbar = _app.getNavbar();
        navbar.updateNavigationBar(_page);
    }

    @Override
    protected void onPause(){
        _editor.putBoolean(KEY_UPDATES_ON, _updatesRequested);
        _editor.commit();

        super.onPause();
    }

    @Override
    protected void onStop(){
        if(servicesConnected()){
            if(_locationClient.isConnected()){
                //_locationClient.removeLocationUpdates((LocationListener)this);
            }
            _locationClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle){
        MyLog.v("Connected");
        if(_updatesRequested){
            MyLog.v("RequestLocationUpdates");
            //_locationClient.removeLocationUpdates(_locationRequest, this);

            Location lastLoc = _locationClient.getLastLocation();
            if(lastLoc != null)
            {
                _userLatLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
                onLocationChanged(lastLoc);    //TODO: HACKS!!!!! Ensures that user icon is displayed. Fix if able.
            } else {
                _userLatLng = _standardCenter;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location){
        _userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderEnabled(String provider) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderDisabled(String provider) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
                switch (resultCode)  {
                    case Activity.RESULT_OK :
                        break;
                }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()){
            try{
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                MyLog.e("SendIntentException in onConnectionFailed", e);
            }
        }
        else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    protected boolean servicesConnected(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
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
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

        if(errorDialog != null && !_isErrorDialogShowing){
            _isErrorDialogShowing = true;
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getSupportFragmentManager(), "Location Updates");
        }
    }
}

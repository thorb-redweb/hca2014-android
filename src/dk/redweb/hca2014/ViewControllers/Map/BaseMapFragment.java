package dk.redweb.hca2014.ViewControllers.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import dk.redweb.hca2014.*;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.TextHelper.TextHelper;
import dk.redweb.hca2014.StaticNames.DEFAULTTEXT;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.StaticNames.TEXT;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.Views.FlexibleButton;
import dk.redweb.hca2014.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/18/13
 * Time: 12:51 PM
 */
public class BaseMapFragment extends BasePageFragment implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    protected SupportMapFragment _mapFragment;
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

    public static class ErrorDialogFragment extends DialogFragment {
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

    public BaseMapFragment(XmlNode page){
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, int resourceId){
        super.onCreateView(inflater, container, resourceId);

        _mapFragment = SupportMapFragment.newInstance();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.map_lnrMapFragmentBox, _mapFragment).commit();
        NavController.changeChildPageWithFragment(_mapFragment, this, R.id.map_lnrMapFragmentBox, false);

        _sharedPrefs = _app.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
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

            _locationClient = new LocationClient(getActivity(), this, this);
        } else{
            MyLog.e("Google Play Services not connected");
        }

        try {
            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.map_flxButtonBack);
            if(_page.hasChild(PAGE.RETURNBUTTON) && _page.getBoolFromNode(PAGE.RETURNBUTTON)){
                flxBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().popBackStack();
                    }
                });
            } else {
                flxBackButton.setVisibility(View.GONE);
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting ReturnButton attribute from page xml", e);
        }

        setAppearance();
        setText();

        return _view;
    }

    private void setAppearance() {
        try {
            AppearanceHelper helper = new AppearanceHelper(getActivity(), _locallook, _globallook);

            RelativeLayout rltBackground = (RelativeLayout)findViewById(R.id.map_rltMainview);
            helper.setViewBackgroundImageOrColor(rltBackground, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.map_flxButtonBack);
            helper.setViewBackgroundImageOrColor(flxBackButton, LOOK.BACKBUTTONBACKGROUNDIMAGE,
                    LOOK.BACKBUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.FlexButton.setImage(flxBackButton, LOOK.BACKBUTTONICON);
            helper.FlexButton.setTextColor(flxBackButton, LOOK.BACKBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.FlexButton.setTextSize(flxBackButton, LOOK.BACKBUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.FlexButton.setTextStyle(flxBackButton, LOOK.BACKBUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.FlexButton.setTextShadow(flxBackButton, LOOK.BACKBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.BACKBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for BaseMapFragment", e);
        }
    }

    private void setText() {
        try{
            TextHelper helper = new TextHelper(_view, _name, _xml);

            helper.setFlexibleButtonText(R.id.map_flxButtonBack, TEXT.BACKBUTTON, DEFAULTTEXT.BACKBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting text for BaseMapFragment", e);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        _googleMap = _mapFragment.getMap();

        if(servicesConnected()){
            _locationClient.connect();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        if(_sharedPrefs.contains(KEY_UPDATES_ON)){
            _updatesRequested = _sharedPrefs.getBoolean(KEY_UPDATES_ON, false);
        } else {
            _editor.putBoolean(KEY_UPDATES_ON, false);
            _editor.commit();
        }
    }

    @Override
    public void onPause(){
        _editor.putBoolean(KEY_UPDATES_ON, _updatesRequested);
        _editor.commit();

        super.onPause();
    }

    @Override
    public void onStop(){
        if(servicesConnected()){
            if(_locationClient.isConnected()){
                _locationClient.removeLocationUpdates(this);
            }
            _locationClient.disconnect();
        }

        super.onStop();
    }

    protected View findViewById(int id){
        return _view.findViewById(id);
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
        if(_app.isDebugging()){
            _userLatLng = _app.getDebugPosition();
        }

    }

    @Override
    public void onDisconnected() {
        Toast.makeText(getActivity(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
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
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                MyLog.e("SendIntentException in onConnectionFailed", e);
            }
        }
        else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    protected boolean servicesConnected(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
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
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);

        if(errorDialog != null && !_isErrorDialogShowing){
            _isErrorDialogShowing = true;
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getFragmentManager(), "Location Updates");
        }
    }
}
